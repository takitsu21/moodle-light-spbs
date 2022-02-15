package fr.uca.springbootstrap.models;

import javax.persistence.*;
import fr.uca.auth.models.User;

@Entity
@Table(name = "grades_questionnaire")
public class GradesQuestionnaire {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinTable(name = "students_grades",
            joinColumns = @JoinColumn(name = "grades_id"),
            inverseJoinColumns = @JoinColumn(name = "questionnaire_id"))
    private Questionnaire questionnaire;

    @OneToOne
    private User student;

    private Integer note;

    public GradesQuestionnaire(Questionnaire questionnaire, Integer note, User student) {
        this.questionnaire = questionnaire;
        this.note = note;
        this.student = student;
    }

    public GradesQuestionnaire() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }
}
