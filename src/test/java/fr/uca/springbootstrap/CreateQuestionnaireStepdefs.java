package fr.uca.springbootstrap;

import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.Questionnaire;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.QuestionnaireRepository;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = SpringBootSecurityPostgresqlApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CreateQuestionnaireStepdefs {

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Etantdonné("le questionnaire {string} dans le module {string}")
    public void leQuestionnaireDansLeModule(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1)
                .orElse(new Module(arg1));

        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg0);
        if (questionnaire == null){
            questionnaire = new Questionnaire(arg0, "Questionnaire", 8);
        }
        questionnaireRepository.save(questionnaire);

        module.addRessource(questionnaire);
        moduleRepository.save(module);
    }
}
