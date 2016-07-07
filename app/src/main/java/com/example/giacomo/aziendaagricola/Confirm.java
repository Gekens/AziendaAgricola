package com.example.giacomo.aziendaagricola;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giacomo on 23/06/2016.
 */
public class Confirm extends DialogFragment {
    EditText nome;
    EditText cognome;
    EditText mail;
    TextView totale;
    String qta, cas, snome, scognome, smail, sdisp, pro, qua;
    Spinner spinqtaprimegiant, spincasprimegiant;
    private Mail m, r;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        nome = (EditText) getActivity().findViewById(R.id.editTextNome);
        cognome = (EditText) getActivity().findViewById(R.id.editTextCognome);
        mail = (EditText) getActivity().findViewById(R.id.editTextMail);
        spinqtaprimegiant = (Spinner) getActivity().findViewById(R.id.spinnerQtaPrimegiant);
        spincasprimegiant = (Spinner) getActivity().findViewById(R.id.spinnerCasPrimeGiant);
        totale = (TextView) getActivity().findViewById(R.id.textViewNum);
        m = new Mail("giacomozancan@gmail.com", "95642108");
        r = new Mail("thegekens@gmail.com", "Dovakinzanc0");
        AlertDialog.Builder vBuilder = new AlertDialog.Builder(getActivity());
        vBuilder.setTitle("Sei sicuro?");
        vBuilder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                snome = nome.getText().toString();
                scognome = cognome.getText().toString();
                smail = mail.getText().toString();
                qta = spincasprimegiant.getSelectedItem().toString();
                cas = spinqtaprimegiant.getSelectedItem().toString();
                getDialog().dismiss();

                String[] toArr = {"giacomozancan@gmail.com"}; // This is an array, you can add more emails, just separate them with a coma
                m.setTo(toArr); // load array to setTo function
                m.setFrom("fromEmail@domain.tld"); // who is sending the email
                m.setSubject("Ordine 'Prime Giant'");
                m.setBody("Nome: " + snome + "\n" +
                        "Cognome: " + scognome + "\n" +
                        "Mail: " + smail + "\n" +
                        "Quantità: " + qta + "\n" +
                        "Cassetta: " + cas);

                String[] toArrr = {smail}; // This is an array, you can add more emails, just separate them with a coma
                r.setTo(toArrr); // load array to setTo function
                r.setFrom("fromEmail@domain.tld"); // who is sending the email
                r.setSubject("Ordine 'Prime Giant'");
                r.setBody("Il tuo ordine è stato inviato, a breve riceverai la data in cui l'ordine sarà pronto." + "\n\n" + "Totale: " + totale.getText().toString());


                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                StrictMode.setThreadPolicy(policy);


                sdisp = "In attesa";
                pro = "http://serverdatizancan.esy.es/ciliegieicon.png";
                qua = "Prime Giant";

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("name", snome));
                nameValuePairs.add(new BasicNameValuePair("surname", scognome));
                nameValuePairs.add(new BasicNameValuePair("mail", smail));
                nameValuePairs.add(new BasicNameValuePair("qta", qta));
                nameValuePairs.add(new BasicNameValuePair("cas", cas));
                nameValuePairs.add(new BasicNameValuePair("disp", sdisp));
                nameValuePairs.add(new BasicNameValuePair("qua", qua));
                nameValuePairs.add(new BasicNameValuePair("image", pro));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://serverdatizancan.esy.es/setAllCustomers.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    HttpEntity httpEntity = httpResponse.getEntity();

                    Toast.makeText(getActivity(), "Database", Toast.LENGTH_SHORT).show();
                } catch (ClientProtocolException e){
                    Log.d("Log tag", e + "");
                }
                catch (IOException e){
                    Log.d("Log tag", e + "");
                }

                try {
                    if (m.send()) {
                        // success
                        Toast.makeText(getActivity(), "Ordine inviato", Toast.LENGTH_LONG).show();
                    }
                    else {
                        // failure
                        Toast.makeText(getActivity(), "Errore: Controlla la tua connessione", Toast.LENGTH_LONG).show();
                    }
                    try {
                        if (r.send()) {
                            // success
                            Intent vIntent = new Intent(getActivity(), MainActivity.class);
                            startActivity(vIntent);
                        }
                        else {
                            // failure
                            Toast.makeText(getActivity(), "Mail non inviata.", Toast.LENGTH_LONG).show();
                            Intent vIntent = new Intent(getActivity(), MainActivity.class);
                            startActivity(vIntent);
                        }
                    } catch (Exception e) {
                        // some other problem
                        Toast.makeText(getActivity(), "La mail non è corretta.", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    // some other problem
                    Toast.makeText(getActivity(), "Controlla la tua connessione.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
        vBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        vBuilder.setNeutralButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return vBuilder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
