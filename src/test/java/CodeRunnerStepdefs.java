import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;

public class CodeRunnerStepdefs {
    @Etantdonné("Un enseignant avec le nom de connexion {string} crn")
    public void unEnseignantAvecLeNomDeConnexionCrn(String arg0) {
    }

    @Et("un module {string} qui a un enseignant {string} et un étudiant {string} crn")
    public void unModuleQuiAUnEnseignantEtUnÉtudiantCrn(String arg0, String arg1, String arg2) {
    }

    @Et("un module {string} qui a un enseignant {string} et un étudiant {string} et qui a la question numéro {int} {string} avec description {string} la réponse est {string} avec le test {string} question numéro {int} crn")
    public void unModuleQuiAUnEnseignantEtUnÉtudiantEtQuiALaQuestionNuméroAvecDescriptionLaRéponseEstAvecLeTestQuestionNuméroCrn(String arg0, String arg1, String arg2, int arg3, String arg4, String arg5, String arg6, String arg7, int arg8) {
    }

    @Quand("{string} veut ajouter la question {string} avec description {string} la réponse est {string} avec le test {string} crn")
    public void veutAjouterLaQuestionAvecDescriptionLaRéponseEstAvecLeTestCrn(String arg0, String arg1, String arg2, String arg3, String arg4) {
    }

    @Alors("le dernier status de réponse est {int} crn")
    public void leDernierStatusDeRéponseEstCrn(int arg0) {
    }

    @Et("la question est créer")
    public void laQuestionEstCréer() {
    }

    @Quand("{string} écrit son code python {string} et soumet sont code au module {string} de la question numéro {int}")
    public void écritSonCodePythonEtSoumetSontCodeAuModuleDeLaQuestionNuméro(String arg0, String arg1, String arg2, int arg3) {
    }

    @Et("la réponse est vrai {int} crn")
    public void laRéponseEstVraiCrn(int arg0) {
    }

    @Et("la réponse est faux {int} crn")
    public void laRéponseEstFauxCrn(int arg0) {
    }
}
