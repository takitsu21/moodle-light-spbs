package Ressource.Exercices;

public class OpenQuestion extends Question {
    String[] possibleAnswers;
    String[] answers;

    public OpenQuestion(int number, String name, String description, String[] possibleAnswers, String[] answers) {
        super(number, name, description);
        this.possibleAnswers = possibleAnswers;
        this.answers = answers;
    }
}
