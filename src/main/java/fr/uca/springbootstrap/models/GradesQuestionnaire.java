package fr.uca.springbootstrap.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;


@Entity
public class GradesQuestionnaire {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    private Questionnaire questionnaire;

    @OneToOne
    private User student;

    private int note;

}
