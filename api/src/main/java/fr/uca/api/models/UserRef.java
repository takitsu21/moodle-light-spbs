package fr.uca.api.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class UserRef {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Integer userId;

    @NotBlank
    @Size(max = 20)
    private String username;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles_courses",
            joinColumns = @JoinColumn(name = "user_ref_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_courses_id", referencedColumnName = "id"))
    private Set<RoleCourses> roleCourses = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_modules",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id"))
    private Set<Module> modules = new HashSet<>();

    public UserRef() {
    }

    public UserRef(Integer userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer id) {
        this.userId = id;
    }

    public Set<RoleCourses> getRoles() {
        return roleCourses;
    }

    public void setRoles(Set<RoleCourses> roleCourses) {
        this.roleCourses = roleCourses;
    }

    public Set<Module> getModules() {
        return modules;
    }

    public void setModules(Set<Module> modules) {
        this.modules = modules;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<RoleCourses> getRoleCourses() {
        return roleCourses;
    }

    public void setRoleCourses(Set<RoleCourses> roleCourses) {
        this.roleCourses = roleCourses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRef user = (UserRef) o;
        return userId.equals(user.getUserId()) && username.equals(user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username);
    }

    public boolean isTeacher() {
        for (RoleCourses roleCourses : this.roleCourses) {
            if (roleCourses.getName().equals(ERole.ROLE_TEACHER)) {
                return true;
            }
        }
        return false;
    }
}
