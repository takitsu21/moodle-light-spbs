#language: fr
#encoding: utf-8

Fonctionnalité: En tant qu'enseignant je veux pouvoir ajouter et retirer un cours (série de texte) à un module

  Contexte:
    Etant donné le professeur "Jacques" assigne au module "Philo"
      Et un module "SVT" qui a un cours "Test" et numéro 2 et qui a un enseignant "Jacques" crs

  Scénario: Ajout d'une ressource cours d'un module
    Quand "Jacques" veut ajouter le cours "TestPhilo" qui a pour description "description" et numéro 1 au module "Philo" crs
    Alors le code de retour est 200
    Et "TestPhilo" est ajouté au module "Philo" crs
    Et les tables sont videes

  Scénario: Suppression d'une ressource cours d'un module
    Quand "Jacques" veut supprimer le cours "Test" et numéro 2 du module "SVT" crs
    Alors le code de retour est 200
    Et "Test" est supprimé du module "SVT" crs
    Et les tables sont videes

  Scénario: Ajout d'une ressource cours d'un module qui contient déjà ce cours
    Quand "Jacques" veut ajouter le cours "Test" et numéro 2 au module "SVT" crs
    Alors le code de retour est 400
    Et "Test" n'est pas ajouté au module "SVT" crs
    Et les tables sont videes

  Scénario: Suppression d'une ressource cours d'un module qui ne contient pas ce cours
    Quand "Jacques" veut supprimer le cours "Test2" et numéro 3 du module "SVT" crs
    Alors le code de retour est 400
    Et "Test2" n'est pas supprimé du module "SVT" crs
    Et les tables sont videes
