package com.fr.codage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;

public class DESActivity extends AppCompatActivity {

    public static final String DETAILS = "DETAILS";
    public static final int ENCRYPT =  1;
    public static final int DECRYPT = -1;
    public EditText editTMessage, editTCle;
    public TextView result, detail;
    public static String txt, cle;
    public static StringBuilder trace = new StringBuilder();

    public static final int[] tablePermutInitiale = {
            58,	50,	42,	34,	26,	18,	10,	2,
            60,	52,	44,	36,	28,	20,	12,	4,
            62,	54,	46,	38,	30,	22,	14,	6,
            64,	56,	48,	40,	32,	24,	16,	8,
            57,	49,	41,	33,	25,	17,	 9,	1,
            59,	51,	43,	35,	27,	19,	11,	3,
            61,	53,	45,	37,	29,	21,	13,	5,
            63,	55,	47,	39,	31,	23,	15,	7
    };

    public static final int[] tablePermutFinal = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49,  17, 57, 25
    };

    public static final int[] Developpement = {
            32,  1,  2,  3,   4,  5,
            4,  5,  6,  7,   8,  9,
            8,  9, 10, 11,  12, 13,
            12, 13, 14, 15,  16, 17,
            16, 17, 18, 19,  20, 21,
            20, 21, 22, 23,  24, 25,
            24, 25, 26, 27,  28, 29,
            28, 29, 30, 31,  32,  1
    };

    public static final int[] Permutation = {
            16,  7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2,  8, 24, 14,
            32, 27,  3,  9,
            19, 13, 30,  6,
            22, 11,  4, 25
    };

    public static final int[] PermutationChoice1 = {
            57, 49, 41, 33, 25, 17,  9,
            1, 58, 50, 42, 34, 26, 18,
            10,  2, 59, 51, 43, 35, 27,
            19, 11,  3, 60, 52, 44, 36,

            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14,  6, 61, 53, 45, 37, 29,
            21, 13,  5, 28, 20, 12,  4
    };

    public static final int[] PermutationChoice2 = {
            14,	17,	11,	24,	 1,	 5,
            3,	28,	15,	 6,	21,	10,
            23,	19,	12,	 4,	26,	 8,
            16,	 7,	27,	20,	13,	 2,
            41,	52,	31,	37,	47,	55,
            30,	40,	51,	45,	33,	48,
            44,	49,	39,	56,	34,	53,
            46,	42,	50,	36,	29,	32
    };

    public static final int[][][] boiteDeSubstitution = {
            { { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
                    { 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
                    { 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
                    { 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 } },

            { { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
                    { 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
                    { 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
                    { 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 } },

            { { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
                    { 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
                    { 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
                    { 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 } },

            { { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
                    { 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
                    { 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
                    { 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 } },

            { { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
                    { 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
                    { 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
                    { 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 } },

            { { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
                    { 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
                    { 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
                    { 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 } },

            { { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
                    { 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
                    { 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
                    { 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 } },

            { { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
                    { 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
                    { 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
                    { 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } }
    };

    public static final int[] sousCles = {
            1, 1, 2, 2, 2, 2, 2, 2,
            1, 2, 2, 2, 2, 2, 2, 1
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_e_s);

        setTitle(getString(R.string.des));

        editTMessage = findViewById(R.id.getText);
        editTCle = findViewById(R.id.getCle);
        result = findViewById(R.id.result);
        detail = findViewById(R.id.detail);

        detail.setMovementMethod(new ScrollingMovementMethod());    // Activer le scroll du textViev pour voir la trace complète
    }

    // Quand on appuie sur le bouton crypter :
    // - On récupèrer les valeurs de la zone de texte message et de la clé si il ne sont pas vide
    // - Si c'est vide : on envoi une bulle d'info pour préciser de remplir tous les champs
    // - Sinon on lance la fonction execDES() avec en paramètre le message, le message clé et la méthode de cryptage qui est ici crypter
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void crypter(View v){
        trace.setLength(0);
        txt = editTMessage.getText().toString();
        cle = editTCle.getText().toString();
        if(txt.matches("") || cle.matches("")){
            Toast.makeText(getApplicationContext(), getString(R.string.emptyField), Toast.LENGTH_LONG).show();
        }else{
            execDES(txt, cle, ENCRYPT);
        }
    }

    // Quand on appuie sur le bouton decrypter :
    // - On récupèrer les valeurs de la zone de texte message et de la clé si il ne sont pas vide
    // - Si c'est vide : on envoi une bulle d'info pour préciser de remplir tous les champs
    // - Sinon on lance la fonction execDES() avec en paramètre le message, le message clé et la méthode de cryptage qui est ici decrypter
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void decrypter(View v){
        trace.setLength(0);
        txt = editTMessage.getText().toString();
        cle = editTCle.getText().toString();
        if(txt.matches("") || cle.matches("")){
            Toast.makeText(getApplicationContext(), getString(R.string.emptyField), Toast.LENGTH_LONG).show();
        }else{
            execDES(txt, cle, DECRYPT);
        }
    }

    // Fonction qui prend en paramètre une table de permutation et un hexa et qui retourne la l'hexa permuté
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String permutation(int[] table, String bloc) {
        StringBuilder output = new StringBuilder();
        bloc = hexaToBin(bloc);
        for (int value : table) {
            output.append(bloc.charAt(value - 1));
        }
        return binToHexa(output.toString());
    }

    // Fonction qui retourne le décalage à gauche des bits avec en paramètre un hexa et le nombre de bit que l'on souhaite décaller
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String leftShift(String hex, int bitsDecal) {
        // [ Création d'une table de permutation pour le décalage à gauche selon la taille en bit de l'hexa
        int len = hex.length()*4 - 1;
        int[] leftShift = new int[len+1];
        for(int i=0; i < len; i++) {
            leftShift[i] = i+2;
        }
        leftShift[len] = 1;
        // ]
        for(int j=bitsDecal; j>0; j--) {	// Décalage selon le nombre de bit
            hex = permutation(leftShift, hex);
        }
        return hex;
    }

    // Fonction qui prend en paamètre une clé en hexa et qui retourne une liste de 16 sous-clé de 48 bits chacune
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String[] getAllKeys(String key) {
        String[] keys = new String[16];
        key = permutation(PermutationChoice1, key); // Première permutation
        for (int i = 0; i < 16; i++) { 	// Décalage à gauche des 16 clés
            String leftPart = leftShift(key.substring(0, 7), sousCles[i]);
            String rightPart = leftShift(key.substring(7, 14), sousCles[i]);
            key = leftPart + rightPart;
            keys[i] = permutation(PermutationChoice2, key); // Deuxième permutation
        }
        return keys;
    }

    // Fonction qui prend en paramètre deux hexa et qui retoune le XOR logique de ses deux hexa
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String xor(String l, String r) {
        long left = Long.parseUnsignedLong(l, 16);	// hexa en decimal
        long right = Long.parseUnsignedLong(r, 16);	// hexa en decimal
        String result = Long.toHexString(left ^ right); // XOR reconverti en hexa
        while (result.length() < r.length()) // Ajout des 0 manquants
            result = "0" + result;
        return result;
    }

    // Fonction qui converti un hexa en binaire
    public static String hexaToBin(String hex) {
        StringBuilder binOfBloc = new StringBuilder();
        for(int i=0; i< hex.length(); i++) {
            String binOfChar = String.format("%4s",new BigInteger(Character.toString(hex.charAt(i)), 16).toString(2)).replace(' ', '0');
            binOfBloc.append(binOfChar);
        }
        return binOfBloc.toString();
    }

    // Fonction qui converti un inaire en hexa
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String binToHexa(String bin) {
        int n = bin.length()/4;
        long decimal = Long.parseUnsignedLong(bin, 2);
        String hexStr = Long.toHexString(decimal);
        while (hexStr.length() < n)
            hexStr = "0" + hexStr;
        return hexStr;
    }

    // Fonction qui divise le bloc de 48 bits en 8 vecteur de 4 bits et retourne un bloc de 32 bits
    public static String sBox(String hex) {
        StringBuilder result = new StringBuilder();
        hex = hexaToBin(hex);
        for (int i = 0; i < 48; i += 6) {
            String temp = hex.substring(i, i + 6);
            int num = i / 6;
            int row = Integer.parseInt(""+temp.charAt(0) + temp.charAt(5), 2 );
            int col = Integer.parseInt( temp.substring(1, 5), 2);
            String vect = Integer.toHexString(boiteDeSubstitution[num][row][col]);
            Log.d(DETAILS,"S"+(num+1)+" = " + hexaToBin(vect)+" ");
            trace.append("S").append(num + 1).append(" = ").append(hexaToBin(vect)).append(" ");
            result.append(vect);
        }
        return result.toString();
    }

    // Fonction qui prend en paramètre le bloc initial la sous-clé et le nombre de tour et retourne le bloc ittéré
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String feistel(String hex, String key, int tour) {
        Log.d(DETAILS,"Itération "+(tour+1));
        trace.append("Itération ").append(tour + 1).append("\n");
        Log.d(DETAILS,"K = "+hexaToBin(key));
        trace.append("K = ").append(hexaToBin(key)).append("\n");

        String initLeftPart = hex.substring(0, 8);
        String initRightPart = hex.substring(8, 16);

        String newLeftPart = initRightPart;
        String newRightPart = initRightPart;
        // Expansion à 48 bits
        String expansion = permutation(Developpement, newRightPart);
        Log.d(DETAILS,"E(R) = "+hexaToBin(expansion));
        trace.append("E(R) = ").append(hexaToBin(expansion)).append("\n");
        // xor de la valeur temp et la clé i
        String xor = xor(expansion, key);
        Log.d(DETAILS,"A = E(R) + K = "+hexaToBin(xor));
        trace.append("A = E(R) + K = ").append(hexaToBin(xor)).append("\n");
        // s-box pour avoir le bloc en 32 bits
        String sbox = sBox(xor);
        Log.d(DETAILS,"B = "+hexaToBin(sbox));
        trace.append("\nB = ").append(hexaToBin(sbox)).append("\n");
        // D-box
        String dbox = permutation(Permutation, sbox);
        Log.d(DETAILS,"P(B) = "+hexaToBin(dbox));
        trace.append("P(B) = ").append(hexaToBin(dbox)).append("\n");
        // xor
        newRightPart = xor(initLeftPart, dbox);

        Log.d(DETAILS,"L1 = "+hexaToBin(newLeftPart)+" = "+newLeftPart);
        trace.append("L1 = ").append(hexaToBin(newLeftPart)).append(" = ").append(newLeftPart).append("\n");
        Log.d(DETAILS,"R1 = "+ hexaToBin(newRightPart)+" = "+newRightPart);
        trace.append("R1 = ").append(hexaToBin(newRightPart)).append(" = ").append(newRightPart).append("\n");
        Log.d(DETAILS,"--------------------------------");
        trace.append("--------------------------------\n");
        // inversion de la partie gauche et droite
        return newLeftPart + newRightPart;
    }

    // Fonction qui exécute le cryptage / décryptage DES avec en paramètre un bloc et une clé en hexa et qui retourne le bloc chiffré
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void execDES(String bloc, String cle, int cryptingMethod) {

        // Récuperation des clés
        String[] keys = getAllKeys(cle);
        //System.out.println(Arrays.toString(keys));

        // Permutation initiale
        Log.d(DETAILS,"BinOfBloc : "+hexaToBin(bloc));
        String permutInitiale = permutation(tablePermutInitiale, bloc);
        trace.append("------------------- TRACE -------------------\n");
        Log.d(DETAILS, "L0 = "+hexaToBin(permutInitiale.substring(0,8))  +" = "+permutInitiale.substring(0,8) );
        trace.append("L0 = ").append(hexaToBin(permutInitiale.substring(0, 8))).append(" = ").append(permutInitiale.substring(0, 8)).append("\n");
        Log.d(DETAILS, "R0 = "+hexaToBin(permutInitiale.substring(8,16)) +" = "+permutInitiale.substring(8,16) );
        trace.append("R0 = ").append(hexaToBin(permutInitiale.substring(8, 16))).append(" = ").append(permutInitiale.substring(8, 16)).append("\n");
        Log.d(DETAILS,"--------------------------------");
        trace.append("--------------------------------\n");
        // Itération - Feistel
        if(cryptingMethod == 1) {
            for (int i = 0; i < 16; i++) {
                permutInitiale = feistel(permutInitiale, keys[i], i);
            }
        } else {
            for (int i = 15; i > -1; i--) {
                permutInitiale = feistel(permutInitiale, keys[i], 15-i);
            }
        }

        // Permutation final
        String permutFinale = permutInitiale.substring(8, 16) + permutInitiale.substring(0, 8);
        permutFinale = permutation(tablePermutFinal, permutFinale);

        result.setText(permutFinale);
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
