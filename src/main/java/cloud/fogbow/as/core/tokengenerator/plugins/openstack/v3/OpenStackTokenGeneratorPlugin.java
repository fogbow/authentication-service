package cloud.fogbow.as.core.tokengenerator.plugins.openstack.v3;

import java.util.HashMap;
import java.util.Map;

import cloud.fogbow.common.util.connectivity.HttpRequestClientUtil;
import cloud.fogbow.as.core.PropertiesHolder;
import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;
import org.apache.log4j.Logger;
import cloud.fogbow.common.constants.FogbowConstants;
import cloud.fogbow.common.exceptions.FatalErrorException;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.as.core.constants.ConfigurationConstants;
import cloud.fogbow.as.core.constants.DefaultConfigurationConstants;
import cloud.fogbow.as.core.constants.Messages;
import cloud.fogbow.as.core.tokengenerator.TokenGeneratorPlugin;
import cloud.fogbow.as.core.tokengenerator.plugins.AttributeJoiner;
import cloud.fogbow.as.core.util.HttpToFogbowAsExceptionMapper;

public class OpenStackTokenGeneratorPlugin implements TokenGeneratorPlugin {
    private static final Logger LOGGER = Logger.getLogger(OpenStackTokenGeneratorPlugin.class);

    public static final String V3_TOKENS_ENDPOINT_PATH = "/auth/tokens";
    public static final String X_SUBJECT_TOKEN = "X-Subject-Token";
    public static final String PROJECT_NAME = "projectname";
    public static final String PASSWORD = "password";
    public static final String USER_NAME = "username";
    public static final Object DOMAIN = "domain";
    private static final String PROJECT_ID_KEY = "project";

    private String v3TokensEndpoint;
    private HttpRequestClientUtil client;
    private String tokenProviderId;

    public OpenStackTokenGeneratorPlugin() throws FatalErrorException {
        this.tokenProviderId = PropertiesHolder.getInstance().getProperty(ConfigurationConstants.LOCAL_MEMBER_ID);


        String identityUrl = PropertiesHolder.getInstance().getProperty(ConfigurationConstants.OPENSTACK_KEYSTONE_V3_ENDPOINT);
        if (isUrlValid(identityUrl)) {
            this.v3TokensEndpoint = identityUrl + V3_TOKENS_ENDPOINT_PATH;
        }
        String timeoutRequestStr = PropertiesHolder.getInstance().getProperty(
                ConfigurationConstants.HTTP_REQUEST_TIMEOUT, DefaultConfigurationConstants.HTTP_REQUEST_TIMEOUT);
        Integer timeoutHttpRequest = Integer.parseInt(timeoutRequestStr);
        this.client = new HttpRequestClientUtil(timeoutHttpRequest);
    }

    private boolean isUrlValid(String url) throws FatalErrorException {
        if (url == null || url.trim().isEmpty()) {
            throw new FatalErrorException(String.format(Messages.Fatal.INVALID_SERVICE_URL, (url == null ? "null" : "")));
        }
        return true;
    }

    @Override
    public String createTokenValue(Map<String, String> credentials) throws FogbowException,
            UnexpectedException {

        String jsonBody = mountJsonBody(credentials);

        HttpRequestClientUtil.Response response = null;
        try {
            response = this.client.doPostRequest(this.v3TokensEndpoint, jsonBody);
        } catch (HttpResponseException e) {
            HttpToFogbowAsExceptionMapper.map(e);
        }
        String tokenString = getTokenFromJson(response);
        return tokenString;
    }

    private String getTokenFromJson(HttpRequestClientUtil.Response response) throws UnexpectedException {

        String tokenValue = null;
        Header[] headers = response.getHeaders();
        for (Header header : headers) {
            if (header.getName().equals(X_SUBJECT_TOKEN)) {
                tokenValue = header.getValue();
            }
        }

        try {
            CreateTokenResponse createTokenResponse = CreateTokenResponse.fromJson(response.getContent());
            CreateTokenResponse.User userTokenResponse = createTokenResponse.getUser();
            String userId = userTokenResponse.getId();
            String userName = userTokenResponse.getName();

            CreateTokenResponse.Project projectTokenResponse = createTokenResponse.getProject();
            String projectId = projectTokenResponse.getId();

            Map<String, String> attributes = new HashMap<>();
            attributes.put(FogbowConstants.PROVIDER_ID_KEY, this.tokenProviderId);
            attributes.put(FogbowConstants.USER_ID_KEY, userId);
            attributes.put(FogbowConstants.USER_NAME_KEY, userName);
            attributes.put(PROJECT_ID_KEY, projectId);
            attributes.put(FogbowConstants.TOKEN_VALUE_KEY, tokenValue);
            return AttributeJoiner.join(attributes);
        } catch (Exception e) {
            LOGGER.error(Messages.Error.UNABLE_TO_GET_TOKEN_FROM_JSON, e);
            throw new UnexpectedException(Messages.Error.UNABLE_TO_GET_TOKEN_FROM_JSON, e);
        }
    }

    private String mountJsonBody(Map<String, String> credentials) {
        String projectName = credentials.get(PROJECT_NAME);
        String password = credentials.get(PASSWORD);
        String domain = credentials.get(DOMAIN);
        String userName = credentials.get(USER_NAME);

        CreateTokenRequest createTokenRequest = new CreateTokenRequest.Builder()
                .projectName(projectName)
                .domain(domain)
                .userName(userName)
                .password(password)
                .build();

        return createTokenRequest.toJson();
    }

    // Used in testing
    public void setClient(HttpRequestClientUtil client) {
        this.client = client;
    }
}
