package com.example.giacomo.aziendaagricola;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    ImageButton buttonciliegie, buttonlist, buttoncontatti, buttongalleria, buttonuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonciliegie = (ImageButton) findViewById(R.id.imageButtonCiligie);
        buttonlist = (ImageButton) findViewById(R.id.imageButtonList);
        buttoncontatti = (ImageButton) findViewById(R.id.imageButtonContatti);
        buttongalleria = (ImageButton) findViewById(R.id.imageButtonGalleria);
        buttonuser = (ImageButton) findViewById(R.id.imageButtonUser);

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
            String[] cred = s.split(" ");
            InputRead.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        buttonuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, buttonuser);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.user, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.one) {
                            Fragment vFragment = new Ordini();//creo il fragment
                            FragmentManager vManager = getFragmentManager();//recupero il fragmentManager
                            FragmentTransaction vTransaction = vManager.beginTransaction();//inizializzo una transazione
                            vTransaction.replace(R.id.container, vFragment, "list");//faccio l'add del fragment
                            vTransaction.commit();//faccio il commit della transazione
                        }
                        else if (item.getItemId() == R.id.two) {
                            try {
                                FileOutputStream fileout = openFileOutput("mytextfile.txt", MODE_PRIVATE);
                                OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
                                outputWriter.write("");
                                outputWriter.close();
                            }
                            catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(getBaseContext(), "Sei Uscito!", Toast.LENGTH_SHORT).show();
                            Intent vIntent = new Intent(MainActivity.this, Login.class);
                            startActivity(vIntent
                            );
                        }
                        else if (item.getItemId() == R.id.three) {
                            File file = getBaseContext().getFileStreamPath("mytextfile.txt");
                            file.delete();
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });

        buttongalleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonciliegie.clearColorFilter();
                buttonlist.clearColorFilter();
                buttoncontatti.clearColorFilter();
                buttongalleria.setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                Fragment vFragment = new Galleria();//creo il fragment
                FragmentManager vManager = getFragmentManager();//recupero il fragmentManager
                FragmentTransaction vTransaction = vManager.beginTransaction();//inizializzo una transazione
                vTransaction.replace(R.id.container, vFragment, "galleria");//faccio l'add del fragment
                vTransaction.commit();//faccio il commit della transazione
            }
        });

        buttoncontatti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonciliegie.clearColorFilter();
                buttonlist.clearColorFilter();
                buttongalleria.clearColorFilter();
                buttoncontatti.setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                Fragment vFragment = new Contatti();//creo il fragment
                FragmentManager vManager = getFragmentManager();//recupero il fragmentManager
                FragmentTransaction vTransaction = vManager.beginTransaction();//inizializzo una transazione
                vTransaction.replace(R.id.container, vFragment, "contatti");//faccio l'add del fragment
                vTransaction.commit();//faccio il commit della transazione
            }
        });

        buttonlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonciliegie.clearColorFilter();
                buttoncontatti.clearColorFilter();
                buttongalleria.clearColorFilter();
                buttonlist.setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                Fragment vFragment = new ListFragment();//creo il fragment
                FragmentManager vManager = getFragmentManager();//recupero il fragmentManager
                FragmentTransaction vTransaction = vManager.beginTransaction();//inizializzo una transazione
                vTransaction.replace(R.id.container, vFragment, "list");//faccio l'add del fragment
                vTransaction.commit();//faccio il commit della transazione
            }
        });

        buttonciliegie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonlist.clearColorFilter();
                buttoncontatti.clearColorFilter();
                buttongalleria.clearColorFilter();
                buttonciliegie.setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                Fragment vFragment = new ciliegie();//creo il fragment
                FragmentManager vManager = getFragmentManager();//recupero il fragmentManager
                FragmentTransaction vTransaction = vManager.beginTransaction();//inizializzo una transazione
                vTransaction.replace(R.id.container, vFragment, "ciliegie");//faccio l'add del fragment
                vTransaction.commit();//faccio il commit della transazione
            }
        });

        if (savedInstanceState == null) {//metto il fragment solo se è la prima volta che avvio l'activity, infatti il fragment resta se giriamo lo smartphone.
            Fragment vFragment = new ListFragment();//creo il fragment
            FragmentManager vManager = getFragmentManager();//recupero il fragmentManager
            FragmentTransaction vTransaction = vManager.beginTransaction();//inizializzo una transazione
            vTransaction.add(R.id.container, vFragment, "list");//faccio l'add del fragment
            vTransaction.commit();//faccio il commit della transazione
        }




    }


}