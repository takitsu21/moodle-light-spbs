#language: fr
#encoding: utf-8

Fonctionnalité: En tant que professeur je veux pouvoir voir la note de tous les étudiants

  Contexte:
    Etant donné le professeur "Paul" assigné au module de "Maths" gg
    Et un module "Prog 2" qui a un enseignant "Jacques", un etudiant "Margaux" et un etudiant "Jeannot" gg
    Et un questionnaire "Questionnaire code runner" appartenant à un module "Prog 2" gg
    Et la question numéro 1 "Factorial" avec description "Ecrire la fonction fact(n) qui calcule n!" la réponse est "720" avec le test "print(fact(6))" dans le "Questionnaire code runner" du module "Prog 2" gg
    Et "Margaux" soumet son code dans le fichier "fact_success.py" à la question 1 du questionnaire "Questionnaire code runner" du module "Prog 2"
    Et "Margaux" soumet le questionnaire "Questionnaire code runner" du module "Prog 2" gg

  Scénario: Le professeur récupères les notes de chaque étudiant
    Quand "Jacques" récupères les notes du questionnaire "Questionnaire code runner" du module "Prog 2" gg
    Alors le dernier status de request est 200 gg
    Et les notes de "Margaux" sont affichées et avec un résultat de "1/1"
    Et les tables sont videes


