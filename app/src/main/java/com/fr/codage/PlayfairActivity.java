package com.fr.codage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PlayfairActivity extends AppCompatActivity {

    public static final int ENCRYPT =  1;
    public static final int DECRYPT = -1;
    public EditText editTMessage, editTCle;
    public TextView result, detail;
    public static String txt, cle;
    public static StringBuilder trace = new StringBuilder();
    public static String[][] table;
    public static int length;
    public Boolean validTexts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playfair);

        setTitle(getString(R.string.playfair));

        editTMessage = findViewById(R.id.getText);
        editTCle = findViewById(R.id.getCle);
        result = findViewById(R.id.result);
        detail = findViewById(R.id.detail);
    }

    // Réduit le clavier quand on touche autre chose qu'un champ à saisir
    public void closeKeyboard(View v) {
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // Quand on appuie sur le bouton Crypter :
    // - On récupère les valeurs de la zone de texte message et de la clé si ils ne sont pas vides
    // - Si c'est vide : on envoie une bulle d'info pour préciser de remplir tous les champs
    // - Sinon si les zones de textes contiennent bien que des lettres, on lance la fonction playfair() avec en paramètre le message, le message clé et la méthode de cryptage qui est ici : crypter
    public void crypter(View v){
        trace.setLength(0);
        detail.setText("");
        result.setText("");
        txt = editTMessage.getText().toString();
        cle = editTCle.getText().toString();
        if(txt.matches("") || cle.matches("")){
            Toast.makeText(getApplicationContext(), getString(R.string.emptyField), Toast.LENGTH_LONG).show();
        }else{
            validTexts();
            if(validTexts) playfair(txt, cle, ENCRYPT);
            else Toast.makeText(getApplicationContext(), getString(R.string.validTexts), Toast.LENGTH_LONG).show();
        }
    }

    // Quand on appuie sur le bouton Décrypter :
    // - On récupère les valeurs de la zone de texte message et de la clé si ils ne sont pas vides
    // - Si c'est vide : on envoie une bulle d'info pour préciser de remplir tous les champs
    // - Sinon si les zones de textes contiennent bien que des lettres, on lance la fonction playfair() avec en paramètre le message, le message clé et la méthode de cryptage qui est ici : décrypter
    public void decrypter(View v){
        trace.setLength(0);
        detail.setText("");
        result.setText("");
        txt = editTMessage.getText().toString();
        cle = editTCle.getText().toString();
        if(txt.matches("") || cle.matches("")){
            Toast.makeText(getApplicationContext(), getString(R.string.emptyField), Toast.LENGTH_LONG).show();
        }else{
            validTexts();
            if(validTexts) playfair(txt, cle, DECRYPT);
            else Toast.makeText(getApplicationContext(), getString(R.string.validTexts), Toast.LENGTH_LONG).show();
        }
    }

    // Fonction qui retourne vrai si les EditText contiennent bien que des lettres (faux sinon)
    public Boolean validTexts(){
        validTexts = true;
        for(int i=0;i<txt.length();i++) {
            if (!(txt.charAt(i) >= 'A' && txt.charAt(i) <= 'Z' || txt.charAt(i) >= 'a' && txt.charAt(i) <= 'z'))
                validTexts = false;
        }
        for(int i=0;i<cle.length();i++) {
            if (!(cle.charAt(i) >= 'A' && cle.charAt(i) <= 'Z' || cle.charAt(i) >= 'a' && cle.charAt(i) <= 'z'))
                validTexts = false;
        }
        return validTexts;
    }

    public void playfair(String mess, String key, int cryptingMethod){
        StringBuilder messEncypt = new StringBuilder();

        mess = mess.toUpperCase();  // Met les caractères du message en majuscule
        key = key.toUpperCase();    // Met les caractères de la clé en majuscule

        table = getTable(key);      // Création de la table de la clé
        showTable(table);           // Affichage de la table de la clé dans le TextView detail

        if(cryptingMethod == 1){
            if(mess.length()%2 !=0) mess += "G";
            length = mess.length()/2+ mess.length()%2;  // nombre de pair

            // [ Ajout d'un X entre deux paire de caractères si ils sont identique
            for(int i = 0; i < length; i++){
                if(mess.length()%2 == 0 && mess.charAt(2 * i) == mess.charAt(2 * i + 1)) {
                    mess = new StringBuffer(mess).insert(2 * i + 1, 'X').toString();
                    length = mess.length() / 2 + mess.length() % 2;
                }
            }
            // ]
            // [ Création d'un tableau qui fait la séparation en pair du message et ajout d'un caractère si le message n'est pas pair (ici G)
            String[] tableMess = new String[length];
            for(int j = 0; j < length ; j++){
                if(j == (length - 1) && mess.length() / 2 == (length - 1))  mess += "G";
                tableMess[j] = mess.charAt(2 * j) +""+ mess.charAt(2 * j + 1);
            }
            // ]
            // [ On lance la fonction encryptTable() qui chhiffrera le message et on ajoute les caractères chiffrés dans le message resultat
            String[] tableEncrypt = encryptTable(tableMess);
            for(int k = 0; k < length; k++) messEncypt.append(tableEncrypt[k]);

            result.setText(messEncypt.toString());  // Affiche le message crypté dans la zone de texte result
        } else {
            for(int i = 0; i < mess.length() / 2; i++){
                char v0 = mess.charAt(2*i);
                char v1 = mess.charAt(2*i+1);
                int r1 = getPoint(v0).x;
                int r2 = getPoint(v1).x;
                int c1 = getPoint(v0).y;
                int c2 = getPoint(v1).y;
                // Règle 1: Si deux lettres sont sur la même ligne, on prend les deux lettres qui les suivent immédiatement à leur droite
                if(r1 == r2){
                    c1 = (c1 + 4) % 5;
                    c2 = (c2 + 4) % 5;
                // Règle 2: Si deux lettres sont sur la même colonne, on prend les deux lettres qui les suivent immédiatement en dessous
                }else if(c1 == c2){
                    r1 = (r1 + 4) % 5;
                    r2 = (r2 + 4) % 5;
                // Règle 3: Si les deux lettres sont sur les coins d'un rectangle, alors les lettres chiffrées sont sur les deux autres coins
                }else{
                    int tmp = c1;
                    c1 = c2;
                    c2 = tmp;
                }
                // On parcours le tableau et on place les valeurs chiffrées dans le message déchiffré
                messEncypt.append(table[r1][c1]).append(table[r2][c2]);
            }
            result.setText(messEncypt.toString());  // Affiche le message décrypté dans la zone de texte result
        }
        result.setText(messEncypt.toString());  // Affiche le message crypté / décrypté dans la zone de texte result

    }

    // Fonction qui prend en paramètre un tableau contenant des pairs de caractères du message en clair et qui retourne un tableau contenant des pairs de caractères du message chiffré
    private String[] encryptTable(String[] tableMess){
        String[] tableEncrypt = new String[length];     // Table qui contiendra le message chiffré par pair

        for(int i = 0; i < length; i++){
            char v0 = tableMess[i].charAt(0);
            char v1 = tableMess[i].charAt(1);
            int r1 = getPoint(v0).x;
            int r2 = getPoint(v1).x;
            int c1 = getPoint(v0).y;
            int c2 = getPoint(v1).y;

            // Règle 1: Si deux lettres sont sur la même ligne, on prend les deux lettres qui les suivent immédiatement à leur droite
            if(r1 == r2){
                c1 = (c1 + 1) % 5;
                c2 = (c2 + 1) % 5;

            // Règle 2: Si deux lettres sont sur la même colonne, on prend les deux lettres qui les suivent immédiatement en dessous
            }else if(c1 == c2){
                r1 = (r1 + 1) % 5;
                r2 = (r2 + 1) % 5;

            // Règle 3: Si les deux lettres sont sur les coins d'un rectangle, alors les lettres chiffrées sont sur les deux autres coins
            }else{
                int tmp = c1;
                c1 = c2;
                c2 = tmp;
            }
            // On parcours le tableau et on place les valeurs dans la table chiffrée
            tableEncrypt[i] = table[r1][c1] + "" + table[r2][c2];
        }
        return tableEncrypt;
    }

    // Fonction qui prend en paramètre un caractère et qui retourne un point contenant la ligne et la colonne de se caractère
    private Point getPoint(char c){
        Point p = new Point(0,0);
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 5; j++)
                if(c == table[i][j].charAt(0))
                    p = new Point(i,j);
        return p;
    }

    // Fonction qui prend en paramètre le tableau de clé et qui affiche se tableau dans la trace
    private void showTable(String[][] table){
        trace.append("-------------------------------------- TRACE --------------------------------------\n");
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                trace.append(table[i][j]).append(" ");
            }
            trace.append("\n");
        }
        detail.setText(trace);
    }

    // Fonction qui prend en paramètre le message clé et qui retourne un tableau ou on y ajoute cette clé
    private String[][] getTable(String key){
        String[][] table = new String[5][5];
        String fullKey = key + "ABCDEFGHIJKLMNOPQRSTUVXYZ";

        // [ Initialisation du tableau avec des caractères vides
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 5; j++)
                table[i][j] = "";
        // ]
        // Remplissage de la table
        for(int k = 0; k < fullKey.length(); k++){
            boolean charInTable = false, charUsed = false;
            for(int i = 0; i < 5; i++){
                for(int j = 0; j < 5; j++){ // Si le caractère est déjà présent dans la table à la position x=i et y=j on met le booléen à true pour ne pas le réajouter
                    if( table[i][j].equals("" + fullKey.charAt(k)) ){
                        charInTable = true;
                    //Sinon si :
                    // - la table à la position x=i et y=j est vide
                    // - le caractère n'est pas déjà dans la table
                    // - le caractère na pas déjà été utilisé dans la boucle
                    // --> On ajoute le caractère à la position x=i et y=j
                    } else if( !charInTable && !charUsed && table[i][j].equals("") ){
                        table[i][j] = "" + fullKey.charAt(k);
                        charUsed = true;
                    }
                }
            }
        }
        return table;
    }

    // Fonction qui quand on appuie sur le texte résultat remplace la zone de texte du message par le message résultat
    public void getResultText(View v){
        editTMessage.setText(result.getText());
    }
}
