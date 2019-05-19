package cloud.fogbow.as.api.http.response;

import io.swagger.annotations.ApiModelProperty;

public class Version {
    @ApiModelProperty(example = "v.1.0.0-as-c803775-common-4e0d74e")
    private String version;

    public Version() {}

    public Version(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
