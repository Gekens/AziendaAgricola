package com.example.giacomo.aziendaagricola;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Giacomo on 01/07/2016.
 */
public class Ordini extends Fragment {
    private ListView lv;
    MyCustomAdapter dataAdapter;
    SwipeRefreshLayout refresh;
    String check;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ordini, container, false);
        lv = (ListView) view.findViewById(R.id.listView2);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isInternetOn();
                new GetAllCustomerTask().execute(new ApiConnector());
            }
        });
        isInternetOn();
        new GetAllCustomerTask().execute(new ApiConnector());

        try {
            FileInputStream fileIn=getActivity().openFileInput("mytextfile.txt");
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
            check = cred[0];
            InputRead.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }



    public void setTextToTextView(JSONArray jsonArray) {
        int n = 0;
        String s = "";
        ArrayList<Orders> lista = new ArrayList<Orders>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject json = null;
                try {
                    n = jsonArray.length() - 1 - i;
                    json = jsonArray.getJSONObject(n);
                    if (check.equals(json.getString("mail"))) {


                        lista.add(new Orders(json.getString("image"), json.getString("qua"), json.getString("disp")));
                        dataAdapter = new MyCustomAdapter(getActivity(), R.layout.ordini_layout, lista);
                        // Assign adapter to ListView
                        lv.setAdapter(dataAdapter);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private class GetAllCustomerTask extends AsyncTask<ApiConnector, Long, JSONArray> {
        @Override
        protected JSONArray doInBackground(ApiConnector... params) {

            // it is executed on Background thread

            return params[0].GetAllOrders();
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            setTextToTextView(jsonArray);
            refresh.setRefreshing(false);


        }
    }

    private class MyCustomAdapter extends ArrayAdapter<Orders> {

        private ArrayList<Orders> lista;

        public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<Orders> lista) {
            super(context, textViewResourceId, lista);
            this.lista = new ArrayList<Orders>();
            this.lista.addAll(lista);
        }

        private class ViewHolder {
            TextView qualità, disponibilità;
            ImageView image;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.ordini_layout, null);

                holder = new ViewHolder();
                holder.qualità = (TextView) convertView.findViewById(R.id.textViewQua);
                holder.disponibilità = (TextView) convertView.findViewById(R.id.textViewDisp);
                holder.image = (ImageView) convertView.findViewById(R.id.imageViewIcon);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Orders prod = lista.get(position);
            holder.qualità.setText(prod.getQualità());
            holder.disponibilità.setText(prod.getDisponibilità());
            Picasso.with(getContext()).load(prod.getImage()).into(holder.image);
            holder.qualità.setTag(prod);
            holder.disponibilità.setTag(prod);

            return convertView;
        }
    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            Toast.makeText(getActivity(), " Connessione Assente ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }
}
