package com.fr.codage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HillActivity extends AppCompatActivity {

    public static final int ENCRYPT =  1;
    public static final int DECRYPT = -1;
    public static String[] pickerVals;
    public EditText editTMessage;
    public TextView result;
    public NumberPicker pickerA, pickerB, pickerC, pickerD;
    public static String txt;
    public static int pA, pB, pC, pD;

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
        setContentView(R.layout.activity_hill);

        setTitle(getString(R.string.hill));

        editTMessage = findViewById(R.id.getText);
        result = findViewById(R.id.result);

        pickerA = findViewById(R.id.pickerA);
        pickerB = findViewById(R.id.pickerB);
        pickerC = findViewById(R.id.pickerC);
        pickerD = findViewById(R.id.pickerD);

        // Ajout des valeurs littéralement dans le number picker car il ne prend pas en compte les nombres négatifs
        pickerVals = new String[] {"-9","-8","-7","-6","-5","-4","-3","-2","-1","0","1","2","3","4","5","6","7","8","9"};
        pickerA.setMaxValue(18);
        pickerA.setMinValue(0);
        pickerB.setMaxValue(18);
        pickerB.setMinValue(0);
        pickerC.setMaxValue(18);
        pickerC.setMinValue(0);
        pickerD.setMaxValue(18);
        pickerD.setMinValue(0);
        pickerA.setDisplayedValues(pickerVals);
        pickerB.setDisplayedValues(pickerVals);
        pickerC.setDisplayedValues(pickerVals);
        pickerD.setDisplayedValues(pickerVals);

        pickerA.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                pA = Integer.parseInt(pickerVals[pickerA.getValue()]);
            }
        });
        pickerB.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                pB = Integer.parseInt(pickerVals[pickerB.getValue()]);
            }
        });
        pickerC.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                pC = Integer.parseInt(pickerVals[pickerC.getValue()]);
            }
        });
        pickerD.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                pD = Integer.parseInt(pickerVals[pickerD.getValue()]);
            }
        });
    }

    // Quand on appuie sur le bouton crypter :
    // - On récupèrer les valeurs de la zone de texte message (si il n'est pas vide) et des chiffres composera la matrice
    // - Si c'est vide : on envoi une bulle d'info pour préciser de remplir tous les champs
    // - Sinon on lance la fonction hill() avec en paramètre le message, les chiffres qui composera la matrice et la méthode dencryptage qui est ici ENCrYPT pour crypter
    public void crypter(View v){
        txt = editTMessage.getText().toString();
        if(txt.matches("")){
            Toast.makeText(getApplicationContext(), getString(R.string.emptyField), Toast.LENGTH_LONG).show();
        }else{
            hill(txt, pA, pB, pC, pD, ENCRYPT);
        }
    }

    // Quand on appuie sur le bouton decrypter :
    // - On récupèrer les valeurs de la zone de texte message (si il n'est pas vide) et des chiffres composera la matrice
    // - Si c'est vide : on envoi une bulle d'info pour préciser de remplir tous les champs
    // - Sinon on lance la fonction hill() avec en paramètre le message, les chiffres qui composera la matrice et la méthode dencryptage qui est ici DECrYPT pour décrypter
    public void decrypter(View v){
        txt = editTMessage.getText().toString();
        if(txt.matches("")){
            Toast.makeText(getApplicationContext(), getString(R.string.emptyField), Toast.LENGTH_LONG).show();
        }else{
            hill(txt, pA, pB, pC, pD, DECRYPT);
        }
    }

    // Fonction qui crypte ou décrypte avec la méthode de cryptage Hill et qui prend en paramètre un message à crypter/décrypter, des valeurs d'une matrice et la méthode de cryptages
    public void hill(String mess, int A, int B, int C, int D, int cryptingMethod){
        StringBuilder messEncrypt = new StringBuilder();	// Message qui contiendra le cryptage

        List<Integer> list = new ArrayList<>();             // list qui conteindra les décimaux des caractères du message saisi
        for(int j = 0; j < mess.length(); j++) {             // Analyse chaque caractère du message saisi
            int codePointKey = mess.codePointAt( j );        // On récupère le décimal du caractère du message
            // Si le décimal du caractère est supérieur à 127 (c'est-à-dire, si c'est un caractère de la table ASCII étendue),on éxécute getExtendChar() avec le décimal du caratère et récupère le bon décimal
            if(codePointKey > 127)  codePointKey = getExtendChar(codePointKey);
            if(j<=mess.length()-7) {                 // Si il reste encore 7 cactères avant la fin du message (c'est_à-dire, qu'il y a possibilité que ça soit un caractère hexa)
                String i0 = String.valueOf(mess.charAt(j));
                String i1 = String.valueOf(mess.charAt(j+1));
                String i2 = String.valueOf(mess.charAt(j+2));
                if(i0.equals("\\") && i1.equals("0") && i2.equals("x")) {   // Si les 3 valeurs à partir de i du message contient bien : \0x   ..
                    String hexaCode = String.format("%s%s%s%s", mess.charAt(j+3), mess.charAt(j+4), mess.charAt(j+5), mess.charAt(j+6));    // On récupère les 4 caractères hexa après le \0x
                    codePointKey = Integer.parseInt(hexaCode,16);   // On change le décimal avec la nouvelle valeur (on converti l'hexa en décimal)
                    j=j+6;  // On passe au caractère suivant
                }
            }
            list.add(codePointKey); // On ajoute dans la liste le décimal du caractère du message saisi
        }


        for (int i = 0; i < list.size(); i = i + 2) {   // Analyse chaque décimaux deux par deux du message saisi qu'on a mis dans une liste
            if(list.size()%2 != 0) list.add(35);       // Si la taille de la liste est impair, on ajoute un caractère (35 -> #)

            // ------------------------------------------ RECUPERATION DU DECIMAL

            //---------------------- Partage en bloc de 2 avec récuépération de décimal

            int codePointChar0 = list.get(i);       // On récupère le décimal du caractère à la position i de la liste
            int codePointChar1 =list.get(i+1);      // On récupère le décimal du caractère à la position i+1 de la liste

            int determinant = det(pA,pB,pC,pD);                     // Calcule le déterminant de la matrice
            if( determinant==0 || (determinant%2 == 0 && determinant%13 != 0) ) {       // Si le déterminant de la matrice est égal à 0 ou qu'il est pair et non-divisible par 13 ..
                Toast.makeText(getApplicationContext(),  getString(R.string.errorMatrice), Toast.LENGTH_LONG).show();   // On affiche que la matrice n'est pas convenable si l'on souhaite par la suite décrypter
            } else {                                                                    // Sinon (la matrice permet le décryptage) ..

                // ==================================CRYPTAGE

                if(cryptingMethod == 1){                // Si la méthode de cryptage est 1 (c'est-à-dire que l'on souhaite crypter) ..

                    //-------------- Combinaison linéaire du bloc et modulo

                    int linearComb0 = mod(A*codePointChar0 + B*codePointChar1,256);     // On effectue la combinaison linéaire 0 et on éexute la fonction mod() avec en paramètre le 1er élément du bloc et la valeur du modulo pour récupérer la bonne valeur du décimal
                    int linearComb1 = mod(C*codePointChar0 + D*codePointChar1,256);     // On effectue la combinaison linéaire 1 et on éexute la fonction mod() avec en paramètre le 1er élément du bloc et la valeur du modulo pour récupérer la bonne valeur du décimal

                    // ---------------------------------- AFFICHAGE

                    messEncrypt.append(getCharacterByDecimal(linearComb0));
                    messEncrypt.append(getCharacterByDecimal(linearComb1));
                    //System.out.println("codePoint : "+codePointChar0 +" "+codePointChar1+" linear : "+linearComb0+" "+linearComb1);

                    // ================================== DECRYPTAGE

                } else {                                // Sinon (la méthode de cryptage est 0, c'est-à-dire que l'on souhaite décripter) ..

                    //-------------- Multiplication par la matrice

                    int[] inverseDet = invDet(pA,pB,pC,pD);                 // On inverse le déterminant
                    int invMat = inverMatrice(determinant, 256);      // On récupère l'inverse du déterminant modulo 256
                    for(int j = 0; j<inverseDet.length; j++) {
                        inverseDet[j] =(inverseDet[j]*invMat);          // On multiplie le déterminant avec chaque élément de la matrice
                    }
                    int val0 = mod(inverseDet[0]*codePointChar0 + inverseDet[1]*codePointChar1,256);    // On récupère la valeur du décimal du premier élément du bloc
                    int val1 = mod(inverseDet[2]*codePointChar0 + inverseDet[3]*codePointChar1,256);    // On récupère la valeur du décimal du deuxième élément du bloc

                    // ---------------------------------- AFFICHAGE

                    messEncrypt.append(getCharacterByDecimal(val0));
                    messEncrypt.append(getCharacterByDecimal(val1));
                }
            }
        }
        result.setText(messEncrypt.toString());     // Affiche le message crypter / décrypter dans la zone de texte result
    }

    // Fonction qui retourne le reste d'une division avec en paramètre la valeur à diviser et son modulo
    public static int mod (int x , int y){
        return x >= 0 ? x % y : y - 1 - ((-x-1) % y) ;
    }

    // Fonction qui retourne la valeur du déterminant avec en paramètres les valeurs d'une matrice
    public static int det(int a, int b, int c, int d) {
        return(a*d-b*c);
    }

    // Fonction qui retourne la matrice pour calculer l'inverse de la matrice avec en paramètres les valeurs d'une matrice
    public static int[] invDet(int a, int b, int c, int d) {
        return new int[]{d,-b,-c,a};
    }

    // Fonction qui prend en paramètre le déterminant et le reste de l'inverse du déterminant
    public static int inverMatrice(int k, int mod) {
        int i = 1;
        while(true) {
            i++;
            if ( i % 2 == 0 && i / 2 > 1)
                continue;
            else if ( i % 3 == 0 && i / 3 > 1)
                continue;
            else if ( i % 5 == 0 && i / 5 > 1)
                continue;
            else if ( i % 7 == 0 && i / 7>1)
                continue;
            int inverse = k*i%mod;
            if(inverse == 1) {
                return i;
            }
        }
    }


    // Réduit le clavier quand on touche autre chose qu'un champs à saisir
    public void closeKeyboard(View v) {
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
