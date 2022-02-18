package payload.request;

import javax.validation.constraints.NotBlank;
import java.util.Set;

public class RoleRequest {
    @NotBlank
    private String username;

    private Set<String> role;

    private String roleName;

    public RoleRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getRole() {
        return role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
