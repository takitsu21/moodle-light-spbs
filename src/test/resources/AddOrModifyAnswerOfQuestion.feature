# language: fr
# encoding: utf-8

  Fonctionnalité: Ajouter ou modifier une réponse d'une question

    Contexte:
      Etant donné Un élève "Antoine"
      Et un questionnaire "Test1"
      Et une question QCM "Q1" appartenant au questionnaire "Test1"
      Et une question open "Q2" appartenant au questionnaire "Test1", avec la réponse 2 choisie

    Scénario: Ajouter une réponse à la question open
      Quand "Antoine" choisis d'ajouter la réponse 1 à la question "Q2" du questionnaire "Test1"
      Alors La réponse est ajouté aux réponses donnée par l'élève dans la question

    Scénario: Retirer une réponse à la question open
      Quand "Antoine" choisis d'enlever la réponse 2 à la question "Q2" du questionnaire "Test1"
      Alors  La réponse est enlevé des réponses donnée par l'élève à la question

    Scénario: Choisir une réponse à une question QCM
      Quand "Antoine" choisi la réponse 1 à la question "Q1" du questinnaire "Test1"
      Alors La réponse choisi est la réponse 1
