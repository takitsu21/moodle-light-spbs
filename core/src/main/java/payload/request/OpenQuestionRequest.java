package payload.request;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class OpenQuestionRequest {

    @NotNull
    private int number;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

//    private Set<Answer> possibleAnswers;
//    private Set<Answer> answers;
//    private Set<AnswerOpenQuestion> answerOpenQuestionSet;

    public OpenQuestionRequest(){
    }

    public OpenQuestionRequest(String name, String description, int number){
        this.description = description;
        this.name = name;
        this.number = number;
    }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

//    public Set<Answer> getAnswers() { return answers; }
//    public void setAnswers(Set<Answer> answers) { this.answers = answers; }
//    public void addAnswer(Answer answer){ answers.add(answer);}
//    public void removeAnswer(Answer answer){ answers.remove(answer);}
//
//    public Set<Answer> getPossibleAnswers() { return possibleAnswers; }
//    public void setPossibleAnswers(Set<Answer> possibleAnswers) { this.possibleAnswers = possibleAnswers; }
//    public void addPossibleAnswer(Answer answer){ possibleAnswers.add(answer);}
//    public void removePossibleAnswer(Answer answer){ possibleAnswers.remove(answer);}
//
//    public Set<AnswerOpenQuestion> getAnswerOpenQuestionSet() { return answerOpenQuestionSet; }
//    public void setAnswerOpenQuestionSet(Set<AnswerOpenQuestion> answerOpenQuestionSet) { this.answerOpenQuestionSet = answerOpenQuestionSet; }
//    public void addAnswerOpenQuestion(AnswerOpenQuestion answerOpenQuestion){ answerOpenQuestionSet.add(answerOpenQuestion);}
//    public void removeAnswerOpenQuestion(AnswerOpenQuestion answerOpenQuestion){ answerOpenQuestionSet.remove(answerOpenQuestion);}
//
//    public AnswerOpenQuestion getStudentAnswerByAnswer(User student){
//        for (AnswerOpenQuestion openAnswer : this.answerOpenQuestionSet){
//            if (openAnswer.getStudent() == student){
//                return openAnswer;
//            }
//        }
//        return null;
//    }

}
