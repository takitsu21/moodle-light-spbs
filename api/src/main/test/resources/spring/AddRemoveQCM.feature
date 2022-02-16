#language: fr
#encoding: utf-8

Fonctionnalité: Ajouter ou retirer une question quelconque d'un questionnaire

  Contexte:
    Etant donné le professeur "Jean-Patrick" assigne au module "Sport"
    Et le professeur "Mariette"
    Et le questionnaire "Controle" dans le module "Sport"
    Et la question "QCM1" dans le questionnaire "Controle" du module "Sport" arqqq

  Scénario: Ajouter un QCM dans un module qu'on possède
    Quand Le professeur "Jean-Patrick" veut ajouter la question "QCM2" au questionnaire "Controle" dans le module "Sport" arqqq
    Alors la réponse est 200 arqqq
    Et la question "QCM2" existe dans le questionnaire "Controle" du module "Sport" arqqq
    Et les tables sont videes

  Scénario: Ajouter un QCM dans un module qu'on ne possède pas
    Quand Le professeur "Mariette" veut ajouter la question "QCM3" au questionnaire "Controle" dans le module "Sport" arqqq
    Alors la réponse est 400 arqqq
    Et la question "QCM3" n'existe pas dans le questionnaire "Controle" du module "Sport" arqqq
    Et les tables sont videes

  Scénario: Supprimer une question appartenant a un module que l'on possède
    Quand Le professeur "Jean-Patrick" veut supprimer la question "QCM1" du questionnaire "Controle" dans le module "Sport" arqqq
    Alors  la réponse est 200 arqqq
    Et la question "QCM1" n'existe pas dans le questionnaire "Controle" du module "Sport" arqqq
    Et les tables sont videes

  Scénario: Supprimer une question appartenant a un module que l'on ne possède pas
    Quand Le professeur "Mariette" veut supprimer la question "QCM1" du questionnaire "Controle" dans le module "Sport" arqqq
    Alors la réponse est 400 arqqq
    Et la question "QCM1" existe dans le questionnaire "Controle" du module "Sport" arqqq
    Et les tables sont videes
