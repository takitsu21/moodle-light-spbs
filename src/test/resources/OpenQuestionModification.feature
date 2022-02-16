# language: fr
# encoding: utf-8

  Fonctionnalité: Modification d'une question ouverte

    Contexte:
      Etant donné Un module "Maths" oqm
      Et le questionnaire "Questionnaire 1" dans le module "Maths"
      Et une question ouverte "Question 1" contenue dans le questionnaire "Questionnaire 1" du module "Maths" oqm
      Et "Question 1" du questionnaire "Questionnaire 1" du module "Maths" ayant comme réponses possibles "A", "B", "C" oqm
      Et "Question 1" du questionnaire "Questionnaire 1" du module "Maths" ayant comme réponses "A" et "B" oqm
      Et le professeur "Cédric" assigne au module "Maths"
      Et le professeur "Cinzia"

    Scénario: Professeur ajoute une réponse possible au questionnaire en ayant le bon module
      Quand le professeur "Cédric" ajoute une réponse possible de contenu "D" a la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths" oqm
      Alors le code de retour est 200
      Et les réponses possibles de la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths" possède un réponse de contenu "D" oqm
      Et les tables sont videes
