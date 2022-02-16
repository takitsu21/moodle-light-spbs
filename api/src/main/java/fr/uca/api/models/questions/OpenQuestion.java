package fr.uca.api.models.questions;


import fr.uca.api.models.UserRef;

import javax.persistence.*;
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
    private Set<AnswerOpenQuestion> answerOpenQuestionSet;


    public OpenQuestion(Set<Answer> answers, Set<AnswerOpenQuestion> answerOpenQuestionSet,
                        Set<Answer> possibleAnswers, String name, String description,
                        int number){
        super(number, name, description);
        this.answers = answers;
        this.answerOpenQuestionSet = answerOpenQuestionSet;
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

    public Set<AnswerOpenQuestion> getAnswerOpenQuestionSet() { return answerOpenQuestionSet; }
    public void setAnswerOpenQuestionSet(Set<AnswerOpenQuestion> answerOpenQuestionSet) { this.answerOpenQuestionSet = answerOpenQuestionSet; }
    public void addAnswerOpenQuestion(AnswerOpenQuestion answerOpenQuestion){ answerOpenQuestionSet.add(answerOpenQuestion);}
    public void removeAnswerOpenQuestion(AnswerOpenQuestion answerOpenQuestion){ answerOpenQuestionSet.remove(answerOpenQuestion);}

    public AnswerOpenQuestion getStudentAnswerByStudent(UserRef student){
        for (AnswerOpenQuestion openAnswer : this.answerOpenQuestionSet){
            if (openAnswer.getStudent().equals(student)){
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
            if (answer.getAnswer() == content){
                return answer;
            }
        }
        return null;
    }
}
