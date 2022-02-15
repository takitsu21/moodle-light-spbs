package fr.uca.springbootstrap.models.questions;

import fr.uca.springbootstrap.models.User;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@DiscriminatorValue("open")
public class OpenQuestion extends Question {

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable( name = "possible_answers_open",
            joinColumns = @JoinColumn(name ="open_question"),
            inverseJoinColumns = @JoinColumn(name="answers"))
    private Set<Answer> possibleAnswers;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable( name = "answers_to_open",
            joinColumns = @JoinColumn(name ="open_question"),
            inverseJoinColumns = @JoinColumn(name="answers_open"))
    private Set<Answer> answers;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "students_anwser_open",
            joinColumns = @JoinColumn(name = "open_question"),
            inverseJoinColumns = @JoinColumn(name = "student_anwser_open_question"))
    private Set<AnswerOpenQuestion> studentsAnswers;


    public OpenQuestion(Set<Answer> answers, Set<AnswerOpenQuestion> studentAnswers,
                        Set<Answer> possibleAnswers, String name, String description,
                        int number){
        super(number, name, description);
        this.answers = answers;
        this.studentsAnswers = studentAnswers;
        this.possibleAnswers = possibleAnswers;
    }


    public OpenQuestion() {
        super();
    }

    public Set<Answer> getAnswers() { return answers; }
    public void setAnswers(Set<Answer> answers) { this.answers = answers; }
    public void addAnswer(Answer answer){ answers.add(answer);}
    public void removeAnswer(Answer answer){ answers.remove(answer);}

    public Set<Answer> getPossibleAnswers() { return possibleAnswers; }
    public void setPossibleAnswers(Set<Answer> possibleAnswers) { this.possibleAnswers = possibleAnswers; }
    public void addPossibleAnswer(Answer answer){ possibleAnswers.add(answer);}
    public void removePossibleAnswer(Answer answer){ possibleAnswers.remove(answer);}

    public Set<AnswerOpenQuestion> getStudentsAnswers() { return studentsAnswers; }
    public void setAnswerStudentAnswerSet(Set<AnswerOpenQuestion> answerOpenQuestionSet) {
        this.studentsAnswers = answerOpenQuestionSet;
        }
    public void addStudentAnswer(AnswerOpenQuestion answerOpenQuestion){
        studentsAnswers.add(answerOpenQuestion);
    }
    public void removeStudentAnswer(AnswerOpenQuestion answerOpenQuestion){
        studentsAnswers.remove(answerOpenQuestion);
    }

    public AnswerOpenQuestion getStudentAnswerByStudent(User student){
        for (AnswerOpenQuestion openAnswer : this.studentsAnswers){
            if (openAnswer.getStudent() == student){
                return openAnswer;
            }
        }
        return null;
    }

    public Answer getPossibleAnswerById(long id){
        for (Answer answer : possibleAnswers){
            if (answer.getId() == id){
                return answer;
            }
        }
        return null;
    }

    public Answer getPossibleAnswerByContent(String content){
        for (Answer answer : possibleAnswers){
            if (Objects.equals(answer.getAnswer(), content)){
                return answer;
            }
        }
        return null;
    }

    public Answer getAnswerByContent(String content){
        for (Answer answer : answers){
            if (Objects.equals(answer.getAnswer(), content)){
                return answer;
            }
        }
        return null;
    }

}
