#language: fr
#encoding: utf-8

Fonctionnalité: En tant qu'utilisateur je veux pouvoir voir les modules ou je suis assigné

  Contexte:
    Etant donné le professeur "Paul" assigne au module "Maths"
    Et le professeur "Paul" assigne au module "Chimie"
    Et le professeur "Jean2"
    Et l'etudiant "Juliette" assigne au module "Maths"
    Et l'etudiant "Mia"

  Scénario: Le professeur "Paul" get ses modules
    Quand L'utilisateur "Paul" get ses modules
    Et le dernier status de request est 200 gm
    Alors les modules sont "Maths" et "Chimie"
    Et les tables sont videes

  Scénario: Le professeur "Jean" get ses modules
    Quand L'utilisateur "Jean2" get ses modules
    Et le dernier status de request est 200 gm
    Alors il n'y a pas de module
    Et les tables sont videes

  Scénario: L'élève "Juliette" get ses modules
    Quand L'utilisateur "Juliette" get ses modules
    Et le dernier status de request est 200 gm
    Alors le module est "Maths"
    Et les tables sont videes

  Scénario: L'élève "Mia" get ses modules
    Quand L'utilisateur "Mia" get ses modules
    Et le dernier status de request est 200 gm
    Alors il n'y a pas de module
    Et les tables sont videes
