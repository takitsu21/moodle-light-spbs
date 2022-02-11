#language: fr
#encoding: utf-8

Fonctionnalité: En tant que enseignant je veux pouvoir modifier la visibilité des ressources

  Contexte:
    Etant donné le professeur "Paul" assigné au module de "Maths" avec un questionnaire "Exam" visible qcm
    Et le questionnaire "Exam" du module "Maths" a un QCM "Q1" qcm
    Et le QCM "Q1" du questionnaire "Exam" du module "Maths" a les reponses possible "oui" et "non" qcm
    Et l'élève "Batiste" assigné au module de "Maths" qcm

  Scénario: Le professeur "Paul" ajoute une reponse
    Quand le professeur "Paul" essaie d'ajouter la reponse possible "je ne sais pas" au QCM "Q1" du questionnaire "Exam" du module "Maths"
    Et le dernier status de request est 200 qcm
    Alors la reponse "je ne sais pas" est dans le QCM "Q1" du questionnaire "Exam" du module "Maths"

  Scénario: Le professeur "Paul" ajoute une bonne reponse
    Quand le professeur "Paul" essaie d'ajouter la bonne reponse "oui" au QCM "Q1" du questionnaire "Exam" du module "Maths"
    Et le dernier status de request est 200 qcm
    Alors la bonne reponse "oui" est dans le QCM "Q1" du questionnaire "Exam" du module "Maths"

  Scénario: L'élève "Batiste" ajoute une reponse
    Quand L élève "Batiste" essaie d'ajouter ça reponse "oui" au QCM "Q1" du questionnaire "Exam" du module "Maths"
    Et le dernier status de request est 200 qcm
    Alors la reponse de l'étudiant "Batiste" est "oui" est dans le QCM "Q1" du questionnaire "Exam" du module "Maths"