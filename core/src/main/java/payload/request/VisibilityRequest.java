package payload.request;

import javax.validation.constraints.NotNull;

public class VisibilityRequest {
    @NotNull
    private Boolean visibility;

    public VisibilityRequest(Boolean visibility) {
        this.visibility = visibility;
    }

    public VisibilityRequest() {
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }
}
