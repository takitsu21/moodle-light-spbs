import fr.uca.springbootstrap.models.*;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.questions.QCM;
import fr.uca.springbootstrap.models.questions.Question;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.QuestionnaireRepository;
import fr.uca.springbootstrap.repository.RoleRepository;
import fr.uca.springbootstrap.repository.UserRepository;
import fr.uca.springbootstrap.repository.question.QCMRepository;
import fr.uca.springbootstrap.repository.question.QuestionRepository;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import io.cucumber.java.hu.Ha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

public class TeacherModifyOrDeleteQuestionTest {
    private static final String PASSWORD = "password";

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    QCMRepository qcmRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;


    @Etantdonné("un professeur {string} ayant le module {string} tmdqa")
    public void unProfesseurAyantLeModuleTmdqa(String arg0, String arg1) {
        // Professeur
        User teacher = userRepository.findByUsername(arg0).
                orElse(new User(arg0,arg0+"@test.fr",encoder.encode(PASSWORD)));
        teacher.setRoles(new HashSet<Role>() {{
            add(roleRepository.findByName(ERole.ROLE_TEACHER).
                    orElseThrow(() -> new RuntimeException("Error: Role is no found")));
        }});
        userRepository.save(teacher);

        // Module
        Module module = moduleRepository.findByName(arg1).
                orElse(new Module(arg1));
        module.setParticipants(new HashSet<>(){{
            add(teacher);
        }});
        moduleRepository.save(module);
    }

    @Et("un professeur {string} n'ayant pas le module {string} tmdqb")
    public void unProfesseurNAyantPasLeModuleTmdqb(String arg0, String arg1) {
        // Professeur
        User teacher = userRepository.findByUsername(arg0).
                orElse(new User(arg0,arg0+"@test.fr",encoder.encode(PASSWORD)));
        teacher.setRoles(new HashSet<Role>() {{
            add(roleRepository.findByName(ERole.ROLE_TEACHER).
                    orElseThrow(() -> new RuntimeException("Error: Role is no found")));
        }});
        userRepository.save(teacher);
    }

    @Et("un questionnaire {string} appartenant à un module {string} tmdqc")
    public void unQuestionnaireAppartenantÀUnModuleTmdqc(String arg0, String arg1) {
        // Questionnaire
        Questionnaire questionnaire = questionnaireRepository.findByName(arg0).
                orElse(new Questionnaire(arg0, "Description "+arg0));
        questionnaireRepository.save(questionnaire);

        // Module
        Module module = moduleRepository.findByName(arg1).
                orElse(new Module(arg1));
        module.setRessources(new HashSet<>(){{
            add(questionnaire);
        }});
        moduleRepository.save(module);
    }

    @Et("une question QCM d'identifiant {int} et de nom {string} et de description {string} et le numéro {int} appartenant au questionnaire {string} tmdqd")
    public void uneQuestionQCMDIdentifiantEtDeNomEtDeDescriptionEtLeNuméroAppartenantAuQuestionnaireTmdqd(int arg0, String arg1, String arg2, int arg3, String arg4) {
        // Question
        Question question = questionRepository.findById(arg0).
                orElse(new QCM(arg3, arg1, arg2));
        questionRepository.save(question);

        // Questionnaire
        Questionnaire questionnaire = questionnaireRepository.findByName(arg4).
                orElse(new Questionnaire(arg4, "Description "+arg4));
        questionnaire.setQuestions(new HashSet<>(){{
            add(question);
        }});
        questionnaireRepository.save(questionnaire);
    }

    @Quand("Le professeur {string} veut supprimer la question {string} tmdqe")
    public void leProfesseurVeutSupprimerLaQuestionTmdqe(String arg0, String arg1) {
        User teacher = userRepository.findByUsername(arg0).get();
        Question question = questionRepository.findByName(arg1);

    }

    @Alors("le status de la dernière requète est {int} tmdqf")
    public void leStatusDeLaDernièreRequèteEstTmdqf(int arg0) {

    }

    @Et("la question {string} n'existe plus tmdqg")
    public void laQuestionNExistePlusTmdqg(String arg0) {

    }

