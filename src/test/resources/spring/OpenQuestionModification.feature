# language: fr
# encoding: utf-8

  Fonctionnalité: Modification d'une question ouverte

    Contexte:
      Etant donné que les tables sont videes
      Et Un module "Maths"
      Et un questionnaire "Questionnaire 1" contenu dans le module "Maths"
      Et une question ouverte "Question 1" contenue dans le le questionnaire "Questionnaire 1" du module "Maths"
      Et la "Question 1" du questionnaire "Questionnaire 1" du module "Maths" a comme réponses possibles "A", "B", "C"
      Et la "Question 1" du questionnaire "Questionnaire 1" du module "Maths" a comme réponses "A" et "B"
      Et la "Question 2" du questionnaire "Questionnaire 1" du module "Maths" a commme réponses possibles "Z" et comme réponse "Z"
      Et un professeur "Cédric" ayant le module "Maths"
      Et un professeur "Cinzia" n'ayant pas de module
      Et un élève "Yann" ayant le module "Maths"
      Et "Yann" a répondu "A", "C" a la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths"
      Et "Yann" a répondu "Z" a la question "Question 2" du questionnaire "Questionnaire 1" du module "Maths"

      Et un élève "JP" n'ayant pas le module "Maths"


    Scénario: Professeur supprime une réponse possible d'une question de son module
      Quand le professeur "Cédric" veut supprimer la réponse possible "A" de la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths"
      Alors le status de la dernière requète est 200
      Et la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths" a pas comme réponse et réponse possible "A"

    Scénario: Professeur supprime une réponse possible d'une question contenant une seule réponse possible de son module
      Quand le professeur "Cédric" veut supprimer la réponse possible "Z" de la question "Question 2" du questionnaire "Questionnaire 1" du module "Maths"
      Alors le status de la dernière requète est 400
      Et la question "Question 2" du questionnaire "Questionnaire 1" du module "Maths" a comme réponses possible "Z" et comme réponses "Z"

    Scénario: Professeur supprime une réponse possible d'une question d'un module ne lui appartenant pas
      Quand le professeur "Cinzia" veut supprimer la réponse possible "A" de la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths"
      Alors le status de la dernière requète est 400
      Et la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths" a comme réponses possible "A" et comme réponses "A"


    Scénario: Professeur supprime une réponse d'une question de son module
      Quand le professeur "Cédric" veut supprimer la réponse "A" de la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths"
      Alors le status de la dernière requète est 200
      Et la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths" a comme réponse possible "A" et na pas comme réponses "A"

    Scénario: Professeur supprime une réponse d'une question contenant une seule réponse de son module
      Quand le professeur "Cédric" veut supprimer la réponse "Z" de la question "Question 2" du questionnaire "Questionnaire 1" du module "Maths"
      Alors le status de la dernière requète est 400
      Et la question "Question 2" du questionnaire "Questionnaire 1" du module "Maths" a comme réponses possible "Z" et comme réponses "Z"

    Scénario: Professeur supprime une réponse d'une question d'un module ne lui appartenant pas
      Quand le professeur "Cinzia" veut supprimer la réponse "A" de la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths"
      Alors le status de la dernière requète est 400
      Et la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths" a comme réponses possible "A" et comme réponses "A"

    Scénario: Professeur ajoute une réponse possible a une question de son module
      Quand le professeur "Cédric" ajoute la réponse possible "D" a la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths"
      Alors le status de la dernière requète est 200
      Et la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths" a comme réponse possible "D" et na pas comme réponses "D"

    Scénario: Professeur ajoute une réponse possible a une question d'un module qu'il n'a pas
      Quand le professeur "Cinzia" ajoute la réponse possible "D" a la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths"
      Alors le status de la dernière requète est 400
      Et la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths" a pas comme réponse et réponse possible "D"

    Scénario: Professeur ajoute une réponse (appartenant aux réponses possibles) a une question de son module
      Quand le professeur "Cédric" ajoute la réponse "C" a la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths"
      Alors le status de la dernière requète est 200
      Et les réponses de la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths" contiennent la réponse "C"

    Scénario: Professeur ajoute une réponse (appartenant aux réponses possibles) a une question d'un module qui ne lui appartient pas
      Quand le professeur "Cinzia" ajoute la réponse "C" a la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths"
      Alors le status de la dernière requète est 400
      Et les réponses de la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths" ne contiennent pas la réponse "C"

    Scénario: Professeur ajoute une réponse n'appartenant pas aux réponses possibles de sa question
      Quand le professeur "Cinzia" ajoute la réponse "D" a la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths"
      Alors le status de la dernière requète est 400
      Et les réponses de la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths" ne contiennent pas la réponse "D"

    Scénario: Elève ajoute une réponse (appartenant aux réponses possibles) a une question de son module
      Quand l'élève "Yann" veut ajouter sa réponse "B" a la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths"
      Alors le status de la dernière requète est 200
      Et la réponses de "Yann" a la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths" contient la réponse "B"

    Scénario: Elève ajoute une réponse (appartenant aux réponses possibles) a une question d'un module qu'il n'a pas
      Quand l'élève "Jp" veut supprimer sa réponse "B" a la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths"
      Alors le status de la dernière requète est 400
      Et la réponses de "Yann" a la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths" ne contient pas "B"

    Scénario: Elève ajoute une réponse (n'appartenant pas aux réponses possibles) a une question de son module
      Quand l'élève "Yann" veut supprimer sa réponse "D" a la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths"
      Alors le status de la dernière requète est 400
      Et la réponses de "Yann" a la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths" ne contient pas "D"

    Scénario: Elève supprime une des ses réponses a une question de son module
      Quand l'élève "Yann" veut supprimer sa réponse "A" a la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths"
      Alors le status de la dernière requète est 200
      Et la réponses de "Yann" a la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths" ne contient pas "A"

    Scénario: Elève supprine une de ses réponses a une question d'un module qu'il n'a pas
      Quand l'élève "Jp" veut supprimer sa réponse "A" a la question "Question 1" du questionnaire "Questionnaire 1" du module "Maths"
      Alors le status de la dernière requète est 400