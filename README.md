<h1>  Compte-rendu Traveaux Pratiques</h1>
# Explication de QTQ

Quel Tram Quand(QTQ) est une application pour connaitre l'horaire du passage des bus/tram de la CTS à l'arret choisie.
L'application accèdent à l'aide de requète à la base de donnée en temps réel de la cts afin de trouvé les bus/trams souhaité.


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

Le String.xml a par exemple été traduit en Anglais et en Breton pour assurer la bonne compréhension de l'application a tous les utlisateurs! 
(Même si les utilisateurs breton du raison CTS sont peu nombreux)
## Les Toasts 

Les toasts sont des messages en bas de l'écran permettant de transmettre des informations a l'utilisateur .
Un Toast notifie l'utilisateur que l'arret qu'il a tapé à bien été enregistrer comme favori. 

```
    Toast.MakeText(this,"Data Saved",Toast.LENGTH_SHORT).show()
```



## Les Boutons 

Le bouton permet de sauvegarder les données et de les transmettres dans une autre activité à l'aide d'un Intent 


```
    Intent activite_res = new Intent(getApplicationContext(),ResultActivity
    activite_res.putExtra("name",((EditText)findViewById(R.id.id_editText)).getText().toString());
    startActivity(activite_res);
        finish();
```


## Cycle de vie


- Au lancement de l'application Android appel les méthodes `onCreate`, `onStart` et `onResume`.
- A la fermeture de l'application Android appel les méthodes `onPause`,`onStop` et `onDestroy`.
- Quand l'application est passer au second plan Android appel la méthode `onPause`



## SharedPreferences

L'on crée un Preference Manager que les attaches a l'activité avant de crée un éditeur.
``` 
 mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

```
L'on édite l'éditeur afin qu'il sauvegarde les données inscrit dans R.string.EditTextStop. 
Il ne faut surtout pas oublier de apply() afin que la sauvegarde se face.
```
String name= etxt.getText().toString();
                mEditor.putString(getString(R.string.EditTextStop),name);
                mEditor.apply();

```



# TP2 : Fragment, NavigationDrawer

## NavigationDrawer

Le NavigationDrawer est composé d'un **DrawerLayout** qui représente la vue principale 
en contenant les sous-vues comme le **FrameLayout** et la **Toolbar**. 
La **NavigationView** représente le menu de l'application. Elle permet d'acceder à l'ensemble de Pages/Fragments


## Fragments 

Un **Fragment** est une view permettant d'afficher certaine information precise. 
Pour crée un Fragment il faut :
```
Fragment fragment = new myFragment();
FragmentManager fManager = getSupportFragmentManager();
FragmentTrasaction fTransaction = fManager.beginTransaction();
fTransaction.replace(R.id.myLayout,fragment).addToBackStack(null);
fTransaction.commit();
```


L'interret des fragments est de pouvoir communiquer entre eux et avec les activités à l'aide
d'un **onFragmentInteractionListener** 

```java
@Override
public void onFragmentInteraction(int dest , String text) {
    NavController navController = Navigation.findNavController(this,R.id.fraghost);
    Bundle args = new Bundle();
    args.putString("Ktext",text);
    if(destination == 1) {
        navController.navigate(R.id.frag1, args);
    } else {
        navController.navigate(R.id.frag2, args);
    }
}
```
 














Le RecyclerView permet d'afficher une certaine quantité d'information tout en limitant la mémoire consommé. 
Toutes information qui disparait de l'écran (en scrollant) est décharger et réutiliser pour afficher les inforamtions suivante.
Le NavigationDrawer permet de ne pas charger toutes les données d'un coups, seul les informations afficher sont chargées. 


Le ViewHolder 
```
public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
            LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.affiche_bus,parent,false);
            return new MyViewHolder(view);
        }
```
