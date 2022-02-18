#language: fr
#encoding: utf-8

Fonctionnalité: En tant qu'utilisateur je veux pouvoir créer une question Code Runner et écrire du code python et l'éxécuter

  Contexte:
    Etant donné le professeur "Jacques" assigne au module "Programmation"
    Et l'etudiant "Margaux" assigne au module "Programmation"
    Et le professeur "Jacques" assigne au module "Prog 2"
    Et l'etudiant "Margaux" assigne au module "Prog 2"
    Et le questionnaire "Questionnaire code runner" dans le module "Prog 2"
    Et la question numero 1 "Factorial" avec description "Ecrire la fonction fact(n) qui calcule n!" la reponse est "720" avec le test "print(fact(6))" dans le "Questionnaire code runner" du module "Prog 2"

  Scénario: Ajout d'une question Code Runner
    Quand "Jacques" veut ajouter la question "Factorial" avec description "Ecrire la fonction fact(n) qui calcule n!" la réponse est "720" avec le test "print(fact(6))" au module "Programmation" dans le "Questionnaire code runner" crn
    Alors le code de retour est 200
    Et la question "Factorial" est créer dans le questionnaire "Questionnaire code runner" dans le module "Programmation"
    Et les tables sont videes

  Scénario: Execution du code envoyé par un élève
    Quand "Margaux" écrit son code python dans le fichier "fact_success.py" et soumet sont code au module "Prog 2" de la question numéro 1 dans le "Questionnaire code runner"
    Alors le code de retour est 200
    Et la réponse est vrai 1 crn
    Et les tables sont videes

  Scénario: Execution d'un code faux envoyé par un élève
    Quand "Margaux" écrit son code python dans le fichier "fact_wrong.py" et soumet sont code au module "Prog 2" de la question numéro 1 dans le "Questionnaire code runner"
    Alors le code de retour est 200
    Et la réponse est faux 0 crn
    Et les tables sont videes

