# language: fr
# encoding: utf-8

Fonctionnalité: En tant qu'élève je veux pouvoir valider un questionnaire

  Contexte:
    Etant donné un élève "Antoine"
    Et un questionnaire rempli "Test"

    Scénario: L'élève valide un questionnaire
      Quand l'élève "Antoine" valide un questionnaire
      Alors le questionnaire est validé
