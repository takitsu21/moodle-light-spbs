#language: fr
#encoding: utf-8

Fonctionnalité: En tant que professeur je veux pouvoir créer une question ouverte

  Contexte:
    Etant donné le professeur "Paul" assigné au module de "Maths" avec un questionnaire "Exam" visible oq
    Et le questionnaire "Exam" du module "Maths" a une question ouverte "Q1"
    Et le questionnaire "Exam" du module "Maths" a une question ouverte "Q2"
    Et la question ouverte "Q1" du questionnaire "Exam" du module "Maths" a les reponses possible "oui" et "non" et "noui"
    Et la question ouverte "Q2" du questionnaire "Exam" du module "Maths" a les reponses possible "oui" et "non" et "noui"
    Et l'etudiant "Batiste" assigne au module "Maths"

  Scénario: Le professeur "Paul" ajoute une reponse
    Quand le professeur "Paul" essaie d'ajouter la reponse possible "je ne sais pas" a la question ouverte "Q1" du questionnaire "Exam" du module "Maths"
    Et le code de retour est 200
    Alors la reponse "je ne sais pas" est dans la question ouverte "Q1" du questionnaire "Exam" du module "Maths" oq
    Et les tables sont videes

  Scénario: Le professeur "Paul" ajoute une bonne reponse
    Quand le professeur "Paul" essaie d'ajouter les bonnes reponses "oui" et "noui" a la question ouverte "Q1" du questionnaire "Exam" du module "Maths"
    Et le code de retour est 200
    Alors les bonnes reponses "oui" et "noui" sont dans la question ouverte "Q1" du questionnaire "Exam" du module "Maths" oq
    Et les tables sont videes

  Scénario: L'élève "Batiste" ajoute une reponse
    Quand L élève "Batiste" essaie d'ajouter ses reponses "oui" et "noui" a la question ouverte "Q1" du questionnaire "Exam" du module "Maths"
    Et le code de retour est 200
    Alors les reponses de l'étudiant "Batiste" sont "oui" et "noui" dans la question ouverte "Q1" du questionnaire "Exam" du module "Maths" oq
    Et les tables sont videes

  Scénario: L'élève "Batiste" ajoute une reponse pas valable
    Quand L élève "Batiste" essaie d'ajouter ça reponse "coucou" a la question ouverte "Q2" du questionnaire "Exam" du module "Maths"
    Et le code de retour est 400
    Alors "Batiste" n a pas de reponse de l'étudiant a la question ouverte "Q2" du questionnaire "Exam" du module "Maths" oq
    Et les tables sont videes