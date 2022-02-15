package fr.uca.api.models.questions;

import fr.uca.api.models.UserRef;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@DiscriminatorValue("qcm")
public class QCM extends Question {

//    @ManyToMany
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "possible_answers",
            joinColumns = @JoinColumn(name = "open_question"),
            inverseJoinColumns = @JoinColumn(name = "answers"))
    private Set<Answer> possibleAnswers=new HashSet<>();


    @OneToOne
    private Answer answer;

//    @ManyToMany
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "student_answer_qcm",
            joinColumns = @JoinColumn(name = "qcm"),
            inverseJoinColumns = @JoinColumn(name = "student_answer"))
    private Set<AnswerQCM> studentsAnswers=new HashSet<>();

    public QCM(int number, String name, String description) {
        super(number, name, description);
    }

    public QCM() {
        super();
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Set<AnswerQCM> getStudentsAnswers() { return studentsAnswers; }

    public AnswerQCM getStudentAnswerOf(UserRef user) {
        for(AnswerQCM answerQCM: studentsAnswers){
            if(answerQCM.getStudent().equals(user)){
                return answerQCM;
            }
        }
        return null;
    }

    public AnswerQCM getStudentAnswerOf(String user) {
        for(AnswerQCM answerQCM: studentsAnswers){
            if(answerQCM.getStudent().getUsername().equals(user)){
                return answerQCM;
            }
        }
        return null;
    }

    public boolean StudentAnswerContains(UserRef user) {
        for(AnswerQCM answerQCM: studentsAnswers){
            if(answerQCM.getStudent().equals(user)){
                return true;
            }
        }
        return false;
    }

    public boolean possibleAnswerContains(String s) {
        for(Answer answer: possibleAnswers){
            if(Objects.equals(answer.getAnswer(), s)){
                return true;
            }
        }
        return false;
    }



    public void setStudentsAnswers(Set<AnswerQCM> studentsAnswers) {
        this.studentsAnswers = studentsAnswers;
    }

    public Set<Answer> getPossibleAnswers() {
        return possibleAnswers;
    }

    public void setPossibleAnswers(Set<Answer> possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
    }
}
