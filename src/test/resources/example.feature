#language: fr
#encoding: utf-8

Fonctionnalité: Changement de saison

  Contexte:
    Etant donné un "joueur" veux pouvoir changer de saison


  Scénario: Passage de la saison hiver à printemps
    Quand le curseur est à la case 2 de saison "hiver"
    Et lance le dé qui tombe sur une face a 1 points pour avancer dans la saison
    Alors il passe à la saison "printemps"