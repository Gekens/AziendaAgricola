package com.example.giacomo.aziendaagricola;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Created by Giacomo on 28/06/2016.
 */
public class Contatti extends Fragment{
    WebView webView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contatti, container, false);
        webView = (WebView) view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        String customHtml = "<html><body><script src='https://maps.googleapis.com/maps/api/js?v=3.exp'></script><div style='overflow:hidden;height:440px;width:100%;'><div id='gmap_canvas' style='height:440px;width:100%;'></div><div><small><a href=\"http://embedgooglemaps.com\">\t\t\t\t\t\t\t\t\tgoogle maps html\t\t\t\t\t\t\t</a></small></div><div><small><a href=\"http://www.multihostersreview.com/\">foxleech premium account</a></small></div><style>#gmap_canvas img{max-width:none!important;background:none!important}</style></div><script type='text/javascript'>function init_map(){var myOptions = {zoom:10,center:new google.maps.LatLng(45.4441283,11.58707359999994),mapTypeId: google.maps.MapTypeId.ROADMAP};map = new google.maps.Map(document.getElementById('gmap_canvas'), myOptions);marker = new google.maps.Marker({map: map,position: new google.maps.LatLng(45.4441283,11.58707359999994)});infowindow = new google.maps.InfoWindow({content:'<strong>Azienda Agricola Margherita</strong><br>Castegnero, Calcare 3<br>'});google.maps.event.addListener(marker, 'click', function(){infowindow.open(map,marker);});infowindow.open(map,marker);}google.maps.event.addDomListener(window, 'load', init_map);</script></body></html>";
        webView.loadData(customHtml, "text/html", "UTF-8");
        return view;
    }
}
