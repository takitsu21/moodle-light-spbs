#language: fr
#encoding: utf-8

Fonctionnalité: En tant que professeur je veux pouvoir créer un QCM

  Contexte:
    Etant donné le professeur "Paul" assigne au module "Maths"
    Et le questionnaire "ExamQCM" dans le module "Maths"
    Et le questionnaire "ExamQCM" dans le module "Maths" est visible
    Et le questionnaire "ExamQCM" du module "Maths" a un QCM "Q1" qcm
    Et le questionnaire "ExamQCM" du module "Maths" a un QCM "Q2" qcm
    Et le QCM "Q1" du questionnaire "ExamQCM" du module "Maths" a les reponses possible "oui" et "non" qcm
    Et le QCM "Q2" du questionnaire "ExamQCM" du module "Maths" a les reponses possible "oui" et "non" qcm
    Et l'etudiant "Batiste" assigne au module "Maths"

  Scénario: Le professeur "Paul" ajoute une reponse
    Quand le professeur "Paul" essaie d'ajouter la reponse possible "je ne sais pas" au QCM "Q1" du questionnaire "ExamQCM" du module "Maths"
    Et le dernier status de request est 200 qcm
    Alors la reponse "je ne sais pas" est dans le QCM "Q1" du questionnaire "ExamQCM" du module "Maths"
    Et les tables sont videes

  Scénario: Le professeur "Paul" ajoute une bonne reponse
    Quand le professeur "Paul" essaie d'ajouter la bonne reponse "oui" au QCM "Q1" du questionnaire "ExamQCM" du module "Maths"
    Et le dernier status de request est 200 qcm
    Alors la bonne reponse "oui" est dans le QCM "Q1" du questionnaire "ExamQCM" du module "Maths"
    Et les tables sont videes

  Scénario: L'élève "Batiste" ajoute une reponse
    Quand L élève "Batiste" essaie d'ajouter ça reponse "oui" au QCM "Q1" du questionnaire "ExamQCM" du module "Maths"
    Et le dernier status de request est 200 qcm
    Alors la reponse de l'étudiant "Batiste" est "oui" est dans le QCM "Q1" du questionnaire "ExamQCM" du module "Maths"
    Et les tables sont videes

  Scénario: L'élève "Batiste" ajoute une reponse pas valable
    Quand L élève "Batiste" essaie d'ajouter ça reponse "coucou" au QCM "Q2" du questionnaire "ExamQCM" du module "Maths"
    Et le dernier status de request est 400 qcm
    Alors "Batiste" n a pas de reponse de l'étudiant au QCM "Q2" du questionnaire "ExamQCM" du module "Maths"
    Et les tables sont videes