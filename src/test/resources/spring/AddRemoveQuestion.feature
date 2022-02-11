#language: fr
#encoding: utf-8

Fonctionnalité: Ajouter ou retirer une question quelconque d'un questionnaire

  Contexte:
    Etant donné le professeur "Jean" assigné au module de "Sport" arqqq
    Et le professeur "Marie" sans module arqqq
    Et le questionnaire "Controle" dans le module "Sport" arqqq
    Et la question "Q1" dans le questionnaire "Controle" arqqq

  Scénario: Supprimer une question appartenant a un module que l'on possède
    Quand Le professeur "Jean" veut supprimer la question "Q1" du questionnaire "Controle" dans le module "Sport" arqqq
    Alors  la réponse est 200 arqqq
    Et la question "Q1" n'existe plus dans le questionnaire "Controle" arqqq

  Scénario: Supprimer une question appartenant a un module que l'on ne possède pas
    Quand Le professeur "Marie" veut supprimer la question "Q1" du questionnaire "Controle" dans le module "Sport" arqqq
    Alors la réponse est 400 arqqq
    Et la question "Q1" existe dans le questionnaire "Controle" arqqq