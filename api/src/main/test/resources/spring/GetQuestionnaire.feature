#language: fr
#encoding: utf-8

Fonctionnalité: Accéder aux questionnaires
  Contexte:
    Etant donné le professeur "Andrew" assigne au module "Histoire"
    #invisible de base
    Et le questionnaire "Partiel" dans le module "Histoire"
    Et le questionnaire "Interro" dans le module "Histoire"
    Et le questionnaire "Interro" du module "Histoire" est visible
    Et le professeur "Marco"
    Et l'etudiant "Pedro" assigne au module "Histoire"
    Et l'etudiant "Madeleine"

  Scénario: Le professeur "Andrew" récupère le questionnaire "Partiel" du module "Histoire"
    Quand l'utilisateur "Andrew" récupère le questionnaire "Partiel" du module "Histoire"
    Alors le code de retour est 200
    Et le questionnaire "Partiel" est renvoyé
    Et les tables sont videes

  Scénario: Le professeur "Marco" essaye de récupérer le questionnaire "Interro" du module "Histoire"
    Quand l'utilisateur "Marco" récupère le questionnaire "Partiel" du module "Histoire"
    Alors le code de retour est 400
    Et les tables sont videes

  Scénario: L'étudiant "Pedro" essaye de récupérer le questionnaire "Partiel" du module "Histoire"
    Quand l'utilisateur "Pedro" récupère le questionnaire "Partiel" du module "Histoire"
    Alors le code de retour est 400
    Et les tables sont videes

  Scénario: L'étudiant "Pedro" essaye de récupérer le questionnaire "Interro" du module "Histoire"
    Quand l'utilisateur "Pedro" récupère le questionnaire "Interro" du module "Histoire"
    Alors le code de retour est 200
    Et le questionnaire "Interro" est renvoyé
    Et les tables sont videes

  Scénario: L'étudiant "Madeleine" essaye de récupérer le questionnaire "Interro" du module "Histoire"
    Quand l'utilisateur "Madeleine" récupère le questionnaire "Interro" du module "Histoire"
    Alors le code de retour est 400
    Et les tables sont videes
