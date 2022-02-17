package fr.uca.springbootstrap;

import fr.uca.api.models.Module;
import fr.uca.api.models.Questionnaire;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.QuestionnaireRepository;
import io.cucumber.java.fr.Etantdonné;
import org.springframework.beans.factory.annotation.Autowired;

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
            questionnaire = new Questionnaire(arg0, "Questionnaire", 1);
        }
        questionnaireRepository.save(questionnaire);

        module.addRessource(questionnaire);
        moduleRepository.save(module);
    }
}
