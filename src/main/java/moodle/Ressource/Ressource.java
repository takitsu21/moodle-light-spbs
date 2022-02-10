package moodle.Ressource;

import moodle.Ressource.Cours.Cours;
import moodle.Ressource.Exercices.Questionnaire;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "ressources")
public class Ressource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "cours_ressources",
            joinColumns = @JoinColumn(name = "ressource_id"),
            inverseJoinColumns = @JoinColumn(name = "cours_id"))
    private List<Cours> cours;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "questionnaire_ressources",
            joinColumns = @JoinColumn(name = "ressource_id"),
            inverseJoinColumns = @JoinColumn(name = "questionnaire_id"))
    private List<Questionnaire> questionnaires;

    @NotBlank
    private boolean visibility = false;

    public Ressource(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Ressource() {

    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
}
