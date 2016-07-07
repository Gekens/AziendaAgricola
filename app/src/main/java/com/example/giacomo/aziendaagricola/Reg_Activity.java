package com.example.giacomo.aziendaagricola;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
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

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Reg_Activity extends AppCompatActivity {
    Button registrazione, login;
    TextView seek;
    SeekBar seekBar;
    EditText name, mail, password, password2;
    String sname;
    String smail;
    String spassword;
    String spassword2;
    String encryp;
    static String salt;
    private PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_);
        name = (EditText) findViewById(R.id.name);
        mail = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        password2 = (EditText) findViewById(R.id.password2);
        registrazione = (Button) findViewById(R.id.btnRegister);
        login = (Button) findViewById(R.id.btnLinkToLoginScreen);
        seek = (TextView) findViewById(R.id.textViewRat);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        salt = getSalt();

        File file = getBaseContext().getFileStreamPath("mytextfile.txt");
        if(file.exists()) {
            launchHomeScreen();
            finish();
        }

        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (password.getText().toString().length() <= 5) {
                    seekBar.setMax(10);
                    seekBar.setProgress(2);
                    seek.setTextColor(getResources().getColor(R.color.debole));
                    seek.setText("Password Debole");
                }
                else if (password.getText().toString().length() <= 8) {
                    seekBar.setMax(10);
                    seekBar.setProgress(5);
                    seek.setTextColor(getResources().getColor(R.color.normale));
                    seek.setText("Password Normale");
                }
                else if (password.getText().toString().length() <= 12) {
                    seekBar.setMax(10);
                    seekBar.setProgress(8);
                    seek.setTextColor(getResources().getColor(R.color.colorAccent));
                    seek.setText("Password Forte");
                }
                else if (password.getText().toString().length() > 12) {
                    seekBar.setMax(10);
                    seekBar.setProgress(10);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((password.getText().toString()).equals(password2.getText().toString())) {
                    seek.setTextColor(getResources().getColor(R.color.normale));
                    seek.setText("Le password coincidono");


                }
                else {

                    seek.setTextColor(getResources().getColor(R.color.debole));
                    seek.setText("Le password non coincidono");


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        registrazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sname = name.getText().toString();
                smail = mail.getText().toString();
                spassword = password.getText().toString();
                spassword2 = password2.getText().toString();

                if (sname.length() == 0) {
                    Toast.makeText(Reg_Activity.this, "Inserisci il nome", Toast.LENGTH_SHORT).show();
                    name.requestFocus();
                } else if (smail.length() == 0) {
                    Toast.makeText(Reg_Activity.this, "Inserisci la mail", Toast.LENGTH_SHORT).show();
                    mail.requestFocus();
                } else if (spassword.length() == 0) {
                    Toast.makeText(Reg_Activity.this, "Inserisci la password", Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                } else if (spassword2.length() == 0) {
                    Toast.makeText(Reg_Activity.this, "Inserisci nuovamente la password", Toast.LENGTH_SHORT).show();
                    password2.requestFocus();
                } else {


                    if ((spassword.length() != 0) && (spassword2.length() != 0)) {
                        if (spassword.equals(spassword2)) {
                            encryp = getEncryptedPassword(salt, spassword);


                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                            StrictMode.setThreadPolicy(policy);

                            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair("name", sname));
                            nameValuePairs.add(new BasicNameValuePair("mail", smail));
                            nameValuePairs.add(new BasicNameValuePair("encryp", encryp));

                            try {
                                HttpClient httpClient = new DefaultHttpClient();
                                HttpPost httpPost = new HttpPost("http://serverdatizancan.esy.es/createUser.php");
                                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                HttpResponse httpResponse = httpClient.execute(httpPost);
                                HttpEntity httpEntity = httpResponse.getEntity();
                                Intent vIntent = new Intent(Reg_Activity.this, Login.class);
                                startActivity(vIntent);

                                Toast.makeText(Reg_Activity.this, "Registrazione avvenuta con successo", Toast.LENGTH_SHORT).show();
                            } catch (ClientProtocolException e) {
                                Log.d("Log tag", e + "");
                            } catch (IOException e) {
                                Log.d("Log tag", e + "");
                            }
                        } else {
                            Toast.makeText(Reg_Activity.this, "Le password non coincidono", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Reg_Activity.this, "Per favore inserisci la password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void launchHomeScreen() {
        startActivity(new Intent(Reg_Activity.this, Login.class));
        finish();
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
