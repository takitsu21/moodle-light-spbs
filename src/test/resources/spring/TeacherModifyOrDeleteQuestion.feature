# language: fr
# encoding: utf-8

Fonctionnalité: Professeur modifie (nom/description) ou supprime une question

  Contexte:
    Etant donné un professeur "Jean" ayant le module "Gestion de projet" tmdqa
    Et un professeur "Marie" n'ayant pas le module "Gestion de projet" tmdqb
    Et un questionnaire "Test1" appartenant à un module "Gestion de projet" tmdqc
    Et une question QCM d'identifiant 1 et de nom "Q1" et de description "Bonjour ?" et le numéro 1 appartenant au questionnaire "Test1" tmdqd

  Scénario: Supprimer une question appartenant a un module que l'on possède
    Quand Le professeur "Jean" veut supprimer la question "Q1" du questionnaire "Test1" du module "Gestion de projet" tmdqe
    Alors  le status de la dernière requète est 200 tmdqf
    Et la question "Q1" n'existe plus tmdqg

  Scénario: Supprimer une question appartenant a un module que l'on ne possède pas
    Quand Le professeur "Marie" veut supprimer la question "Q1" du questionnaire "Test1" du module "Gestion de projet" tmdqh
    Alors le dernier status de réponse est 400 tmdqi
    Et la question "Q1" existe tmdqj

  Scénario: Modifier le nom d'une question appartenant a un module que l'on possède
    Quand Le professeur "Jean" veut modifier le nom de la question d'identifiant 1 du questionnaire "Test1" du module "Gestion de projet" par "Q1 new" tmdqk
    Alors  le status de la dernière requète est 200 tmdqql
    Et la question d'identifant 1 a pour nom "Q1 new" tmdqm

  Scénario: Modifier le nom d'une question appartenant a une module qu'on ne possède pas
    Quand Le professeur "Marie" veut modifier le nom de la question d'identifant 1 du questionnaire "Test1" du module "Gestion de projet" par "Q1 mauvaise" tmdqn
    Alors le dernier status de réponse est 400 tmdqqo
    Et la question didentifant 1 s'appelle toujours "Q1 new" tmdqp

  Scénario: Modifier la description d'une question appartenant a un module que l'on possède
    Quand Le professeur "Jean" veut modifier la description de la question d'identifiant 1 du questionnaire "Test1" du module "Gestion de projet" par "Au revoir ?" tmdqq
    Alors  le status de la dernière requète est 200 tmdqr
    Et la question d'identifiant 1 possède la description "Au revoir ?" tmdqs

  Scénario: Modifier la description d'une question appartenant a une module qu'on ne possède pas
    Quand Le professeur "Marie" veut modifier la description de la question d'identifiant 1 du questionnaire "Test1" du module "Gestion de projet" par "A midi ?" tmdqt
    Alors le dernier status de réponse est 400 tmdqu
    Et la question d'identifiant 1 possède toujours la description "Au revoir ?" tmdqv

  Scénario: Modifier le numéro d'une question appartenant a un module que l'on possède
    Quand le professeur "Jean" veut modifier le numéro de la question d'identifiant 1 du questionnaire "Test1" du module "Gestion de projet" par 2 tmdqw
    Alors le dernier status de réponse est 200 tmdqx
    Et le question d'identifiant 1 possède le numéro 2 tmdqy

  Scénario: Modifier le numéro d'une question appartenant a un module que l'on ne possède pas
    Quand le professeur "Marie" veut modifier le numéro de la question d'indentifiant 1 du questionnaire "Test1" du module "Gestion de projet" par 3 tmdqz
    Alors le dernier status de réponse et 400 tmdqaa
    Et la question d'identifiant 1 possède le numéro 2 tmdqab