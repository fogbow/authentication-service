package cloud.fogbow.as.api.http.response;

import cloud.fogbow.as.constants.ApiDocumentation;
import io.swagger.annotations.ApiModelProperty;

public class Token {
    @ApiModelProperty(example = ApiDocumentation.Model.TOKEN)
    private String token;

    public Token() {}

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
