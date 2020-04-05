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

    public static int pgcd(int a, int b) {
        int r = 0;
        while(b != 0) {
            r = a%b;
            a = b;
            b = r;
        }
        return a;
    }

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


    public static String[] bloc(int m, int n) {
        String res = Integer.toString(m);
        ArrayList<String> l = new ArrayList();

        if(m < n) {
            l.add(Integer.toString(m));
            return l.toArray(new String[1]);
        } else {
            int debut = 0;
            int fin = res.length();

            while (debut != fin) {
                String index = res.substring(debut, fin);

                if(Integer.parseInt(index) < n) {
                    l.add(res.substring(debut,fin));
                    debut = fin;
                    fin = res.length();
                } else {
                    fin--;
                }
            }
        }
        return l.toArray(new String[l.size()]);

    }

    public static BigInteger crypt(BigInteger m, BigInteger e, BigInteger n) {
        return m.modPow(e, n);
    }

    public static BigInteger decrypt(BigInteger m, BigInteger d, BigInteger n) {
        return m.modPow(d, n);
    }


    public void RSA(BigInteger p, BigInteger q, BigInteger m, int cryptingMethod) {
        trace.setLength(0);
        StringBuilder res = new StringBuilder();

        BigInteger n = p.multiply(q);
        BigInteger phi_n = (p.subtract(new BigInteger("1")).multiply(q.subtract(new BigInteger("1")))  );

        trace.append("------------------- TRACE -------------------\n");
        trace.append("n = p x q = ").append(n).append("\n");
        trace.append("φ(n) = (p - 1) x (q - 1) = ").append(phi_n).append("\n");

        BigInteger e;
        if(p.intValue() > q.intValue()) {
            e = p.add(new BigInteger("1"));
        }else {
            e = q.add(new BigInteger("1"));
        }

        while (pgcd(phi_n.intValue(), e.intValue()) != 1 ) {
            e = e.add(new BigInteger("1"));
        }
        trace.append("e premier avec φ(n) = ").append(e).append("\n");

        int d = inverMod(e.intValue(), phi_n.intValue());

        trace.append("d = ").append(inverMod(e.intValue(), phi_n.intValue())).append("\n");

        String[] maListe = bloc(m.intValue(),n.intValue());

        trace.append("Bloc du message = ").append(Arrays.toString(bloc(m.intValue(), n.intValue()))).append("\n");
        trace.append("--------------------------------\n");
        for (String value : maListe) {
            BigInteger s = new BigInteger(value);
            if (cryptingMethod == 1) {
                res.append(crypt(s, e, n));
            } else {
                res.append(decrypt(s, BigInteger.valueOf(d), n));
            }
        }
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
