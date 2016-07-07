package com.example.giacomo.aziendaagricola;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.InputStreamReader;


public class primegiant extends Activity {
    Button invia;
    ImageView help;
    Spinner spinqtaprimegiant, spincasprimegiant;
    TextView totale;
    EditText nome, cognome, mail;
    String qta, cas, snome, scognome, smail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primegiant);
        spinqtaprimegiant = (Spinner) findViewById(R.id.spinnerQtaPrimegiant);
        spincasprimegiant = (Spinner) findViewById(R.id.spinnerCasPrimeGiant);
        help = (ImageView) findViewById(R.id.imageButton);
        nome = (EditText) findViewById(R.id.editTextNome);
        cognome = (EditText) findViewById(R.id.editTextCognome);
        mail = (EditText) findViewById(R.id.editTextMail);
        totale = (TextView) findViewById(R.id.textViewNum);
        invia = (Button) findViewById(R.id.buttonInvia);

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
            smail = cred[0];
            InputRead.close();
            mail.setText(smail);

        } catch (Exception e) {
            e.printStackTrace();
        }


        ArrayAdapter<CharSequence> adapterqtaprimegiant = ArrayAdapter.createFromResource(this, R.array.cassetta, android.R.layout.simple_spinner_item);
        adapterqtaprimegiant.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinqtaprimegiant.setAdapter(adapterqtaprimegiant);

        ArrayAdapter<CharSequence> adaptercasprimegiant = ArrayAdapter.createFromResource(this, R.array.quantità, android.R.layout.simple_spinner_item);
        adaptercasprimegiant.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spincasprimegiant.setAdapter(adaptercasprimegiant);

        spincasprimegiant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                qta = spincasprimegiant.getSelectedItem().toString();
                if (spinqtaprimegiant.getSelectedItemPosition() == 0) {
                    cas = "1";
                }
                else if (spinqtaprimegiant.getSelectedItemPosition() == 1) {
                    cas = "2";
                }
                else if (spinqtaprimegiant.getSelectedItemPosition() == 2) {
                    cas = "3";
                }
                totale.setText((Integer.toString(Integer.parseInt(qta)*Integer.parseInt(cas)) + "€"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinqtaprimegiant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                qta = spincasprimegiant.getSelectedItem().toString();
                if (spinqtaprimegiant.getSelectedItemPosition() == 0) {
                    cas = "1";
                }
                else if (spinqtaprimegiant.getSelectedItemPosition() == 1) {
                    cas = "2";
                }
                else if (spinqtaprimegiant.getSelectedItemPosition() == 2) {
                    cas = "3";
                }
                totale.setText((Integer.toString(Integer.parseInt(qta)*Integer.parseInt(cas)) + "€"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPopupWindow(v);
            }
        });


    }

    private void displayPopupWindow(View anchorView) {
        PopupWindow popup = new PopupWindow(primegiant.this);
        View layout = getLayoutInflater().inflate(R.layout.popup_content, null);
        popup.setContentView(layout);
        // Set content width and height
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // Closes the popup window when touch outside of it - when looses focus
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        // Show anchored to button
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.showAsDropDown(anchorView);
    }

    public void sendEmail(View view){
        snome = nome.getText().toString();
        scognome = cognome.getText().toString();
        qta = spincasprimegiant.getSelectedItem().toString();
        cas = spinqtaprimegiant.getSelectedItem().toString();

        if (snome.length() == 0) {
            Toast.makeText(primegiant.this, "Inserisci il tuo nome", Toast.LENGTH_SHORT).show();
            nome.requestFocus();
        }
        else if (scognome.length() == 0) {
            Toast.makeText(primegiant.this, "Inserisci il tuo cognome", Toast.LENGTH_SHORT).show();
            cognome.requestFocus();
        }
        else {


            FragmentManager fm = getFragmentManager();
            Confirm dialogFragment = new Confirm ();
            dialogFragment.show(fm, "Sample Fragment");


        }

    }

    @Override
    protected void onResume() {
        qta = spincasprimegiant.getSelectedItem().toString();
        if (spinqtaprimegiant.getSelectedItemPosition() == 0) {
            cas = "1";
        }
        else if (spincasprimegiant.getSelectedItemPosition() == 1) {
            cas = "2";
        }
        else if (spincasprimegiant.getSelectedItemPosition() == 2) {
            cas = "3";
        }
        totale.setText((Integer.toString(Integer.parseInt(qta)*Integer.parseInt(cas)) + "€"));

        super.onResume();
    }
}
