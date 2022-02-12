#language: fr
#encoding: utf-8

Fonctionnalité: En tant qu'utilisateur je veux pouvoir créer une question Code Runner et écrire du code python et l'éxécuter

  Contexte:
    Etant donné Un enseignant avec le nom de connexion "Jacques" crn
    Et un module "Programmation" qui a un enseignant "Jacques" et un étudiant "Margaux" crn
    Et un module "Prog 2" qui a un enseignant "Jacques" et un étudiant "Margaux" et qui a la question numéro 1 "Factorial" avec description "Ecrire la fonction fact(n) qui calcule n!" la réponse est "720" avec le test "print(fact(6))" dans le "Questionnaire code runner" crn

  Scénario: Ajout d'une question Code Runner
    Quand "Jacques" veut ajouter la question "Factorial" avec description "Ecrire la fonction fact(n) qui calcule n!" la réponse est "720" avec le test "print(fact(6))" au module "Programmation" dans le "Questionnaire code runner" crn
    Alors le dernier status de réponse est 200 crn
    Et la question "Factorial" est créer dans le questionnaire "Questionnaire code runner" dans le module "Programmation"
    Et les tables sont videes

  Scénario: Execution du code envoyé par un élève
    Quand "Margaux" écrit son code python dans le fichier "fact_success.py" et soumet sont code au module "Prog 2" de la question numéro 1 dans le "Questionnaire code runner"
    Alors le dernier status de réponse est 200 crn
    Et la réponse est vrai 1 crn
    Et les tables sont videes

  Scénario: Execution d'un code faux envoyé par un élève
    Quand "Margaux" écrit son code python dans le fichier "fact_wrong.py" et soumet sont code au module "Prog 2" de la question numéro 1 dans le "Questionnaire code runner"
    Alors le dernier status de réponse est 200 crn
    Et la réponse est faux 0 crn
    Et les tables sont videes

