package com.fr.codage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CesarActivity extends AppCompatActivity {

    public EditText editTMessage, editTDecalage;
    public TextView result;
    public static String txt, cle;
    public static int key, codePointDecal;

    // Liste des caractères étendus sont forme d'hexa
    public static final String[] EXTENDEDS = {      "0x00c7", "0x00fc", "0x00e9", "0x00e2",
            "0x00e4", "0x00e0", "0x00e5", "0x00e7", "0x00ea", "0x00eb", "0x00e8", "0x00ef",
            "0x00ee", "0x00ec", "0x00c4", "0x00c5", "0x00c9", "0x00e6", "0x00c6", "0x00f4",
            "0x00f6", "0x00f2", "0x00fb", "0x00f9", "0x00ff", "0x00d6", "0x00dc", "0x00a2",
            "0x00a3", "0x00a5", "0x20a7", "0x0192", "0x00e1", "0x00ed", "0x00f3", "0x00fa",
            "0x00f1", "0x00d1", "0x00aa", "0x00ba", "0x00bf", "0x2310", "0x00ac", "0x00bd",
            "0x00bc", "0x00a1", "0x00ab", "0x00bb", "0x2591", "0x2592", "0x2593", "0x2502",
            "0x2524", "0x2561", "0x2562", "0x2556", "0x2555", "0x2563", "0x2551", "0x2557",
            "0x255d", "0x255c", "0x255b", "0x2510", "0x2514", "0x2534", "0x252c", "0x251c",
            "0x2500", "0x253c", "0x255e", "0x255f", "0x255a", "0x2554", "0x2569", "0x2566",
            "0x2560", "0x2550", "0x256c", "0x2567", "0x2568", "0x2564", "0x2565", "0x2559",
            "0x2558", "0x2552", "0x2553", "0x256b", "0x256a", "0x2518", "0x250c", "0x2588",
            "0x2584", "0x258c", "0x2590", "0x2580", "0x03b1", "0x00df", "0x0393", "0x03c0",
            "0x03a3", "0x03c3", "0x00b5", "0x03c4", "0x03a6", "0x0398", "0x03a9", "0x03b4",
            "0x221e", "0x03c6", "0x03b5", "0x2229", "0x2261", "0x00b1", "0x2265", "0x2264",
            "0x2320", "0x2321", "0x00f7", "0x2248", "0x00b0", "0x2219", "0x00b7", "0x221a",
            "0x207f", "0x00b2", "0x25a0", "0x00a0" };

    // Liste des décimaux des caractères étendus
    public static final int[] DECIMALS = { 199, 252, 233, 226, 228, 224, 229, 231, 234, 235, 232, 239, 238,
            236, 196, 197, 201, 230, 198, 244, 246, 242, 251, 249, 255, 214, 220, 162, 163, 165, 8359,
            402, 225, 237, 243, 250, 241, 209, 170, 186, 191, 8976, 172, 189, 188, 161, 171, 187, 9617,
            9618, 9619, 9474, 9508, 9569, 9570, 9558, 9557, 9571, 9553, 9559, 9565, 9564, 9563, 9488,
            9492, 9524, 9516, 9500, 9472, 9532, 9566, 9567, 9562, 9556, 9577, 9574, 9568, 9552, 9580,
            9575, 9576, 9572, 9573, 9561, 9560, 9554, 9555, 9579, 9578, 9496, 9484, 9608, 9604, 9612,
            9616, 9600, 945, 223, 915, 960, 931, 963, 181, 964, 934, 920, 937, 948, 8734, 966, 949,
            8745, 8801, 177, 8805, 8804, 8992, 8993, 247, 8776, 176, 8729, 183, 8730, 8319, 178, 9632, 160 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cesar);

        setTitle(getString(R.string.césar));

        editTMessage = findViewById(R.id.getText);
        editTDecalage = findViewById(R.id.getDecal);
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
    // - On récupèrer les valeurs de la zone de texte message et décalage si il ne sont pas vide (
    // - Si c'est vide : on envoi une bulle d'info pour préciser de remplir tous les champs
    // - Sinon on convertir le décalage en un int et on lance la fonction cesar avec en paramètre le message et le décalage
    public void crypter(View v){
        txt = editTMessage.getText().toString();
        cle = editTDecalage.getText().toString();
        if(txt.matches("") || cle.matches("")){
            Toast.makeText(getApplicationContext(), getString(R.string.emptyField), Toast.LENGTH_LONG).show();
        }else{
            key = Integer.parseInt(cle);
            cesar(txt, key);
        }
    }

    // Quand on appuie sur le bouton decrypter :
    // - On récupèrer les valeurs de la zone de texte message et décalage si il ne sont pas vide (
    // - Si c'est vide : on envoi une bulle d'info pour préciser de remplir tous les champs
    // - Sinon on convertir le décalage en un int et on lance la fonction cesar avec en paramètre le message et le décalage (décalage négatif pour pouvoir reculer)
    public void decrypter(View v){
        txt = editTMessage.getText().toString();
        cle = editTDecalage.getText().toString();
        if(txt.matches("") || cle.matches("")){
            Toast.makeText(getApplicationContext(), getString(R.string.emptyField), Toast.LENGTH_LONG).show();
        }else{
            key = Integer.parseInt(cle);
            cesar(txt, -key);
        }
    }

    // Fonction qui quand on appuie sur le texte résultat remplace la zone de texte du message par le message résultat
    public void getResultText(View v){
        editTMessage.setText(result.getText());
    }

    // Fonction qui crypte ou décrypte avec la méthode de cryptage César et qui prend en paramètre un message à crypter/décrypter et le décalage de caractère
    public void cesar(String mess, int key){

        StringBuilder messEncrypt = new StringBuilder("");	// Message résultat qui contiendra le cryptage

        for (int i = 0; i < mess.length(); i++) {   // Analyse chaque caractère du message saisi

            int codePoint = mess.codePointAt(i);	// Décimal du caractère à la position i du message saisi

            // [ Verifie si la message ne contient pas de caractère hexa de la forme \0x****
            if(i<=mess.length()-7) {    // Si il reste encore 7 cactères avant la fin du message (c'est_à-dire, qu'il y a possibilité que ça soit un caractère hexa)
                String i0 = String.valueOf(mess.charAt(i));
                String i1 = String.valueOf(mess.charAt(i+1));
                String i2 = String.valueOf(mess.charAt(i+2));
                if(i0.equals("\\") && i1.equals("0") && i2.equals("x")) {   // Si les 3 valeurs à partir de i du message contient bien : \0x   ..
                        String hexaCode = String.valueOf(mess.charAt(i+3)) + String.valueOf(mess.charAt(i+4)) + String.valueOf(mess.charAt(i+5)) + String.valueOf(mess.charAt(i+6)); // On récuperer les 4 caractères hexa après le \0x
                        int codePointOfHexaCode = Integer.parseInt(hexaCode,16);    // On le converti l'hexa en décimal
                        codePoint = codePointOfHexaCode;    // On change de décimal avec la nouvelle valeur
                        i=i+6;  // On passe au caractère suivant
                }
            }
            // ]

            if (key > 255) key= key%256;            // Si la clé est supérieur à 255 (c'est-à-dire si il fait une boucle) on récupère le reste de la division pas 256
            if (key <0) key = (256-(-key))%256;     // Si la clé est inférieur à 0 on change la valeur pour qu'il part de la fin de la table ASCII étendue

            // ---------------------------------- RECUPERATION DU CODE ASCII

            if(codePoint > 127) {                               // Si le décimal du caractère est supérieur à 127 (c'est-à-dire, si c'est un caractère de la table ASCII étendue) ..
                int newCodePoint = getExtendChar(codePoint);    // On éxécute getExtendChar avec le décimal du caratère et récupère le bon décimal (plus de détail dans la fonction correspondante)
                codePointDecal = newCodePoint + key;            // On récupère la bonne valeur du décimal + le décalage
            }else {                                             // Sinon (c'est un caractère de la table ASCII normal) ..
                codePointDecal = codePoint + key;               // On récupère le décimal + le décalage
            }

            if(codePointDecal > 255) codePointDecal = codePointDecal%256;   // Si le décimal décalé est supérieur à 255(c'est-à-dire si il fait une boucle) on récupère le reste de la division pas 256

            // ---------------------------------- AFFICHAGE
            if(codePointDecal > 127) {                                  // Si le décimal du caractère décalé est supérieur à 127 (c'est-à-dire, si c'est un caractère de la table ASCII étendue) ..
                String hexaOfExtendChar = "\\"+EXTENDEDS[codePointDecal-128];   // Récupère l'hexa du caractère décalé ( de la forme \0x**** )
                if(DECIMALS[codePointDecal-128] > 255) {	                    // Si le caratère décalé n'est pas affichable ..
                    messEncrypt.append(hexaOfExtendChar);                       // Ajoute dans le message résultat l'hexa du caracère non affichable
                }else {                                                                                     // Sinon (c'est un caractère de la table ASCII normal) ..
                    String extendCharacter = hexToAscii( EXTENDEDS[codePointDecal-128].substring(2) );      // On éxécute la fonction hexToAscii pour avoir le caratère décalé via sont hexa
                    messEncrypt.append(extendCharacter);                                                    // On ajoute dans le message résultat le caracère décalé de la table ASCII étendue
                }
            } else {                                                    // Sinon (c'est un caractère de la table ASCII normal) ..
                String normalCharacter = Character.toString( (char) (codePointDecal) );                                 // On récupère le caractère décalé avec son décimal
                String hexaOfNormalChar = "\\"+ Integer.toHexString( codePointDecal | 0x10000).substring(1);         // On récupère l'hexa du caractère décalé ( de la forme \0x**** )
                messEncrypt.append(normalCharacter.replaceAll("\\p{C}", "\\"+hexaOfNormalChar) );    //  On ajoute dans le message résultat le caracère décalé de la table ASCII normal et si il n'est pas affichable on ajoute à la place son hexa
            }
        }
        result.setText(messEncrypt.toString());     // Affiche le message crypter dans la zone de texte result
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
    // Fonction qui récupère la valeur hexa  et retourne le caractère qui lui correspond (par exemple avec hexa + décalage de 0 : 00e9 -> é )
    private static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder("");
        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString().substring(1);
    }
}
