# language: fr
# encoding: utf-8

Fonctionnalité: En tant qu'élève je veux pouvoir valider un questionnaire

  Contexte:
    Etant donné un élève "Antoine" ayant pris le cours "Génie Logiciel"
    Et un élève "Léa" n'ayant pas le cours "Génie Logiciel"
    Et un questionnaire rempli "Test" appartenant au cours "Génie Locigiel"

    Scénario: L'élève valide un questionnaire
      Quand l'élève "Antoine" valide un questionnaire
      Alors le status de la dernière requète est 200 vq1
      Et le questionnaire est validé vq1

  Scénario: L'élève valide un questionnaire
    Quand l'élève "Léa" valide un questionnaire
    Alors le status de la dernière requète est 400 vq2
    Et le questionnaire n'est pas validé vq2
