package com.qualidadhumana.applicacion.Fragments.FragmentosIniciales;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.qualidadhumana.applicacion.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Armando Acevedo on 10/2/2017.
 */

public class CampusVirtual extends Fragment {

    private String URL = "http://qualidadhumana.com/campusvirtual/index.php";

    private JSONObject jsonObject = new JSONObject();
    private ArrayList<TextInputLayout> inputLayouts = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.campus_virtual, container, false);

        final RequestQueue queue = Volley.newRequestQueue(getContext());

        inputLayouts.add((TextInputLayout) view.findViewById(R.id.textInput_Cam_nom));
        inputLayouts.add((TextInputLayout) view.findViewById(R.id.textInput_Cam_cont));


        (view.findViewById(R.id.button_cam_enviar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificarDatos(inputLayouts)) {

                    peticionPost(queue);

                }
            }
        });


        return view;
    }

    private void peticionPost(final RequestQueue queue) {
        try {
            jsonObject.put("nombre", inputLayouts.get(0).getEditText().getText().toString())
                    .put("password", inputLayouts.get(1).getEditText().getText().toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getContext(), "Se envió correctamente", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error al enviar información", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);

    }


    private boolean verificarDatos(ArrayList<TextInputLayout> editTexts) {

        boolean flat = true;

        for (int i = 0; i < editTexts.size(); i++) {
            editTexts.get(i).setError("");
            if (editTexts.get(i).getEditText().getText().toString().equals("")) {
                editTexts.get(i).setError("Complete este campo");
                flat = false;
            }
        }

        return flat;
    }
}
