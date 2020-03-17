package com.example.qtq;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import java.io.FileReader;
import org.json.*;
import org.json.simple.parser.JSONParser;
import org.apache.http.client.methods.HttpGet;

public class Fragment_Questionnaire extends Fragment {
    Button btnValider;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_fragment__questionnaire,container,false);

        btnValider =(Button)view.findViewById(R.id.btnValide);
        btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr=getFragmentManager().beginTransaction();
                fr.replace(R.id.frmLQuestionnaire,new Fragment_Res());
                fr.commit();
            }
        });

        JSONParser parser = new JSONParser();


        return view;
    }

    RequestQueue queue = Volley.newRequestQueue(this);
    String url ="http://v1/siri/2.0/estimated-timetable";

    // Request a string response from the provided URL.
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the first 500 characters of the response string.
                    textView.setText("Response is: "+ response.substring(0,500));
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            textView.setText("That didn't work!");
        }
    });

// Add the request to the RequestQueue.
queue.add(stringRequest);

}
