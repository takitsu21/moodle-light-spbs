#language: fr
#encoding: utf-8

Fonctionnalité: En tant qu'utilisateur je veux pouvoir voir les ressources d'un module

  Contexte:
    Etant donné le professeur "Paul" assigne au module "Maths"
    Et le module "Maths" a un cours "Pomme" invisible gr
    Et le module "Maths" a un cours "Poir" visible gr
    Et le professeur "Jean"
    Et l'etudiant "Juliette" assigne au module "Maths"
    Et l'etudiant "Mia"

  Scénario: Le professeur "Paul" get les ressources du module "Maths"
    Quand Le professeur "Paul" get les ressources du module "Maths"
    Et le dernier status de request est 200 gr
    Alors les cours "Poir" et "Pomme" sont renvoyé
    Et les tables sont videes

  Scénario: Le professeur "Juliette" get les ressources du module "Maths"
    Quand Le professeur "Juliette" get les ressources du module "Maths"
    Et le dernier status de request est 200 gr
    Alors le cours "Poir" est renvoyé
    Et les tables sont videes

  Scénario: Le professeur "Jean" get les ressources du module "Maths"
    Quand Le professeur "Jean" get les ressources du module "Maths"
    Et le dernier status de request est 400 gr
    Alors aucun cours n'est renvoyé
    Et les tables sont videes

  Scénario: Le professeur "Mia" get les ressources du module "Maths"
    Quand Le professeur "Mia" get les ressources du module "Maths"
    Et le code de retour est 400
    Alors aucun cours n'est renvoyé
    Et les tables sont videes

