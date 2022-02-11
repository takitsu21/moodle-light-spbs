#language: fr
#encoding: utf-8

Fonctionnalité: En tant qu'utilisateur je veux pouvoir voir les modules ou je suis assigné

  Contexte:
    Etant donné le professeur "Paul" assigné au module de "Maths" gm
    Et le professeur "Paul" assigné au module de "Chimie" gm
    Et le professeur "Jean2" qui n'a aucun module gm
    Et l'élève "Juliette" est assigné au module "Maths" gm
    Et l'élève "Mia" assigné a aucun module gm

  Scénario: Le professeur "Paul" get ces modules
    Quand L'utilisateur "Paul" get ces modules
    Et le dernier status de request est 200 gm
    Alors les modules sont "Maths" et "Chimie"

  Scénario: Le professeur "Jean" get ces modules
    Quand L'utilisateur "Jean2" get ces modules
    Et le dernier status de request est 200 gm
    Alors il n'y a pas de module

  Scénario: L'élève "Juliette" get ces modules
    Quand L'utilisateur "Juliette" get ces modules
    Et le dernier status de request est 200 gm
    Alors le module est "Maths"

  Scénario: L'élève "Mia" get ces modules
    Quand L'utilisateur "Mia" get ces modules
    Et le dernier status de request est 200 gm
    Alors il n'y a pas de module



