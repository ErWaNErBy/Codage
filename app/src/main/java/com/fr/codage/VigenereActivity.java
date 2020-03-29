package com.fr.codage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;

public class VigenereActivity extends AppCompatActivity {

    public static final int ENCRYPT =  1;
    public static final int DECRYPT = -1;
    public EditText editTMessage, editTCle;
    public TextView result;
    public static String txt, cle;

    // Liste des caractères étendus sont forme d'hexa
    public static final char[] EXTENDED = {         '\u00C7', '\u00FC', '\u00E9', '\u00E2',
            '\u00E4', '\u00E0', '\u00E5', '\u00E7', '\u00EA', '\u00EB', '\u00E8', '\u00EF',
            '\u00EE', '\u00EC', '\u00C4', '\u00C5', '\u00C9', '\u00E6', '\u00C6', '\u00F4',
            '\u00F6', '\u00F2', '\u00FB', '\u00F9', '\u00FF', '\u00D6', '\u00DC', '\u00A2',
            '\u00A3', '\u00A5', '\u20A7', '\u0192', '\u00E1', '\u00ED', '\u00F3', '\u00FA',
            '\u00F1', '\u00D1', '\u00AA', '\u00BA', '\u00BF', '\u2310', '\u00AC', '\u00BD',
            '\u00BC', '\u00A1', '\u00AB', '\u00BB', '\u2591', '\u2592', '\u2593', '\u2502',
            '\u2524', '\u2561', '\u2562', '\u2556', '\u2555', '\u2563', '\u2551', '\u2557',
            '\u255D', '\u255C', '\u255B', '\u2510', '\u2514', '\u2534', '\u252C', '\u251C',
            '\u2500', '\u253C', '\u255E', '\u255F', '\u255A', '\u2554', '\u2569', '\u2566',
            '\u2560', '\u2550', '\u256C', '\u2567', '\u2568', '\u2564', '\u2565', '\u2559',
            '\u2558', '\u2552', '\u2553', '\u256B', '\u256A', '\u2518', '\u250C', '\u2588',
            '\u2584', '\u258C', '\u2590', '\u2580', '\u03B1', '\u00DF', '\u0393', '\u03C0',
            '\u03A3', '\u03C3', '\u00B5', '\u03C4', '\u03A6', '\u0398', '\u03A9', '\u03B4',
            '\u221E', '\u03C6', '\u03B5', '\u2229', '\u2261', '\u00B1', '\u2265', '\u2264',
            '\u2320', '\u2321', '\u00F7', '\u2248', '\u00B0', '\u2219', '\u00B7', '\u221A',
            '\u207F', '\u00B2', '\u25A0', '\u00A0' };

    // Liste des décimaux des caractères étendus
    public static final int[] DECIMALS = {     199,   252,   233,   226,
            228,   224,   229,   231,   234,   235,   232,   239,   238,
            236,   196,   197,   201,   230,   198,   244,   246,   242,
            251,   249,   255,   214,   220,   162,   163,   165,  8359,
            402,   225,   237,   243,   250,   241,   209,   170,   186,
            191,  8976,   172,   189,   188,   161,   171,   187,  9617,
            9618, 9619,  9474,  9508,  9569,  9570,  9558,  9557,  9571,
            9553, 9559,  9565,  9564,  9563,  9488,  9492,  9524,  9516,
            9500, 9472,  9532,  9566,  9567,  9562,  9556,  9577,  9574,
            9568, 9552,  9580,  9575,  9576,  9572,  9573,  9561,  9560,
            9554, 9555,  9579,  9578,  9496,  9484,  9608,  9604,  9612,
            9616, 9600,   945,   223,   915,   960,   931,   963,   181,
            964,  934,   920,   937,   948,  8734,   966,   949,  8745,
            8801,  177,  8805,  8804,  8992,  8993,   247,  8776,   176,
            8729,  183,  8730,  8319,   178,  9632,   160 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vigenere);

        setTitle(getString(R.string.vigenère));

