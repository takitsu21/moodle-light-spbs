package fr.uca.springbootstrap.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@DiscriminatorValue("qcm")
public class QCM extends Question {

    @ManyToMany
    @JoinTable( name = "possible_answers",
            joinColumns = @JoinColumn(name ="open_question"),
            inverseJoinColumns = @JoinColumn(name="answers"))
    private Set<Answer> possibleAnswers;

    @OneToOne
    private Answer answer;

}
