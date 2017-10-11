package com.qualidadhumana.applicacion.Fragments.FragmentosIniciales.FragmentosNosotros;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qualidadhumana.applicacion.R;

import java.util.ArrayList;

/**
 * Created by Armando Acevedo on 9/27/2017.
 */

public class NosotrosVistas extends Fragment {

    public interface OnNosotrosVistas {
        void onCambiarSubtitulo(String subtitulo);
    }

    OnNosotrosVistas listener;

    private TextView textView;

    private ImageView imageView;

    private ArrayList<String> parrafos_select = new ArrayList<>();

    private ArrayList<String> enlaces_img = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.nosotros_vistas, container, false);

        enlaces_img.add(getString(R.string.img_quines_somos2));
        enlaces_img.add(getString(R.string.img_mision_ok));
        enlaces_img.add(getString(R.string.img_vision_ok));
        enlaces_img.add(getString(R.string.img_valores_ok));

        textView = view.findViewById(R.id.txtView_nosotros);
        imageView = view.findViewById(R.id.imageView_nosotros);

        ArrayList<String> titulos = new ArrayList<>();
//        titulos = getArguments().getStringArrayList("titulos");
        titulos.add(getString(R.string.quienes_somos_ti));
        titulos.add(getString(R.string.mision_ti));
        titulos.add(getString(R.string.vision_ti));
        titulos.add(getString(R.string.vision_ti));

//        parrafos_select.add(getString(R.string.quienes_somos_text));
//        parrafos_select.add(getString(R.string.mision_text));
//        parrafos_select.add(getString(R.string.vision_text));
//        parrafos_select.add(getString(R.string.valores_text));

        int min = 0, max = 0;
        int i = 0;
        switch (getArguments().getInt("select")) {
            case 1:
                min = 0;
                max = 3;
                listener.onCambiarSubtitulo(titulos.get(0));
                break;
            case 2:
                min = 3;
                max = 4;
                listener.onCambiarSubtitulo(titulos.get(1));
                break;
            case 3:
                min = 4;
                max = 5;
                listener.onCambiarSubtitulo(titulos.get(2));
                break;
            case 4:
                min = 5;
                max = 10;
                listener.onCambiarSubtitulo(titulos.get(3));
                break;
        }

        seleccionarParrafos(min, max);
        cargarImagen(getArguments().getInt("select"));
//        textView.setText(parrafos_select.get(getArguments().getInt("select") - 1));

        return view;
    }

    private void cargarImagen(int select) {
        Glide.with(this).load(enlaces_img.get(select - 1))
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    private void seleccionarParrafos(int min, int max) {
////        parrafos_select = getArguments().getStringArrayList("parrafos");
        String[] parrafos = getActivity().getResources().getStringArray(R.array.parrafos);

//        if (parrafos_select != null && !parrafos_select.isEmpty()) {
//            String parrafos = "";
//            for (int i = min; i < max; i++) {
//                parrafos += parrafos_select.get(i) + "\n";
//            }
//
//            textView.setText(parrafos);
//        }

            String parrafosSelect = "";
            for (int i = min; i < max; i++) {
                parrafosSelect += parrafos[i] + "\n";
            }

            textView.setText(parrafosSelect);



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (OnNosotrosVistas) getActivity();
        } catch (ClassCastException ex) {
            throw new ClassCastException(getActivity().toString() + " no se implementó la interfaz OnNosotrosVistas");
        }
    }
}
