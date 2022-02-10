package fr.uca.springbootstrap.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="ressource")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, length = 20)
@DiscriminatorValue("ressource")

public abstract class Ressource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private Integer num;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private boolean visibility=false;

//    @OneToMany(fetch = FetchType.EAGER)
//    @JoinTable(	name = "cours_ressources",
//            joinColumns = @JoinColumn(name = "cours_id"),
//            inverseJoinColumns = @JoinColumn(name = "ressource_id"))
//    private Set<Cours> cours;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(	name = "ressources_modules",
//            joinColumns = @JoinColumn(name = "module_id"),
//            inverseJoinColumns = @JoinColumn(name = "ressource_id"))
//    private Set<Module> modules;

    public Ressource() {
        this.num = 0;
 //       this.modules = new HashSet<>();
    }

    public Ressource(String name, String description, Integer num) {
        this.name = name;
        this.description = description;
        this.num = num;
 //       this.modules = new HashSet<>();
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

//    public Set<Module> getModules() {
 //       return modules;
  //  }

  //  public void setModules(Set<Module> modules) {
   //     this.modules = modules;
  //  }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
}
