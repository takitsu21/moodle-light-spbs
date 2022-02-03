package moodle.Ressource.Exercices;

public class OpenQuestion extends Question {
    private String[] possibleAnswers;
    private String[] answers;

    public OpenQuestion(int number, String name, String description, String[] possibleAnswers, String[] answers) {
        super(number, name, description);
        this.possibleAnswers = possibleAnswers;
        this.answers = answers;
    }
}
