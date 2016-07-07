package com.example.giacomo.aziendaagricola;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giacomo on 27/06/2016.
 */
public class ListFragment extends Fragment {
    private ListView lv;
    MyCustomAdapter dataAdapter;
    SwipeRefreshLayout refresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list, container, false);
        lv = (ListView) view.findViewById(R.id.listView);
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
        return view;
    }

    public void setTextToTextView(JSONArray jsonArray) {
        int n = 0;
        String s = "";
        ArrayList<Prodotti> lista = new ArrayList<Prodotti>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject json = null;
                try {
                    n = jsonArray.length() - 1 - i;
                    json = jsonArray.getJSONObject(n);
                    lista.add(new Prodotti(json.getString("image"), json.getString("qualita"), json.getString("disponibilita")));
                    dataAdapter = new MyCustomAdapter(getActivity(), R.layout.list_layout, lista);
                    // Assign adapter to ListView
                    lv.setAdapter(dataAdapter);

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

            return params[0].GetAllCustomers();
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            setTextToTextView(jsonArray);
            refresh.setRefreshing(false);


        }
    }

    private class MyCustomAdapter extends ArrayAdapter<Prodotti> {

        private ArrayList<Prodotti> lista;

        public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<Prodotti> lista) {
            super(context, textViewResourceId, lista);
            this.lista = new ArrayList<Prodotti>();
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
                convertView = vi.inflate(R.layout.list_layout, null);

                holder = new ViewHolder();
                holder.qualità = (TextView) convertView.findViewById(R.id.textViewQualità);
                holder.disponibilità = (TextView) convertView.findViewById(R.id.textViewDisponibilità);
                holder.image = (ImageView) convertView.findViewById(R.id.imageViewIcon);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Prodotti prod = lista.get(position);
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
