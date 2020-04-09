package com.fr.codage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public class RSAActivity extends AppCompatActivity {

    /*
     * Chiffrement RSA -
     * 1. Choix de p et q : deux nombres premiers distincts
     *
     * 2. Calcul du produit p et q ===> n = p * q
     *
     * 3. Calcul de l'indicatrice d'Euler ===> phi(n) = (p - 1) * (q - 1)
     *
     * 4. Choix d'un entier naturel e < n
     *
     * 5. Calcul de d, inverse de e modulo phi(n)
     */

    // Variables qui servent au choix entre crypter ou déchiffrer un message
    public static final int ENCRYPT =  1;
    public static final int DECRYPT = -1;

    public EditText editTMessage, editTP, editTQ;
    public TextView result, detail;
    public static String txt, p, q;
    public static int intTxt, intP, intQ;
    public static StringBuilder trace = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_s_a);

        setTitle(getString(R.string.rsa));

        editTMessage = findViewById(R.id.getText);
        editTP = findViewById(R.id.getP);
        editTQ = findViewById(R.id.getQ);
        result = findViewById(R.id.result);
        detail = findViewById(R.id.detail);
    }

    // Quand on appuie sur le bouton crypter :
    // - On récupèrer les valeurs de la zone de texte message et de la clé si il ne sont pas vide
    // - Si c'est vide : on envoi une bulle d'info pour préciser de remplir tous les champs
    // - Sinon on lance la fonction RSA() avec en paramètre le message, le message clé et la méthode de cryptage qui est ici crypter
    public void crypter(View v){
        txt = editTMessage.getText().toString();
        p = editTP.getText().toString();
        q = editTQ.getText().toString();
        if(txt.matches("") || p.matches("") || q.matches("")){
            Toast.makeText(getApplicationContext(), getString(R.string.emptyField), Toast.LENGTH_LONG).show();
        }else if( !isPremier(Integer.parseInt(p))  ){
            Toast.makeText(getApplicationContext(), p+getString(R.string.notPrime), Toast.LENGTH_LONG).show();
        }else if( !isPremier(Integer.parseInt(q))  ){
            Toast.makeText(getApplicationContext(), q+getString(R.string.notPrime), Toast.LENGTH_LONG).show();
        }else{
            intTxt = Integer.parseInt(txt);
            intP = Integer.parseInt(p);
            intQ = Integer.parseInt(q);
            RSA(BigInteger.valueOf(intP), BigInteger.valueOf(intQ), BigInteger.valueOf(intTxt), ENCRYPT);
        }
    }

    // Quand on appuie sur le bouton decrypter :
    // - On récupèrer les valeurs de la zone de texte message et de la clé si il ne sont pas vide
    // - Si c'est vide : on envoi une bulle d'info pour préciser de remplir tous les champs
    // - Sinon on lance la fonction RSA() avec en paramètre le message, le message clé et la méthode de cryptage qui est ici decrypter
    public void decrypter(View v){
        txt = editTMessage.getText().toString();
        p = editTP.getText().toString();
        q = editTQ.getText().toString();
        if(txt.matches("") || p.matches("") || q.matches("")){
            Toast.makeText(getApplicationContext(), getString(R.string.emptyField), Toast.LENGTH_LONG).show();
        }else if( !isPremier(Integer.parseInt(p))  ){
            Toast.makeText(getApplicationContext(), p+getString(R.string.notPrime), Toast.LENGTH_LONG).show();
        }else if( !isPremier(Integer.parseInt(q))  ){
            Toast.makeText(getApplicationContext(), q+getString(R.string.notPrime), Toast.LENGTH_LONG).show();
        }else {
            intTxt = Integer.parseInt(txt);
            intP = Integer.parseInt(p);
            intQ = Integer.parseInt(q);
            RSA(BigInteger.valueOf(intP), BigInteger.valueOf(intQ), BigInteger.valueOf(intTxt), DECRYPT);
        }
    }

    // Fonction qui vérifie qu'un nombre est bien premier
    public boolean isPremier(int n) {
        if(n<=1) return false;
        for(int i = 2; i*i<=n; i++) {
            if (n%i ==0)
                return false;
            i++;
        }
        return true;
    }

    /* Fonction qui calcule le pgcd entre deux nombres
     *
     * Paramètres : deux entiers a et b
     * Cette fonction servira à déterminer "e" de la clé publique
     *
     * */
    public static int pgcd(int a, int b) {
        int r = 0;
        while(b != 0) {
            r = a%b;
            a = b;
            b = r;
        }
        return a;
    }

    /* Fonction qui calcule l'inverse de e modulo phi(n)
     *
     * Paramètres : deux entiers la clé publique "e" calculé précédemment et l'indicatrice d'Euler "phi-n"
     * Cette fonction sert à déterminer "d" de la clé privée
     *
     * */
    public static int inverMod(int e, int phi_n) {
        int r = 1, i = 2;
        while(r == 1) {
            i++;
            if ( i % 2 == 0 && i / 2 > 1)
                continue;
            else if ( i % 3 == 0 && i / 3 > 1)
                continue;
            else if ( i % 5 == 0 && i / 5 > 1)
                continue;
            else if ( i % 7 == 0 && i / 7>1)
                continue;
            int inverse = e*i%phi_n;
            if(inverse == 1) {
                r=0;
                return i;
            }
        }
        return 0;
    }

    /* Fonction qui permet de décomposer le message fourni en blocs inférieurs strictement à n
     *
     *
     * Paramètres : deux entiers le message "m" et "n" le produit de p et q"
     *
     *
     * */
    public static String[] bloc(int m, int n) {
        String res = Integer.toString(m);					// On convertit l'entier "m" en string
        ArrayList<String> l = new ArrayList();				// Liste qui sert à stocker la chaine de caractère

        if(m < n) {											// Si la valeur de "m" est inférieure à n alors on l'ajoute à la liste
            l.add(Integer.toString(m));
            return l.toArray(new String[1]);
        } else {											// Sinon on décale la fin de lecture de la chaine jusqu'à ce qu'on trouve un "m" plus petit que "n"
            int debut = 0;									// Index de début
            int fin = res.length();							// Index de fin

            while (debut != fin) {
                String index = res.substring(debut, fin);

                if(Integer.parseInt(index) < n) {			// Si la valeur de "m" est inférieure à "n"
                    l.add(res.substring(debut,fin));		// alors on ajoute à la liste le résultat correspondant aux positions de "debut" et "fin"
                    debut = fin;							// On place alors le "début" de lecture à la place de "fin"
                    fin = res.length();						// et la "fin" prend la nouvelle longueur de la chaine actuelle
                } else {
                    fin--;									// sinon on fait reculer "fin"
                }
            }
        }
        return l.toArray(new String[l.size()]);

    }

    /*
     * Fonctions "crypt" et "decrypt"
     *
     * Utilisation de BigInteger pour représenter les entiers sans limitations de taille
     * La classe BigInteger offre en plus des opérations habituelles sur les entiers,
     * des opérations de calcul en arithmétique modulaire, calcul du Pgcd, génération
     * de nombres premiers, test pour savoir si un entier est premier, etc...
     *
     *
     * Paramètres :
     * - le message   "m"
     * - la valeur de "e"
     * - la valeur de "n"
     *
     *
     */
    public static BigInteger crypt(BigInteger m, BigInteger e, BigInteger n) { return m.modPow(e, n); }
    public static BigInteger decrypt(BigInteger m, BigInteger d, BigInteger n) { return m.modPow(d, n); }

    /*
     * Fonction principale faisant appel aux fonctions précédentes
     */
    public void RSA(BigInteger p, BigInteger q, BigInteger m, int cryptingMethod) {
        trace.setLength(0);
        StringBuilder res = new StringBuilder();    // Variable qui contient le résultat du message

        BigInteger n = p.multiply(q);               // On multiplie p et q à l'aide la méthode multiply

        // Calcul de l'indicatrice d'Euler
        BigInteger phi_n = (p.subtract(new BigInteger("1")).multiply(q.subtract(new BigInteger("1")))  );

        trace.append("------------------- TRACE -------------------\n");
        trace.append("n = p x q = ").append(n).append("\n");
        trace.append("φ(n) = (p - 1) x (q - 1) = ").append(phi_n).append("\n");

        /*
         * On regarde qui entre p et q est plus grand
         * Pour la méthode Brute force
         */
        BigInteger e;
        if(p.intValue() > q.intValue()) {
            e = p.add(new BigInteger("1"));
        }else {
            e = q.add(new BigInteger("1"));
        }

        /*
         * Cacul du pgcd pour déterminer "e"
         */
        while (pgcd(phi_n.intValue(), e.intValue()) != 1 ) {
            e = e.add(new BigInteger("1"));
        }
        trace.append("e premier avec φ(n) = ").append(e).append("\n");

        int d = inverMod(e.intValue(), phi_n.intValue());   // On détermine "d" grâce la méthode d'Euclide étendu

        trace.append("d = ").append(inverMod(e.intValue(), phi_n.intValue())).append("\n");

        String[] maListe = bloc(m.intValue(),n.intValue()); // Liste qui contient le message décomposé en blocs inférieur à n

        trace.append("Bloc du message = ").append(Arrays.toString(bloc(m.intValue(), n.intValue()))).append("\n");
        trace.append("--------------------------------\n");

        /*
         * On parcourt la liste
         * Et on applique soit
         * - la méthode pour crypter le message 	: fonction crypt(s,e,n)
         * - la méthode pour décrypter le message 	: fonction decrypt(s,d,n)
         */
        for (String value : maListe) {
            BigInteger s = new BigInteger(value);
            if (cryptingMethod == 1) {
                res.append(crypt(s, e, n));
            } else {
                res.append(decrypt(s, BigInteger.valueOf(d), n));
            }
        }
        // Affichage du résultat et de la trace
        result.setText(res.toString());
        detail.setText(trace.toString());
    }

    // Réduit le clavier quand on touche autre chose qu'un champs à saisir
    public void closeKeyboard(View v) {
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // Fonction qui quand on appuie sur le texte résultat remplace la zone de texte du message par le message résultat
    public void getResultText(View v){
        editTMessage.setText(result.getText());
    }
}
