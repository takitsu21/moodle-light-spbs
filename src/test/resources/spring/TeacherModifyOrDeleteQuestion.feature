# language: fr
# encoding: utf-8

Fonctionnalité: Professeur modifie (nom/description) ou supprime une question

  Contexte:
    Etant donné un professeur "Jean" ayant le module "Gestion de projet" tmdqa
    Et un professeur "Marie" n'ayant pas le module "Gestion de projet" tmdqb
    Et un questionnaire "Test35" appartenant à un module "Gestion de projet" tmdqc
    Et une question QCM de nom "Ordinateur" et de description "Bonjour ?" et de numéro 1 appartenant au questionnaire "Test35" du module "Gestion de projet" tmdqd

  Scénario: Modifier le nom d'une question appartenant a un module que l'on possède
    Quand Le professeur "Jean" veut modifier le nom de la question de numéro 1 du questionnaire "Test35" du module "Gestion de projet" par "Ordinateur new" tmdqk
    Alors  le status de la dernière requète est 200 tmdqql
    Et la question de numéro 1 a pour nom "Ordinateur new" tmdqm
    Et les tables sont videes

  Scénario: Modifier le nom d'une question appartenant a une module qu'on ne possède pas
    Quand Le professeur "Marie" veut modifier le nom de la question de numéro 1 du questionnaire "Test35" du module "Gestion de projet" par "Ordinateur mauvaise" tmdqn
    Alors le dernier status de réponse est 400 tmdqqo
    Et la question de numéro 1 s'appelle toujours "Ordinateur" tmdqp
    Et les tables sont videes

  Scénario: Modifier la description d'une question appartenant a un module que l'on possède
    Quand Le professeur "Jean" veut modifier la description de la question "Ordinateur" du questionnaire "Test35" du module "Gestion de projet" par "Au revoir ?" tmdqq
    Alors  le status de la dernière requète est 200 tmdqr
    Et la question "Ordinateur" possède la description "Au revoir ?" tmdqs
    Et les tables sont videes

  Scénario: Modifier la description d'une question appartenant a une module qu'on ne possède pas
    Quand Le professeur "Marie" veut modifier la description de la question "Ordinateur" du questionnaire "Test35" du module "Gestion de projet" par "A midi ?" tmdqt
    Alors le dernier status de réponse est 400 tmdqu
    Et la question "Ordinateur" possède toujours la description "Bonjour ?" tmdqv
    Et les tables sont videes

  Scénario: Modifier le numéro d'une question appartenant a un module que l'on possède
    Quand le professeur "Jean" veut modifier le numéro de la question de nom "Ordinateur" du questionnaire "Test35" du module "Gestion de projet" par 2 tmdqw
    Alors le dernier status de réponse est 200 tmdqx
    Et le question "Ordinateur" possède le numéro 2 tmdqy
    Et les tables sont videes

  Scénario: Modifier le numéro d'une question appartenant a un module que l'on ne possède pas
    Quand le professeur "Marie" veut modifier le numéro de la question "Ordinateur" du questionnaire "Test35" du module "Gestion de projet" par 3 tmdqz
    Alors le dernier status de réponse et 400 tmdqaa
    Et la question "Ordinateur" possède le numéro 1 tmdqab
    Et les tables sont videes
