#language: fr
#encoding: utf-8

Fonctionnalité: Ajouté ou enlever un étudiant d'un module

  Contexte:
    Etant donné Un enseignant avec le nom de connexion "Jacques"
      Et un étudiant "Jean"
      Et un étudiant "Margaux"
      Et un étudiant "Franck"
      Et un module "Math" qui a un enseignant "Jacques"
      Et un module "Français" qui a un enseignant "Jacques" et un étudiant "Franck"
      Et l enseignant inscrit l'étudiant "Margaux" au module "Math"

  Scénario: Ajout d'un étudiant à un module
    Quand l'enseignant "Jacques" veut ajouter "Jean" au module de "Math" arm
    Alors le dernier status de réponse est 200 arm
    Et l'étudiant "Jean" est ajouter au module "Math" arm

  Scénario: Enlever un étudiant d'un module
    Quand l'enseignant "Jacques" veut enlever "Margaux" du module de "Math" arm
    Alors le dernier status de réponse est 200 arm
    Et "Margaux" est enlever du module "Math" arm

  Scénario: Ajout d'un étudiant appartenant déjà au module
    Quand l'enseignant "Jacques" veut ajouter "Franck" au module de "Français" arm
    Alors le dernier status de réponse est 400 arm
    Et "Franck" est déjà dans le module "Français" arm

  Scénario: Enlever un étudiant qui n'est pas dans le module
    Quand l'enseignant "Jacques" veut enlever "Jean" du module de "Math" arm
    Alors le dernier status de réponse est 400 arm
    Et "Jean" n'est pas dans le module "Math" arm