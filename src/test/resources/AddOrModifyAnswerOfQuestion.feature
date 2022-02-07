# language: fr
# encoding: utf-8

  Fonctionnalité: Ajouter ou modifier une réponse d'une question

    Contexte:
      Etant donné Un élève "Antoine" ayant le cours "Gestion de projet"
      Et un élève "Léa" n'ayant pas le cours "Gestion de projet"
      Et un questionnaire "Test1" appartenant à un module "Gestion de projet"
      Et une question QCM "Q1" appartenant au questionnaire "Test1" possédant 4 réponses possible
      Et une question open "Q2" appartenant au questionnaire "Test1", avec la réponse 2 choisie ayant 4 réponses possible

    Scénario: Ajouter une réponse à la question open
      Quand "Antoine" choisis d'ajouter la réponse 1 à la question "Q2" du questionnaire "Test1"
      Alors le status de la dernière requète est 200
      Et La réponse est ajouté aux réponses donnée par l'élève dans la question

    Scénario: Retirer une réponse à la question open
      Quand "Antoine" choisis d'enlever la réponse 2 à la question "Q2" du questionnaire "Test1"
      Alors le status de la dernière requète est 200
      Et La réponse est enlevé des réponses donnée par l'élève à la question

    Scénario: Choisir une réponse à une question QCM
      Quand "Antoine" choisi la réponse 1 à la question "Q1" du questinnaire "Test1"
      Alors le status de la dernière requète est 200
      Et La réponse choisi est la réponse 1

    Scénario: Vouloir choisir une réponse qui n'existe pas a un QCM
      Quand "Antoine" choisi la réponse 5 de la question "Q1"
      Alors le status de la requète est 400
      Et "Antoine" n'arrive pas a choisir cette réponse

    Scénario: Vouloir ajouter une réponse qui n'existe pas a une question libre
      Quand "Antoine" choisi la réponse 5 de la question "Q2"
      Alors le status de la requète est 400
      Et "Antoine" n'arrive pas a choisir cette réponse

    Scénario: Vouloir enlever une réponse qui n'existe pas a une question libre
      Quand "Antoine" choisi la réponse 5 de la question "Q2"
      Alors le status de la requète est 400
      Et "Antoine" n'arrive pas a choisir cette réponse

    Scénario: Vouloir répondre a un QCM appartenant a un cours non pris par l'élève
      Quand "Léa" choisis la réponse 1 à la question "Q1"
      Alors le status de la dernière requète est 400
      Et "Léa" ne réussi pas à répondre

    Scénario: Vouloir ajouter une réponse a une question ouverte appartenant a un cours non pris par l'élève
      Quand "Léa" choisis d'ajouter la réponse 1 à la question "Q1"
      Alors le status de la dernière requète est 400
      Et "Léa" ne réussi pas à répondre

    Scénario: Vouloir enlever une réponse a une question ouverte appartenant a un cours non pris par l'élève
      Quand "Léa" choisis d'ajouter la réponse 1 à la question "Q1"
      Alors le status de la dernière requète est 400
      Et "Léa" ne réussi pas à répondre

