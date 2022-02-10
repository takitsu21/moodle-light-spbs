package moodle.Ressource.Cours;

import moodle.Ressource.Ressource;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cours")
public class Cours extends Ressource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "text_cours",
            joinColumns = @JoinColumn(name = "cours_id"),
            inverseJoinColumns = @JoinColumn(name = "text_id"))
    private List<Text> texts;

    public Cours(String name, String description) {
        super(name, description);
    }

    public Cours() {

    }
}
