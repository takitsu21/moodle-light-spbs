package Ressource.Exercices;

public class QCM extends Question {
    String[] possibleAnswers;
    String answer;

    public QCM(int number, String name, String description, String[] possibleAnswers, String answer) {
        super(number, name, description);
        this.possibleAnswers = possibleAnswers;
        this.answer = answer;
    }
}
