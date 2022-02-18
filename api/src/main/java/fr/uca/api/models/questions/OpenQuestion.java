package fr.uca.api.models.questions;


import fr.uca.api.models.UserRef;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@DiscriminatorValue("open")
public class OpenQuestion extends Question {

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "possible_answers",
            joinColumns = @JoinColumn(name ="open_question"),
            inverseJoinColumns = @JoinColumn(name="answers"))
    private Set<Answer> possibleAnswers;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "answers",
            joinColumns = @JoinColumn(name ="open_question"),
            inverseJoinColumns = @JoinColumn(name="answers"))
    private Set<Answer> answers;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "students_anwser",
            joinColumns = @JoinColumn(name = "open_question"),
            inverseJoinColumns = @JoinColumn(name = "student_anwser_open_question"))
    private Set<AnswerOpenQuestion> studentAnswers;


    public OpenQuestion(Set<Answer> answers, Set<AnswerOpenQuestion> studentAnswers,
                        Set<Answer> possibleAnswers, String name, String description,
                        int number){
        super(number, name, description);
        this.answers = answers;
        this.studentAnswers = studentAnswers;
        this.possibleAnswers = possibleAnswers;
    }


    public OpenQuestion() {
        super();
    }

    public OpenQuestion(int num, String name, String description) {
        super(num, name, description);
    }

    public Set<Answer> getAnswers() { return answers; }
    public void setAnswers(Set<Answer> answers) { this.answers = answers; }
    public void addAnswer(Answer answer){ answers.add(answer);}
    public void removeAnswer(Answer answer){ answers.remove(answer);}

    public Set<Answer> getPossibleAnswers() { return possibleAnswers; }
    public void setPossibleAnswers(Set<Answer> possibleAnswers) { this.possibleAnswers = possibleAnswers; }
    public void addPossibleAnswer(Answer answer){ possibleAnswers.add(answer);}
    public void removePossibleAnswer(Answer answer){ possibleAnswers.remove(answer);}

    public Set<AnswerOpenQuestion> getStudentAnswers() { return studentAnswers; }
    public void setStudentAnswers(Set<AnswerOpenQuestion> answerOpenQuestionSet) { this.studentAnswers = answerOpenQuestionSet; }
    public void addAnswerOpenQuestion(AnswerOpenQuestion answerOpenQuestion){ studentAnswers.add(answerOpenQuestion);}
    public void removeAnswerOpenQuestion(AnswerOpenQuestion answerOpenQuestion){ studentAnswers.remove(answerOpenQuestion);}

    public AnswerOpenQuestion getStudentAnswerOf(UserRef user) {
        for(AnswerOpenQuestion answerOpenQuestion: studentAnswers){
            if(answerOpenQuestion.getStudent().equals(user)){
                return answerOpenQuestion;
            }
        }
        return null;
    }

    public AnswerOpenQuestion getStudentAnswerOf(String user) {
        for(AnswerOpenQuestion answerOpenQuestion: studentAnswers){
            if(answerOpenQuestion.getStudent().getUsername().equals(user)){
                return answerOpenQuestion;
            }
        }
        return null;
    }

    public boolean studentAnswerContains(UserRef user) {
        for(AnswerOpenQuestion answerOpenQuestion: studentAnswers){
            if(answerOpenQuestion.getStudent().equals(user)){
                return true;
            }
        }
        return false;
    }

    public boolean studentAnswerContainsAnswer(UserRef user, String s) {
        for(AnswerOpenQuestion answerOpenQuestion: studentAnswers){
            if(answerOpenQuestion.getStudent().equals(user) && answerOpenQuestion.answerContains(s)){
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

    public boolean answerContains(String s) {
        for(Answer answer: answers){
            if(Objects.equals(answer.getAnswer(), s)){
                return true;
            }
        }
        return false;
    }
}
