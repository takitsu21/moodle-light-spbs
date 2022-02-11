package fr.uca.springbootstrap;

import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;

public class GetQuestionStepdefs {
    //TODO
    @Etantdonné("le questionnaire {string} dans le module {string} auq")
    public void leQuestionnaireDansLeModuleAuq(String arg0, String arg1) {
    }


    @Et("l'étudiant {string} dans le module {string} auq")
    public void lÉtudiantDansLeModuleAuq(String arg0, String arg1) {

    }

    @Et("l'étudiant {string} sans module auq")
    public void lÉtudiantSansModuleAuq(String arg0) {

    }

    @Et("la question {string} numéro {int} dans le questionnaire {string} auq")
    public void laQuestionNuméroDansLeQuestionnaireAuq(String arg0, int arg1, String arg2) {

    }

    @Quand("l'étudiant {string} récupère la question {string} du questionnaire {string} du module {string} auq")
    public void lÉtudiantRécupèreLaQuestionDuQuestionnaireDuModuleAuq(String arg0, String arg1, String arg2, String arg3) {

    }

    @Alors("la réponse est {int} auq")
    public void laRéponseEstAuq(int arg0) {

    }

    @Et("la question {string} en renvoyée auq")
    public void laQuestionEnRenvoyéeAuq(String arg0) {
    }
}