    @Quand("Le professeur {string} veut supprimer la question {string} tmdqh")
    public void leProfesseurVeutSupprimerLaQuestionTmdqh(String arg0, String arg1) {

    }

    @Alors("le dernier status de réponse est {int} tmdqi")
    public void leDernierStatusDeRéponseEstTmdqi(int arg0) {

    }

    @Et("la question {string} existe tmdqj")
    public void laQuestionExisteTmdqj(String arg0) {

    }

    @Quand("Le professeur {string} veut modifier le nom de la question d'identifiant {int} par {string} tmdqk")
    public void leProfesseurVeutModifierLeNomDeLaQuestionDIdentifiantParTmdqk(String arg0, int arg1, String arg2) {

    }

    @Alors("le status de la dernière requète est {int} tmdqql")
    public void leStatusDeLaDernièreRequèteEstTmdqql(int arg0) {

    }

    @Et("la question d'identifant {int} a pour nom {string} tmdqm")
    public void laQuestionDIdentifantAPourNomTmdqm(int arg0, String arg1) {

    }

    @Quand("Le professeur {string} veut modifier le nom de la question d'identifant {int} par {string} tmdqn")
    public void leProfesseurVeutModifierLeNomDeLaQuestionDIdentifantParTmdqn(String arg0, int arg1, String arg2) {

    }

    @Alors("le dernier status de réponse est {int} tmdqqo")
    public void leDernierStatusDeRéponseEstTmdqqo(int arg0) {

    }

    @Et("la question d{string}appelle toujours {string} tmdqp")
    public void laQuestionDIdentifantSAppelleToujoursTmdqp(int arg0, String arg1) {

    }

    @Quand("Le professeur {string} veut modifier la description de la question d'identifiant {int} par {string} tmdqq")
    public void leProfesseurVeutModifierLaDescriptionDeLaQuestionDIdentifiantParTmdqq(String arg0, int arg1, String arg2) {

    }

    @Alors("le status de la dernière requète est {int} tmdqr")
    public void leStatusDeLaDernièreRequèteEstTmdqr(int arg0) {

    }

    @Et("la question d'identifiant {int} possède la description {string} tmdqs")
    public void laQuestionDIdentifiantPossèdeLaDescriptionTmdqs(int arg0, String arg1) {

    }

    @Quand("Le professeur {string} veut modifier la description de la question {string} par {string} tmdqt")
    public void leProfesseurVeutModifierLaDescriptionDeLaQuestionParTmdqt(String arg0, String arg1, String arg2) {

    }

    @Alors("le dernier status de réponse est {int} tmdqu")
    public void leDernierStatusDeRéponseEstTmdqu(int arg0) {

    }

    @Et("la question d'identifiant {int} possède toujours la description {string} tmdqv")
    public void laQuestionDIdentifiantPossèdeToujoursLaDescriptionTmdqv(int arg0, String arg1) {

    }

    @Quand("le professeur {string} veut modifier le numéro de la question d'identifiant {int} par {int} tmdqw")
    public void leProfesseurVeutModifierLeNuméroDeLaQuestionDIdentifiantParTmdqw(String arg0, int arg1, int arg2) {

    }

    @Alors("le dernier status de réponse est {int} tmdqx")
    public void leDernierStatusDeRéponseEstTmdqx(int arg0) {

    }

    @Et("le question d'identifiant {int} possède le numéro {int} tmdqy")
    public void leQuestionDIdentifiantPossèdeLeNuméroTmdqy(int arg0, int arg1) {

    }

    @Quand("le professeur {string} veut modifier le numéro de la question d'indentifiant {int} par {int} tmdqz")
    public void leProfesseurVeutModifierLeNuméroDeLaQuestionDIndentifiantParTmdqz(String arg0, int arg1, int arg2) {

    }

    @Alors("le dernier status de réponse et {int} tmdqaa")
    public void leDernierStatusDeRéponseEtTmdqaa(int arg0) {

    }

    @Et("la question d'identifiant {int} possède le numéro {int} tmdqab")
    public void laQuestionDIdentifiantPossèdeLeNuméroTmdqab(int arg0, int arg1) {

    }
}
