#language: fr
#encoding: utf-8

Fonctionnalité: En tant que API REST je veux pouvoir calculé la note d'un élève à un questionnaire quand il le soumet


  Contexte:
    Etant donné Un enseignant avec le nom de connexion "Jacques" sq
    Et un module "Prog 2" qui a un enseignant "Jacques" et un étudiant "Margaux" et qui a la question numéro 1 "Factorial" avec description "Ecrire la fonction fact(n) qui calcule n!" la réponse est "720" avec le test "print(fact(6))" dans le "Questionnaire code runner" sq
    Et "Margaux" écrit son code python dans le fichier "fact_success.py" et soumet sont code au module "Prog 2" de la question numéro 1 dans le "Questionnaire code runner" sq

  Scénario: Soumission du questionnaire bonne note
    Quand "Margaux" soumet le questionnaire "Questionnaire code runner" du module "Prog 2" sq
    Alors le dernier status de réponse est 200 sq
    Et la note est 1 sur 1 sq