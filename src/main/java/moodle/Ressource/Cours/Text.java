package moodle.Ressource.Cours;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(	name = "text")
public class Text {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private int number;

    @NotBlank
    private String content;

    public Text(int number, String content) {
        this.number = number;
        this.content = content;
    }

    public Text() {

    }
}
