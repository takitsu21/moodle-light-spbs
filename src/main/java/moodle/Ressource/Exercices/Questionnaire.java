package moodle.Ressource.Exercices;

import moodle.Ressource.Ressource;
import moodle.users.Student;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;

@Entity
@Table(name = "questionnaires")
public class Questionnaire extends Ressource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "question_questionnaires",
            joinColumns = @JoinColumn(name = "questionnaire_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id"))
    private List<Question> questionnaire;

    private HashMap<Student, Integer> grades;

    public Questionnaire(String name, String description, List<Question> questionnaire, List<Student> students) {
        super(name, description);
        this.questionnaire = questionnaire;
        this.grades = new HashMap<>();
        for (Student student : students.toArray(new Student[0])) {
            grades.put(student, null);
        }
    }

    public Questionnaire() {

    }

    public List<Question> getQuestionnaire() {
        return questionnaire;
    }

    public void addQuestion(Question question) {
        questionnaire.add(question);
    }

    public void addStudent(Student student) {
        grades.put(student, null);
    }

    public HashMap<Student, Integer> getGrades() {
        return grades;
    }

    public void validateQuestionnaire(Student student) {
        int nbQuestion = questionnaire.size();
        int result = 0;
        for (Question question : questionnaire) {
            if (question.succeedQuestion(student)) {
                result += 1;
            }
        }
        grades.put(student, result / nbQuestion);
    }

    public boolean isValidateBy(Student student) {
        return grades.get(student) != null;
    }

    public int getGrade(Student student) {
        return grades.get(student);
    }
}
