package moodle.users;


import moodle.Ressource.Exercices.OpenQuestion;
import moodle.Ressource.Exercices.QCM;
import moodle.Ressource.Exercices.Questionnaire;

public class Student extends User {

    public Student(String name, String password) {
        super(Role.STUDENT, name, password);
    }

    public void validate_questionnaire(Questionnaire questionnaire) {
        questionnaire.validateQuestionnaire(this);
    }

    public void setAnswerQCM(QCM question, int answer) {
        question.setStudentAnswer(this, answer);
    }

    public void removeAnswerOpenQuestion(OpenQuestion question, int answer) {
        question.removeStudentAnswer(this, answer);
    }

    public void addAnswerOpenQuestion(OpenQuestion question, int answer) {
        question.addStudentAnswer(this, answer);
    }

    public void seeResultQuestionnaire(Questionnaire questionnaire) {
        questionnaire.getGrade(this);
    }

}
