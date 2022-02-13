#language: fr
#encoding: utf-8

Fonctionnalité: Accéder aux questionnaires
  Contexte:
    Etant donné le professeur "Andrew" assigné au module d'"Histoire" aaq
    Et le module "Histoire" a un questionnaire "Partiel" invisible aaq
    Et le module "Histoire" a un questionnaire "Interro" visible aaq
    Et le professeur "Marco" qui n'a aucun module aaq
    Et l'élève "Pedro" est assigné au module "Histoire" aaq
    Et l'élève "Madeleine" assigné a aucun module aaq

  Scénario: Le professeur "Andrew" récupère le questionnaire "Partiel" du module "Histoire"
    Quand l'utilisateur "Andrew" récupère le questionnaire "Partiel" du module "Histoire"
    Alors la réponse recue est 200
    Et le questionnaire "Partiel" est renvoyé
    Et les tables sont videes

  Scénario: Le professeur "Marco" essaye de récupérer le questionnaire "Interro" du module "Histoire"
    Quand l'utilisateur "Marco" récupère le questionnaire "Partiel" du module "Histoire"
    Alors la réponse recue est 400
    Et les tables sont videes

  Scénario: L'étudiant "Pedro" essaye de récupérer le questionnaire "Partiel" du module "Histoire"
    Quand l'utilisateur "Pedro" récupère le questionnaire "Partiel" du module "Histoire"
    Alors la réponse recue est 400
    Et les tables sont videes

  Scénario: L'étudiant "Pedro" essaye de récupérer le questionnaire "Interro" du module "Histoire"
    Quand l'utilisateur "Pedro" récupère le questionnaire "Interro" du module "Histoire"
    Alors la réponse recue est 200
    Et le questionnaire "Interro" est renvoyé
    Et les tables sont videes

  Scénario: L'étudiant "Madeleine" essaye de récupérer le questionnaire "Interro" du module "Histoire"
    Quand l'utilisateur "Madeleine" récupère le questionnaire "Interro" du module "Histoire"
    Alors la réponse recue est 400
    Et les tables sont videes
