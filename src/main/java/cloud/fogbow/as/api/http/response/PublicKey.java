package cloud.fogbow.as.api.http.response;

import io.swagger.annotations.ApiModelProperty;

public class PublicKey {
    @ApiModelProperty(position = 0, example = cloud.fogbow.common.constants.ApiDocumentation.Model.PUBLIC_KEY)
    private String publicKey;

    public PublicKey() {}

    public PublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
