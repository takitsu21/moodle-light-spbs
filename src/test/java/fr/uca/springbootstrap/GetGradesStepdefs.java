package fr.uca.springbootstrap;

import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;

public class GetGradesStepdefs {
    @Etantdonné("le professeur {string} assigné au module de {string} gg")
    public void leProfesseurAssignéAuModuleDeGg(String arg0, String arg1) {
    }

    @Et("un module {string} qui a un enseignant {string} et un étudiant {string} et qui a la question numéro {int} {string} avec description {string} la réponse est {string} avec le test {string} dans le {string} gg")
    public void unModuleQuiAUnEnseignantEtUnÉtudiantEtQuiALaQuestionNuméroAvecDescriptionLaRéponseEstAvecLeTestDansLeGg(String arg0, String arg1, String arg2, int arg3, String arg4, String arg5, String arg6, String arg7, String arg8) {
    }

    @Et("{string} soumet le questionnaire {string} du module {string} gg")
    public void soumetLeQuestionnaireDuModuleGg(String arg0, String arg1, String arg2) {
    }

    @Quand("{string} récupères les notes du questionnaire {string} du module {string} gg")
    public void récupèresLesNotesDuQuestionnaireDuModuleGg(String arg0, String arg1, String arg2) {
    }

    @Alors("le dernier status de request est {int} gg")
    public void leDernierStatusDeRequestEstGg(int arg0) {
    }

    @Et("les notes de {string} sont affichés et à un résultat de {int} sur {int}")
    public void lesNotesDeSontAffichésEtÀUnRésultatDeSur(String arg0, int arg1, int arg2) {
    }
}
