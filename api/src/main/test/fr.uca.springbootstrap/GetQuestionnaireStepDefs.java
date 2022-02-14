package fr.uca.springbootstrap;

import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;

public class GetQuestionnaireStepDefs {
    //TODO
    @Etantdonné("le professeur {string} assigné au module d'{string} aaq")
    public void leProfesseurAssignéAuModuleDAaq(String arg0, String arg1) {

    }

    @Et("le module {string} a un questionnaire {string} invisible aaq")
    public void leModuleAUnQuestionnaireInvisibleAaq(String arg0, String arg1) {

    }

    @Et("le module {string} a un questionnaire {string} visible aaq")
    public void leModuleAUnQuestionnaireVisibleAaq(String arg0, String arg1) {

    }

    @Et("le professeur {string} qui n'a aucun module aaq")
    public void leProfesseurQuiNAAucunModuleAaq(String arg0) {

    }

    @Et("l'élève {string} est assigné au cours {string} aaq")
    public void lÉlèveEstAssignéAuCoursAaq(String arg0, String arg1) {

    }

    @Et("l'élève {string} assigné a aucun module aaq")
    public void lÉlèveAssignéAAucunModuleAaq(String arg0) {

    }

    @Quand("le professeur {string} récupère le questionnaire {string} du module {string}")
    public void leProfesseurRécupèreLeQuestionnaireDuModule(String arg0, String arg1, String arg2) {

    }

    @Quand("l'étudiant {string} récupère le questionnaire {string} du module {string}")
    public void lÉtudiantRécupèreLeQuestionnaireDuModule(String arg0, String arg1, String arg2) {

    }

    @Alors("la réponse recue est {int}")
    public void laRéponseRecueEst(int arg0) {

    }

    @Et("le questionnaire {string} est renvoyé")
    public void leQuestionnaireEstRenvoyé(String arg0) {

    }
}
