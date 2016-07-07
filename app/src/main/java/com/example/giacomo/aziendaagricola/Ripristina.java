package com.example.giacomo.aziendaagricola;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Ripristina extends AppCompatActivity {
    EditText mail;
    String smail, spassword;
    Button invia;
    private Mail m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripristina);
        mail = (EditText) findViewById(R.id.email);
        invia = (Button) findViewById(R.id.ripristina);
        m = new Mail("giacomozancan@gmail.com", "95642108");

        invia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smail = mail.getText().toString();
                String salt = getSalt();

                String uuid = UUID.randomUUID().toString();
                String encryp = getEncryptedPassword(salt, uuid);
                String click = "<html><body><form method=\"post\" action=\"http://serverdatizancan.esy.es/updatePassword.php\">\n" +
                        "  <input type=\"submit\" name=\"item\" value="+ "\"" + encryp + "\"" + ">\n" +
                        "</form></body></html>";

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                StrictMode.setThreadPolicy(policy);

                        String[] toArr = {smail}; // This is an array, you can add more emails, just separate them with a coma
                        m.setTo(toArr); // load array to setTo function
                        m.setFrom("fromEmail@domain.tld"); // who is sending the email
                        m.setSubject("Ordine 'Prime Giant'");
                        m.setBody("La password è: " + uuid + " " + click);

                        try {
                            if (m.send()) {
                                // success
                                Intent vIntent = new Intent(Ripristina.this, Login.class);
                                startActivity(vIntent);
                            }
                            else {
                                // failure
                                Toast.makeText(Ripristina.this, "Mail non inviata.", Toast.LENGTH_LONG).show();
                                Intent vIntent = new Intent(Ripristina.this, Login.class);
                                startActivity(vIntent);
                            }
                        } catch (Exception e) {
                            // some other problem
                            Toast.makeText(Ripristina.this, "La mail non è corretta.", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }


            }
        });
    }



    public static String getSalt(){
        String uuid = "margherita";
        return uuid;
    }
    public static String getEncryptedPassword(String salt, String password){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-384");
            md.update(salt.getBytes());

            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
