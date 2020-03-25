<h1>  Compte-rendu Traveaux Pratiques</h1>

# TP1 : Prise en main du framework

# Objectif du TP

Compléter un formulaire et sauvegarder les données entrées


## Fonctionnement

Créeation de 2 classe :
    
- `MainActivity`: Activité comportant l'essentiel des vues de l'application comme
        les **EditTexts** et les **Boutons** 
-`ResultActivity` : Deuxième activité avec pour but d'afficher les informations entré
        dans la première activité à l'aide de **TextView**

## Internationnalisation 
Chaque text inscrit dans une vue est géré pour le fichier String.xml,
l'on peut rajouter d'autre language afin de traduire les textes en fonction de la langue du téléphone.
L'application reconnaitra le language du téléphone lors de son lancement afin d'afficher le language adéquat. 

## Les Toasts 

Les toasts sont des messages en bas de l'écran permettant de transmettre des informations a l'utilisateur .

`
    this,"Data Saved",Toast.LENGTH_SHORT).show()
`


## Les Boutons 

Le bouton permet de sauvegarder les données et de les transmettres dans une autre activité à l'aide d'un Intent 


`  
    Intent activite_res = new Intent(getApplicationContext(),ResultActivity
    
`






