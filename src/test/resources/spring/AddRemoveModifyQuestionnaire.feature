#language: fr
#encoding: utf-8

Fonctionnalité: Ajouter ou enlever une ressource d'un module

  Contexte:
    Étant donné le professeur "Jacob" assigne au module "Mathématiques"
    Et le questionnaire "Examen" du module "Mathématiques"
    Et un module "Philosophie"

  Scénario: Ajout d'un questionnaire par un enseignant autorisé
    Quand l'enseignant "Jacob" veut ajouter le questionnaire "Premier questionnaire" au module "Mathématiques"
    Alors la réponse est 200
    Et le questionnaire "Premier questionnaire" est dans le module "Mathématiques"
    Et les tables sont videes

  Scénario: Ajout d'un questionnaire par un enseignant non autorisé
    Quand l'enseignant "Jacob" veut ajouter le questionnaire "Premier questionnaire" au module "Philosophie"
    Alors la réponse est 400
    Et le questionnaire "Premier questionnaire" n'est pas dans le module "Philosophie"
    Et les tables sont videes

  Scénario: Suppression d'un questionnaire dans un module par un enseignant autorisé
    Quand l'enseignant "Jacob" veut supprimer le questionnaire "Examen" du module "Mathématiques"
    Alors la réponse est 200
    Et le questionnaire "Examen" n'est pas dans le module "Mathématiques"
    Et les tables sont videes

  Scénario: Suppression illégale d'un questionnaire d'un module par un enseignant n'appartenant pas au module
    Etant donné que le questionnaire "Examen" soit dans le module "Philosophie"
    Quand l'enseignant "Jacob" veut supprimer le questionnaire "Examen" du module "Philosophie"
    Alors la réponse est 400
    Et le questionnaire "Examen" est dans le module "Philosophie"
    Et les tables sont videes
