#language: fr
#encoding: utf-8

Fonctionnalité: Accéder aux questionnaires
  Contexte:
    Etant donné le professeur "Andrew" assigné au module d'"Histoire" aaq
    Et le module "Histoire" a un questionnaire "Partiel" invisible aaq
    Et le module "Histoire" a un questionnaire "Interro" visible aaq
    Et le professeur "Marco" qui n'a aucun module aaq
    Et l'élève "Pedro" est assigné au cours "Histoire" aaq
    Et l'élève "Madeleine" assigné a aucun module aaq

  Scénario: Le professeur "Andrew" récupère le questionnaire "Partiel" du module "Histoire"
    Quand le professeur "Andrew" récupère le questionnaire "Partiel" du module "Histoire"
    Alors la réponse recue est 200
    Et le questionnaire "Partiel" est renvoyé

  Scénario: Le professeur "Marco" essaye de récupérer le questionnaire "Interro" du module "Histoire"
    Quand le professeur "Andrew" récupère le questionnaire "Partiel" du module "Histoire"
    Alors la réponse recue est 400

  Scénario: L'étudiant "Pedro" essaye de récupérer le questionnaire "Partiel" du module "Histoire"
    Quand l'étudiant "Pedro" récupère le questionnaire "Partiel" du module "Histoire"
    Alors la réponse recue est 400

  Scénario: L'étudiant "Pedro" essaye de récupérer le questionnaire "Interro" du module "Histoire"
    Quand l'étudiant "Pedro" récupère le questionnaire "Interro" du module "Histoire"
    Alors la réponse recue est 200
    Et le questionnaire "Interro" est renvoyé

  Scénario: L'étudiant "Madeleine" essaye de récupérer le questionnaire "Interro" du module "Histoire"
    Quand l'étudiant "Pedro" récupère le questionnaire "Interro" du module "Histoire"
    Alors la réponse recue est 400
