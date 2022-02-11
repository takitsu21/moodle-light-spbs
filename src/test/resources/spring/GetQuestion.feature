#language: fr
#encoding: utf-8
  
Fonctionnalité: Accéder à une question
  
  Contexte:
    Etant donné le questionnaire "Interrogation" dans le module "Physique" auq
    Et l'étudiant "Charlie" dans le module "Physique" auq
    Et l'étudiant "Tanguy" sans module auq
    Et la question "Bonus" numéro 13 dans le questionnaire "Interrogation" auq
    
  Scénario: l'étudiant "Charlie" récupère la question "Bonus"
    Quand l'étudiant "Charlie" récupère la question "Bonus" du questionnaire "Interrogation" du module "Physique" auq
    Alors la réponse est 200 auq
    Et la question "Bonus" en renvoyée auq
    
  Scénario: l'étudiant "Tanguy" récupère la question "Bonus"
    Quand l'étudiant "Tanguy" récupère la question "Bonus" du questionnaire "Interrogation" du module "Physique" auq
    Alors la réponse est 400 auq