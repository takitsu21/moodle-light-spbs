package fr.uca.springbootstrap.models.questions;

import fr.uca.springbootstrap.models.User;
import fr.uca.springbootstrap.models.questions.Answer;
import fr.uca.springbootstrap.models.questions.OpenQuestion;

import javax.persistence.*;
import java.util.Set;

@Entity
public class StudentAnswerOpenQuestion {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    private Set<Answer> answers;

    @OneToOne
    @JoinColumn(name = "student")
    private User student;

    @OneToOne
    @JoinColumn(name = "open_question")
    private OpenQuestion openQuestion;


    public void setId(Long id) { this.id = id; }
    public Long getId() { return id; }

    public OpenQuestion getOpenQuestion() { return openQuestion; }
    public void setOpenQuestion(OpenQuestion openQuestion) { this.openQuestion = openQuestion; }

    public void setAnswers(Set<Answer> answers) { this.answers = answers; }
    public Set<Answer> getAnswers() { return answers; }

    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }
}
