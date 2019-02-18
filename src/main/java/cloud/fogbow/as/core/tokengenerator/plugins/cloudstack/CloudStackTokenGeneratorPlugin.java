package cloud.fogbow.as.core.tokengenerator.plugins.cloudstack;

import java.util.HashMap;
import java.util.Map;

import cloud.fogbow.common.constants.CloudStackConstants;
import cloud.fogbow.common.constants.HttpMethod;
import cloud.fogbow.common.exceptions.InvalidParameterException;
import cloud.fogbow.common.util.connectivity.HttpResponse;
import cloud.fogbow.common.util.connectivity.HttpRequestClientUtil;
import cloud.fogbow.as.core.PropertiesHolder;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;
import org.apache.log4j.Logger;
import cloud.fogbow.common.constants.FogbowConstants;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.as.constants.ConfigurationPropertyKeys;
import cloud.fogbow.as.constants.ConfigurationPropertyDefaults;
import cloud.fogbow.as.constants.Messages;
import cloud.fogbow.as.core.tokengenerator.TokenGeneratorPlugin;
import cloud.fogbow.as.core.tokengenerator.plugins.AttributeJoiner;
import cloud.fogbow.as.core.util.HttpToFogbowAsExceptionMapper;

public class CloudStackTokenGeneratorPlugin implements TokenGeneratorPlugin {
    private static final Logger LOGGER = Logger.getLogger(CloudStackTokenGeneratorPlugin.class);

    private HttpRequestClientUtil client;
    private String cloudStackUrl;
    private String tokenProviderId;

    public CloudStackTokenGeneratorPlugin() {
        this.tokenProviderId = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.LOCAL_MEMBER_ID_KEY);
        this.cloudStackUrl = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.CLOUDSTACK_URL_KEY);
        String timeoutRequestStr = PropertiesHolder.getInstance().getProperty(
                ConfigurationPropertyKeys.HTTP_REQUEST_TIMEOUT_KEY, ConfigurationPropertyDefaults.HTTP_REQUEST_TIMEOUT);
        Integer timeoutHttpRequest = Integer.parseInt(timeoutRequestStr);
        this.client = new HttpRequestClientUtil();
    }

    public CloudStackTokenGeneratorPlugin(HttpRequestClientUtil client, String cloudStackUrl, String tokenProviderId) {
        this.client = client;
        this.cloudStackUrl = cloudStackUrl;
        this.tokenProviderId = tokenProviderId;
    }

    @Override
    public String createTokenValue(Map<String, String> credentials) throws FogbowException, UnexpectedException {
        if ((credentials == null) || (credentials.get(CloudStackConstants.Identity.USERNAME_KEY_JSON) == null) ||
                (credentials.get(CloudStackConstants.Identity.PASSWORD_KEY_JSON) == null) ||
                credentials.get(CloudStackConstants.Identity.DOMAIN_KEY_JSON) == null) {
            throw new InvalidParameterException(Messages.Exception.NO_USER_CREDENTIALS);
        }

        LoginRequest request = createLoginRequest(credentials);

        // NOTE(pauloewerton): since all cloudstack requests params are passed via url args, we do not need to
        // send a valid json body in the post request
        HttpResponse response = this.client.doGenericRequest(HttpMethod.POST,
                request.getUriBuilder().toString(), new HashMap<>(), new HashMap<>());

        if (response.getHttpCode() > HttpStatus.SC_OK) {
            HttpResponseException exception = new HttpResponseException(response.getHttpCode(), response.getContent());
            HttpToFogbowAsExceptionMapper.map(exception);
            return null;
        } else {
            LoginResponse loginResponse = LoginResponse.fromJson(response.getContent());
            String tokenValue = getTokenValue(loginResponse.getSessionKey());
            return tokenValue;
        }
    }

    private LoginRequest createLoginRequest(Map<String, String> credentials) throws InvalidParameterException {
        String userId = credentials.get(CloudStackConstants.Identity.USERNAME_KEY_JSON);
        String password = credentials.get(CloudStackConstants.Identity.PASSWORD_KEY_JSON);
        String domain = credentials.get(CloudStackConstants.Identity.DOMAIN_KEY_JSON);

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

        HttpResponse response = this.client.doGenericRequest(HttpMethod.GET,
                request.getUriBuilder().toString(), new HashMap<>(), new HashMap<>());

        if (response.getHttpCode() > HttpStatus.SC_OK) {
            HttpResponseException exception = new HttpResponseException(response.getHttpCode(), response.getContent());
            HttpToFogbowAsExceptionMapper.map(exception);
            return null;
        } else {
            try {
                ListAccountsResponse listAccountsResponse = ListAccountsResponse.fromJson(response.getContent());
                // NOTE(pauloewerton): considering only one account/user per request
                ListAccountsResponse.User user = listAccountsResponse.getAccounts().get(0).getUsers().get(0);

                // NOTE(pauloewerton): keeping the token-value separator as expected by the other cloudstack plugins
                String tokenValue = user.getApiKey() + CloudStackConstants.KEY_VALUE_SEPARATOR + user.getSecretKey();
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
    }

    // Used for testing
    public void setClient(HttpRequestClientUtil client) {
        this.client = client;
    }
}
