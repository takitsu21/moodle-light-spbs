# language: fr
# encoding: utf-8

Fonctionnalité: Ajouter ou modifier une réponse d'une question

  Contexte:
    Etant donné Un élève "Antoine" ayant le module "Gestion de projet"
    Et un élève "Léa" n'ayant pas le module "Gestion de projet"
    Et un questionnaire "Test1" appartenant à un module "Gestion de projet"
    Et une question QCM "Q1" appartenant au questionnaire "Test1" possédant 4 réponses possible
    Et une question open "Q2" appartenant au questionnaire "Test1", avec la réponse 2 choisie ayant 4 réponses possible

  Scénario: Ajouter une réponse à la question open
    Quand "Antoine" choisis d'ajouter la réponse 1 à la question "Q2" du questionnaire "Test1" amq1
    Alors le status de la dernière requète est 200 amq2
    Et La réponse est ajouté aux réponses donnée par l'élève dans la question amq3

  Scénario: Retirer une réponse à la question open
    Quand "Antoine" choisis d'enlever la réponse 2 à la question "Q2" du questionnaire "Test1" amq4
    Alors le status de la dernière requète est 200 amq5
    Et La réponse est enlevé des réponses donnée par l'élève à la question amq6

  Scénario: Choisir une réponse à une question QCM
    Quand "Antoine" choisi la réponse 1 à la question "Q1" du questionnaire "Test1" amq7
    Alors le status de la dernière requète est 200 amq8
    Et La réponse choisi est la réponse 1 amq9

  Scénario: Vouloir choisir une réponse qui n'existe pas a un QCM
    Quand "Antoine" choisi la réponse 5 de la question "Q1" amq10
    Alors le status de la requète est 400 amq11
    Et "Antoine" n'arrive pas a choisir cette réponse amq12

  Scénario: Vouloir ajouter une réponse qui n'existe pas a une question libre
    Quand "Antoine" choisi la réponse 5 de la question "Q2" amq13
    Alors le status de la requète est 400 amq14
    Et "Antoine" n'arrive pas a choisir cette réponse amq15

  Scénario: Vouloir enlever une réponse qui n'existe pas a une question libre
    Quand "Antoine" choisi la réponse 5 de la question "Q2" amq16
    Alors le status de la requète est 400 amq17
    Et "Antoine" n'arrive pas a choisir cette réponse amq18

  Scénario: Vouloir répondre a un QCM appartenant a un module non pris par l'élève
    Quand "Léa" choisis la réponse 1 à la question "Q1" amq19
    Alors le status de la dernière requète est 400 amq20
    Et "Léa" ne réussi pas à répondre amq21

  Scénario: Vouloir ajouter une réponse a une question ouverte appartenant a un module non pris par l'élève
    Quand "Léa" choisis d'ajouter la réponse 1 à la question "Q2" amq22
    Alors le status de la dernière requète est 400 amq23
    Et "Léa" ne réussi pas à répondre amq24

  Scénario: Vouloir enlever une réponse a une question ouverte appartenant a un module non pris par l'élève
    Quand "Léa" choisis d'ajouter la réponse 1 à la question "Q1" amq25
    Alors le status de la dernière requète est 400 amq26
    Et "Léa" ne réussi pas à répondre amq26

