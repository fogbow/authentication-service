package org.fogbowcloud.as.core.tokengenerator.plugins.cloudstack;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpResponseException;
import org.apache.log4j.Logger;
import org.fogbowcloud.as.common.constants.FogbowConstants;
import org.fogbowcloud.as.common.exceptions.FogbowException;
import org.fogbowcloud.as.common.exceptions.InvalidParameterException;
import org.fogbowcloud.as.common.exceptions.UnexpectedException;
import org.fogbowcloud.as.common.util.connectivity.HttpRequestClientUtil;
import org.fogbowcloud.as.core.PropertiesHolder;
import org.fogbowcloud.as.core.constants.ConfigurationConstants;
import org.fogbowcloud.as.core.constants.DefaultConfigurationConstants;
import org.fogbowcloud.as.core.constants.Messages;
import org.fogbowcloud.as.core.tokengenerator.TokenGeneratorPlugin;
import org.fogbowcloud.as.core.tokengenerator.plugins.AttributeJoiner;
import org.fogbowcloud.as.core.util.HttpToFogbowAsExceptionMapper;

public class CloudStackTokenGeneratorPlugin implements TokenGeneratorPlugin {
    private static final Logger LOGGER = Logger.getLogger(CloudStackTokenGeneratorPlugin.class);

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String DOMAIN = "domain";

    private String tokenProviderId;
    private String cloudStackUrl;
    private HttpRequestClientUtil client;

    public CloudStackTokenGeneratorPlugin() {
        this.tokenProviderId = PropertiesHolder.getInstance().getProperty(ConfigurationConstants.LOCAL_MEMBER_ID);
        this.cloudStackUrl = PropertiesHolder.getInstance().getProperty(ConfigurationConstants.CLOUDSTACK_ENDPOINT);
        String timeoutRequestStr = PropertiesHolder.getInstance().getProperty(
                ConfigurationConstants.HTTP_REQUEST_TIMEOUT, DefaultConfigurationConstants.HTTP_REQUEST_TIMEOUT);
        Integer timeoutHttpRequest = Integer.parseInt(timeoutRequestStr);
        this.client = new HttpRequestClientUtil(timeoutHttpRequest);
    }

    @Override
    public String createTokenValue(Map<String, String> credentials) throws FogbowException, UnexpectedException {
        if ((credentials == null) || (credentials.get(USERNAME) == null) || (credentials.get(PASSWORD) == null) ||
                credentials.get(DOMAIN) == null) {
            throw new InvalidParameterException(Messages.Exception.NO_USER_CREDENTIALS);
        }

        LoginRequest request = createLoginRequest(credentials);
        HttpRequestClientUtil.Response jsonResponse = null;
        try {
            // NOTE(pauloewerton): since all cloudstack requests params are passed via url args, we do not need to
            // send a valid json body in the post request
            jsonResponse = this.client.doPostRequest(request.getUriBuilder().toString(), "data");
        } catch (HttpResponseException e) {
            HttpToFogbowAsExceptionMapper.map(e);
        }

        LoginResponse response = LoginResponse.fromJson(jsonResponse.getContent());
        String tokenValue = getTokenValue(response.getSessionKey());
        return tokenValue;
    }

    private LoginRequest createLoginRequest(Map<String, String> credentials) throws InvalidParameterException {
        String userId = credentials.get(USERNAME);
        String password = credentials.get(PASSWORD);
        String domain = credentials.get(DOMAIN);

        LoginRequest loginRequest = new LoginRequest.Builder()
                .username(userId)
                .password(password)
                .domain(domain)
                .build(this.cloudStackUrl);

        return loginRequest;
    }

    private String getTokenValue(String sessionKey) throws FogbowException, UnexpectedException {
        ListAccountsRequest request = new ListAccountsRequest.Builder()
                .sessionKey(sessionKey)
                .build(this.cloudStackUrl);

        String jsonResponse = null;
        try {
            // NOTE(pauloewerton): passing a placeholder as there is no need to pass a valid token in this request
            jsonResponse = this.client.doGetRequest(request.getUriBuilder().toString(), "CloudStackTokenValue");
        } catch (HttpResponseException e) {
            HttpToFogbowAsExceptionMapper.map(e);
        }

        String tokenString = null;
        try {
            ListAccountsResponse response = ListAccountsResponse.fromJson(jsonResponse);
            // NOTE(pauloewerton): considering only one account/user per request
            ListAccountsResponse.User user = response.getAccounts().get(0).getUsers().get(0);

            // NOTE(pauloewerton): keeping a colon as separator as expected by the other cloudstack plugins
            String tokenValue = user.getApiKey() + ":" + user.getSecretKey();
            String userId = user.getId();
            String firstName = user.getFirstName();
            String lastName = user.getLastName();
            String userName = (firstName != null && lastName != null) ? firstName + " " + lastName : user.getUsername();

            Map<String, String> attributes = new HashMap<>();
            attributes.put(FogbowConstants.PROVIDER_ID_KEY, this.tokenProviderId);
            attributes.put(FogbowConstants.USER_ID_KEY, userId);
            attributes.put(FogbowConstants.USER_NAME_KEY, userName);
            attributes.put(FogbowConstants.TOKEN_VALUE_KEY, tokenValue);
            return AttributeJoiner.join(attributes);
        } catch (Exception e) {
            LOGGER.error(Messages.Error.UNABLE_TO_GET_TOKEN_FROM_JSON, e);
            throw new UnexpectedException(Messages.Error.UNABLE_TO_GET_TOKEN_FROM_JSON, e);
        }
    }

    // Used for testing
    public void setClient(HttpRequestClientUtil client) {
        this.client = client;
    }
}
