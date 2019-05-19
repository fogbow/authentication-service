package cloud.fogbow.as.api.parameters;

import cloud.fogbow.as.constants.ApiDocumentation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

@ApiModel
public class TokenParameters {
    @ApiModelProperty(position = 0, required = true, example = ApiDocumentation.Model.CREDENTIALS)
    private Map<String, String> credentials;
    @ApiModelProperty(position = 1, required = true, example = cloud.fogbow.common.constants.ApiDocumentation.Model.PUBLIC_KEY)
    private String publicKey;

    public Map<String, String> getCredentials() {
        return credentials;
    }

    public String getPublicKey() {
        return publicKey;
    }
}
