package fr.uca.springbootstrap.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name="ressource")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="ressource_type")
public abstract class Ressource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Integer num;

    @NotBlank
    private String name;

    @NotBlank
    private String description;



//    @OneToMany(fetch = FetchType.EAGER)
//    @JoinTable(	name = "cours_ressources",
//            joinColumns = @JoinColumn(name = "cours_id"),
//            inverseJoinColumns = @JoinColumn(name = "ressource_id"))
//    private Set<Cours> cours;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "ressources_modules",
            joinColumns = @JoinColumn(name = "module_id"),
            inverseJoinColumns = @JoinColumn(name = "ressource_id"))
    private Set<Module> modules;

    public Ressource() {
    }

    public Ressource(String name, String description, Integer num) {
        this.name = name;
        this.description = description;
        this.num = num;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Module> getModules() {
        return modules;
    }

    public void setModules(Set<Module> modules) {
        this.modules = modules;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
