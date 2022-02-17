#language: fr
#encoding: utf-8

Fonctionnalité: En tant que API REST je veux pouvoir calculer la note d'un élève à un questionnaire quand il le soumet


  Contexte:
    Etant donné le professeur "Jacques"
    Et un module "Prog 2" qui a un enseignant "Jacques" et un étudiant "Margaux" et qui a la question numéro 1 "Factorial" avec description "Ecrire la fonction fact(n) qui calcule n!" la réponse est "720" avec le test "print(fact(6))" dans le "Questionnaire code runner" sq
    Et le questionnaire "Questionnaire QCM" dans le module "Prog 2"
    # Ce stepdef est dans CodeRunnerStepdefs
    Et "Margaux" écrit son code python dans le fichier "fact_success.py" et soumet sont code au module "Prog 2" de la question numéro 1 dans le "Questionnaire code runner"
    Et le QCM numero 1 "Culture G" avec pour description "Question de culture generale" et pour bonne response "Bonne reponse" dans le "Questionnaire QCM" du module "Prog 2"
    # Step def dans QCMStepdefs
    Et L élève "Margaux" essaie d'ajouter sa reponse "Bonne reponse" au QCM "Culture G" du questionnaire "Questionnaire QCM" du module "Prog 2"

  Scénario: Soumission du questionnaire bonne note
    Quand "Margaux" soumet le questionnaire "Questionnaire code runner" du module "Prog 2" sq
    Alors le code de retour est 200
    Et la note est 1 sur 1 sq
    Et les tables sont videes

  Scénario: Soumission du questionnaire avec QCM bonne note
    Quand "Margaux" soumet le questionnaire "Questionnaire QCM" du module "Prog 2" sq
    Alors le code de retour est 200
    Et la note est 1 sur 1 sq
    Et les tables sont videes