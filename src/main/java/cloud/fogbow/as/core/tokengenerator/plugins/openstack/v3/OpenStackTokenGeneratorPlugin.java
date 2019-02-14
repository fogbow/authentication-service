package cloud.fogbow.as.core.tokengenerator.plugins.openstack.v3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cloud.fogbow.common.constants.OpenStackConstants;
import cloud.fogbow.common.util.GsonHolder;
import cloud.fogbow.common.util.connectivity.GenericRequestHttpResponse;
import cloud.fogbow.common.util.connectivity.HttpRequestClientUtil;
import cloud.fogbow.as.core.PropertiesHolder;
import org.apache.log4j.Logger;
import cloud.fogbow.common.constants.FogbowConstants;
import cloud.fogbow.common.exceptions.FatalErrorException;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.as.constants.ConfigurationPropertyKeys;
import cloud.fogbow.as.constants.ConfigurationPropertyDefaults;
import cloud.fogbow.as.constants.Messages;
import cloud.fogbow.as.core.tokengenerator.TokenGeneratorPlugin;
import cloud.fogbow.as.core.tokengenerator.plugins.AttributeJoiner;

public class OpenStackTokenGeneratorPlugin implements TokenGeneratorPlugin {
    private static final Logger LOGGER = Logger.getLogger(OpenStackTokenGeneratorPlugin.class);

    private HttpRequestClientUtil client;
    private String v3TokensEndpoint;
    private String tokenProviderId;

    public OpenStackTokenGeneratorPlugin() throws FatalErrorException {
        this.tokenProviderId = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.LOCAL_MEMBER_ID_KEY);

        String identityUrl = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.OPENSTACK_KEYSTONE_V3_URL_KEY);
        if (isUrlValid(identityUrl)) {
            this.v3TokensEndpoint = identityUrl + OpenStackConstants.Identity.V3_TOKENS_ENDPOINT_PATH;
        }
        String timeoutRequestStr = PropertiesHolder.getInstance().getProperty(
                ConfigurationPropertyKeys.HTTP_REQUEST_TIMEOUT_KEY, ConfigurationPropertyDefaults.HTTP_REQUEST_TIMEOUT);
        Integer timeoutHttpRequest = Integer.parseInt(timeoutRequestStr);
        this.client = new HttpRequestClientUtil();
    }

    public OpenStackTokenGeneratorPlugin(HttpRequestClientUtil client, String v3TokensEndpoint, String tokenProviderId) {
        this.client = client;
        this.v3TokensEndpoint = v3TokensEndpoint;
        this.tokenProviderId = tokenProviderId;
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

        HashMap<String, String> body = GsonHolder.getInstance().fromJson(jsonBody, HashMap.class);
        GenericRequestHttpResponse response = this.client.doGenericRequest("POST", this.v3TokensEndpoint, new HashMap<>(), body, null);

        String tokenString = getTokenFromJson(response);
        return tokenString;
    }

    private String getTokenFromJson(GenericRequestHttpResponse response) throws UnexpectedException {
        String tokenValue = null;
        Map<String, List<String>> headers = response.getHeaders();
        if (headers.get(OpenStackConstants.X_SUBJECT_TOKEN) != null) {
            List<String> headerValues = headers.get(OpenStackConstants.X_SUBJECT_TOKEN);
            if (!headerValues.isEmpty()) {
                tokenValue = headerValues.get(0);
            } else {
                tokenValue = null;
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
            attributes.put(OpenStackConstants.Identity.PROJECT_KEY_JSON, projectId);
            attributes.put(FogbowConstants.TOKEN_VALUE_KEY, tokenValue);
            return AttributeJoiner.join(attributes);
        } catch (Exception e) {
            LOGGER.error(Messages.Error.UNABLE_TO_GET_TOKEN_FROM_JSON, e);
            throw new UnexpectedException(Messages.Error.UNABLE_TO_GET_TOKEN_FROM_JSON, e);
        }
    }

    private String mountJsonBody(Map<String, String> credentials) {
        String projectName = credentials.get(OpenStackConstants.Identity.PROJECT_NAME_KEY_JSON);
        String password = credentials.get(OpenStackConstants.Identity.PASSWORD_KEY_JSON);
        String domain = credentials.get(OpenStackConstants.Identity.DOMAIN_KEY_JSON);
        String userName = credentials.get(OpenStackConstants.Identity.USER_NAME_KEY_JSON);

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
