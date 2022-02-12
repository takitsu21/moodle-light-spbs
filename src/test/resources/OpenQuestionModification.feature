# language: fr
# encoding: utf-8

  Fonctionnalité: Modification d'une question ouverte

    Contexte:
      Etant donné Un module "Maths" oqm
      Et un questionnaire "Questionnaire 1" contenu dans le module "Maths" oqm
      Et une question ouverte "Question 1" contenue dans le questionnaire "Questionnaire 1" du module "Maths" oqm
      Et "Question 1" du questionnaire "Questionnaire 1" du module "Maths" ayant comme réponses possibles "A", "B", "C" oqm
      Et "Question 1" du questionnaire "Questionnaire 1" du module "Maths" ayant comme réponses "A" et "B" oqm
      Et un professeur "Cédric" ayant le module "Maths" oqm
      Et un professeur "Cinzia" n'ayant pas de module oqm

    Scénario: Professeur ajoute une réponse possible au questionnaire en ayant le bon module
      Quand le professeur "Cédric" ajoute une réponse possible de contenu "D" a la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths" oqm
      Alors le status de la dernière réponse est 200 oqm
      Et les réponses possibles de la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths" possède un réponse de contenu "D" oqm
