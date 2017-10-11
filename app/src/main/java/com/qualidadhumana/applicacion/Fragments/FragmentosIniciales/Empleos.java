package com.qualidadhumana.applicacion.Fragments.FragmentosIniciales;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qualidadhumana.applicacion.R;


/**
 * Created by Armando Acevedo on 10/3/2017.
 */

public class Empleos extends Fragment {

    private TextView textView;
    private final String URL = "http://qualidadhumana.com/index.php/empleos?format=json";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.empleos, container, false);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        textView = view.findViewById(R.id.textView_empleos);

        //Poner esto en la actividad
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        textView.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                try {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
//
//                } catch (ExceptionInInitializerError e) {
//                    e.printStackTrace();
//                }
            }
        });

        queue.add(stringRequest);

        return view;
    }
}
