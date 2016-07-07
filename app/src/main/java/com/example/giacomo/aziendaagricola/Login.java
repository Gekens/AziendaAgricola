package com.example.giacomo.aziendaagricola;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
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
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Login extends AppCompatActivity {
    Button accedi, rip;
    EditText mail, password;
    String smail, spassword, encryp;
    static String salt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mail = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        accedi = (Button) findViewById(R.id.btnLogin);
        rip = (Button) findViewById(R.id.rip);

        try {
            FileInputStream fileIn=openFileInput("mytextfile.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[100];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            if (s != "") {
                launchHomeScreen();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        salt = getSalt();

        rip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vIntent = new Intent(Login.this, Ripristina.class);
                startActivity(vIntent);
            }
        });


        accedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smail = mail.getText().toString();
                spassword = password.getText().toString();

                if (spassword.length() != 0) {

                    encryp = getEncryptedPassword(salt, spassword);


                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                    StrictMode.setThreadPolicy(policy);

                    List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
                    params.add(new BasicNameValuePair("mail", smail));
                    params.add(new BasicNameValuePair("encryp", encryp));

                    HttpEntity httpEntity = null;

                    String url = "http://serverdatizancan.esy.es/getUser.php?mail=" + smail + "&encryp=" + encryp;

                    try
                    {

                        DefaultHttpClient httpClient = new DefaultHttpClient();  // Default HttpClient


                        HttpGet httpGet = new HttpGet(url);

                        HttpResponse httpResponse = httpClient.execute(httpGet);

                        httpEntity = httpResponse.getEntity();



                    } catch (ClientProtocolException e) {

                        // Signals error in http protocol
                        e.printStackTrace();

                        //Log Errors Here



                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    // Convert HttpEntity into JSON Array
                    JSONArray jsonArray = null;

                    if (httpEntity != null) {
                        try {
                            String entityResponse = EntityUtils.toString(httpEntity);

                            Log.e("Entity Response  : ", entityResponse);

                            jsonArray = new JSONArray(entityResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (jsonArray != null) {
                        try {
                            FileOutputStream fileout = openFileOutput("mytextfile.txt", MODE_PRIVATE);
                            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
                            outputWriter.write(smail + " " + encryp);
                            outputWriter.close();
                        }
                        catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Intent vIntent = new Intent(Login.this, MainActivity.class);
                        startActivity(vIntent);
                        //display file saved message
                        Toast.makeText(getBaseContext(), "Loggato con successo!",
                                Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(Login.this, "Mail o Password non corrette", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void launchHomeScreen() {
        startActivity(new Intent(Login.this, MainActivity.class));
        finish();
    }

    public void generateNote(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getDataDirectory().getAbsolutePath(), "Notes");
                root.mkdirs();
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
