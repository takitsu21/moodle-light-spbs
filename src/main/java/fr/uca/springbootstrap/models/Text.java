package fr.uca.springbootstrap.models;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(	name = "text")
public class Text {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Integer num;

    @NotBlank
    private String content;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(	name = "cours_text",
//            joinColumns = @JoinColumn(name = "cours_id"),
//            inverseJoinColumns = @JoinColumn(name = "text_id"))
//    private Set<Cours> cours;

    public Text() {
    }

    public Text(Integer num, String content) {
        this.num = num;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

//    public Set<Cours> getCours() {
//        return cours;
//    }
//
//    public void setCours(Set<Cours> cours) {
//        this.cours = cours;
//    }
}