        editTMessage = findViewById(R.id.getText);
        editTCle = findViewById(R.id.getCle);
        result = findViewById(R.id.result);
    }

    // Réduit le clavier quand on touche autre chose qu'un champs à saisir
    public void closeKeyboard(View v) {
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // Quand on appuie sur le bouton crypter :
    // - On récupèrer les valeurs de la zone de texte message et de la clé si il ne sont pas vide
    // - Si c'est vide : on envoi une bulle d'info pour préciser de remplir tous les champs
    // - Sinon on lance la fonction vigenere() avec en paramètre le message, le message clé et la méthode de cryptage qui est ici crypter
    public void crypter(View v){
        txt = editTMessage.getText().toString();
        cle = editTCle.getText().toString();
        if(txt.matches("") || cle.matches("")){
            Toast.makeText(getApplicationContext(), getString(R.string.emptyField), Toast.LENGTH_LONG).show();
        }else{
            vigenere(txt, cle, ENCRYPT);
        }
    }

    // Quand on appuie sur le bouton decrypter :
    // - On récupèrer les valeurs de la zone de texte message et de la clé si il ne sont pas vide
    // - Si c'est vide : on envoi une bulle d'info pour préciser de remplir tous les champs
    // - Sinon on lance la fonction vigenere() avec en paramètre le message, le message clé et la méthode de cryptage qui est ici decrypter
    public void decrypter(View v){
        txt = editTMessage.getText().toString();
        cle = editTCle.getText().toString();
        if(txt.matches("") || cle.matches("")){
            Toast.makeText(getApplicationContext(), getString(R.string.emptyField), Toast.LENGTH_LONG).show();
        }else{
            vigenere(txt, cle, DECRYPT);
        }
    }

    // Fonction qui crypte ou décrypte avec la méthode de cryptage Vigenère et qui prend en paramètre un message à crypter/décrypter et un message clé
    public void vigenere(String mess, String key, int cryptingMethod){
        StringBuilder messEncrypt = new StringBuilder();	// Message qui contiendra le cryptage

        List<Integer> list = new ArrayList<>();             // list qui conteindra les décimaux des caractères du message clé
        for(int j = 0; j < key.length(); j++) {             // Analyse chaque caractère du message clé saisi
            int codePointKey = key.codePointAt( j );        // On récupère le décimal du caractère du message clé
            // Si le décimal du caractère est supérieur à 127 (c'est-à-dire, si c'est un caractère de la table ASCII étendue),on éxécute getExtendChar() avec le décimal du caratère et récupère le bon décimal
            if(codePointKey > 127)  codePointKey = getExtendChar(codePointKey);
            if(j<=key.length()-7) {                 // Si il reste encore 7 cactères avant la fin du message (c'est_à-dire, qu'il y a possibilité que ça soit un caractère hexa)
                String i0 = String.valueOf(key.charAt(j));
                String i1 = String.valueOf(key.charAt(j+1));
                String i2 = String.valueOf(key.charAt(j+2));
                if(i0.equals("\\") && i1.equals("0") && i2.equals("x")) {   // Si les 3 valeurs à partir de i du message contient bien : \0x   ..
                    String hexaCode = String.format("%s%s%s%s", key.charAt(j+3), key.charAt(j+4), key.charAt(j+5), key.charAt(j+6));    // On récupère les 4 caractères hexa après le \0x
                    codePointKey = Integer.parseInt(hexaCode,16);   // On change le décimal avec la nouvelle valeur (on converti l'hexa en décimal)
                    j=j+6;  // On passe au caractère suivant
                }
            }
            list.add(codePointKey); // On ajoute dans la liste le décimal du caractère du message clé
        }

        for (int i = 0; i < mess.length(); i++) {             // Analyse chaque caractère du message clé saisi

            int codePointMess = mess.codePointAt(i);    // Décimal du caractère à la position i du message saisi

            // [ Verifie si la message ne contient pas de caractère hexa de la forme \0x****
            if(i<=mess.length()-7) {    // Si il reste encore 7 cactères avant la fin du message (c'est_à-dire, qu'il y a possibilité que ça soit un caractère hexa)
                String i0 = String.valueOf(mess.charAt(i));
                String i1 = String.valueOf(mess.charAt(i+1));
                String i2 = String.valueOf(mess.charAt(i+2));
                if(i0.equals("\\") && i1.equals("0") && i2.equals("x")) {   // Si les 3 valeurs à partir de i du message contient bien : \0x   ..
                    String hexaCode = String.format("%s%s%s%s", mess.charAt(i+3), mess.charAt(i+4), mess.charAt(i+5), mess.charAt(i+6));    // On récupère les 4 caractères hexa après le \0x
                    codePointMess = Integer.parseInt(hexaCode,16);  // On change le décimal avec la nouvelle valeur (on converti l'hexa en décimal)
                    i=i+6;  // On passe au caractère suivant
                }
            }
            // ]

            // ---------------------------------- RECUPERATION DU CODE DECIMAL

            // Si le décimal du caractère est supérieur à 127 (c'est-à-dire, si c'est un caractère de la table ASCII étendue),on éxécute getExtendChar() avec le décimal du caratère et récupère le bon décimal
            if(codePointMess > 127)  codePointMess = getExtendChar(codePointMess);

            int codePointKey = list.get(i%list.size());     // On récuère le décimal du caractère du message clé (on bouclera dans cette liste tant que on lis un caractère du message à crypter / décrypter)

            // On ajoute le décimal du méssage à cryter / décrypter avec le décimal du massage clé (si la méthode de cryptage est ENCRYPT le décimal du message clé est positif sinon si c'est DECRYPT il est négatif)
            int codePointDecal = codePointMess + cryptingMethod * codePointKey;

            if (codePointDecal > 255) codePointDecal = codePointDecal%256;  // Si le décimal décalé est supérieur à 255 (c'est-à-dire si il fait une boucle) on récupère le reste de la division pas 256
            if (codePointDecal < 0) codePointDecal = 256+codePointDecal;    // Si la décimal décalé est inférieur à 0 on change la valeur pour qu'il part de la fin de la table ASCII étendue
            //System.out.println("codePointMess :" + codePointMess + " codePointKey : " + codePointKey+" codePointDecal : " + codePointDecal);

            // ---------------------------------- AFFICHAGE

            messEncrypt.append(getCharacterByDecimal(codePointDecal));  // On éxécute la fonction getCharacterByDecimal() avec le décimal du caractère décalé qui retounera le caractère correspondant
        }
        result.setText(messEncrypt.toString());     // Affiche le message crypter / décrypter dans la zone de texte result
    }

    // Fontion qui prend en paramètre un code décimal et qui retourne le bon caractère qui le correspond
    public static String getCharacterByDecimal(int code) {
        StringBuilder chararter = new StringBuilder();	    // String qui contiendra le caractère correspondant au code décimal
        if( code > 127 ) {                                  // Si le décimal du caractère est supérieur à 127 (c'est-à-dire, si c'est un caractère de la table ASCII étendue) ..
            chararter.append( EXTENDED[code - 128] );       // On récuprère le caractère correspondant dans la table EXTENDED
        } else {                                                    // Sinon (c'est un caractère de la table ASCII normal) ..
            String normalCharacter = Character.toString( (char) (code) );                                          // On récupère le caractère normal avec son décimal
            String hexaOfNormalChar = "\\0x"+ Integer.toHexString( code | 0x10000).substring(1);                // On récupère l'hexa du caractère normal ( de la forme \0x**** )
            chararter.append(normalCharacter.replaceAll("\\p{C}", "\\"+hexaOfNormalChar) );     // On ajoute dans le message résultat le caracère normal de la table ASCII normal et si il n'est pas affichable on ajoute à la place son hexa
        }
        return chararter.toString();                        // Retourne le caractère correspondant sous forme de String
    }

    // Fonction qui compare le décimal du caratère avec la liste de décimal des caractères étendus et :
    // -- si les déicmaux sont identique, retoune le bon décimal du caractère correspondant de la table ASCII étendue
    private static int getExtendChar(int code) {
        for(int i=0; i<DECIMALS.length; i++) {
            if(DECIMALS[i] == code) {
                return 128+i;
            }
        }
        return 0;
    }

    // Fonction qui quand on appuie sur le texte résultat remplace la zone de texte du message par le message résultat
    public void getResultText(View v){
        editTMessage.setText(result.getText());
    }

}
