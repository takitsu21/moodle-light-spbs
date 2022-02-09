#language: fr
#encoding: utf-8

Fonctionnalité: Gérer les cours d'un module

Contexte:
  Etant donné Un enseignant avec le nom de connexion "Jacques" crs
    Et un module "Math" qui a un enseignant "Jacques" crs
    Et un module "Français" qui a un cours "Test" et qui a un enseignant "Jacques" crs

  Scénario: Ajout d'une ressource cours d'un module
    Quand "Jacques" veut ajouter le cours "Types" qui a pour description "description" au module "Math" crs
    Alors le dernier status de réponse est 200 crs
    Et "Types" est ajouté au module "Math" crs

  Scénario: Suppresion d'une ressource cours d'un module
    Quand "Jacques" veut supprimer le cours "Test" du module "Français" crs
    Alors le dernier status de réponse est 200 crs
    Et "Test" est supprimé du module "Français" crs

  Scénario: Ajout d'une ressource cours d'un module qui contient déjà ce cours
    Quand "Jacques" veut ajouter le cours "Test" au module "Français" crs
    Alors le dernier status de réponse est 400 crs
    Et "Test" n'est pas ajouté au module "Français" crs

  Scénario: Suppresion d'une ressource cours d'un module qui ne contient pas ce cours
    Quand "Jacques" veut supprimer le cours "Test2" du module "Français" crs
    Alors le dernier status de réponse est 400 crs
    Et "Test2" n'est pas supprimé du module "Français" crs