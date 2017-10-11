package com.qualidadhumana.applicacion.Fragments.FragmentosIniciales;

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
import com.qualidadhumana.applicacion.Fragments.FragmentosIniciales.FragmentosNosotros.NosotrosVistas;
import com.qualidadhumana.applicacion.R;

import java.util.ArrayList;

/**
 * Created by Armando Acevedo on 9/27/2017.
 */

public class NosotrosFragment extends Fragment implements View.OnClickListener {

    public interface OnNosotrosFragment {
        void onCambiarSubtitulo(String subtitulo);

    }

    OnNosotrosFragment listener;

    private ArrayList<ImageView> imageViews = new ArrayList<>();

    private int[] idsText = {R.id.recycler_text_1, R.id.recycler_text_2, R.id.recycler_text_3, R.id.recycler_text_4};

    private int[] idsImages = {R.id.img_izq_1, R.id.img_izq_2, R.id.img_izq_3, R.id.img_izq_4};

    private ArrayList<String> enlaces_img = new ArrayList<>();

    private Bundle arg = new Bundle();

    private ArrayList<String> reultado_final = new ArrayList<>();


    public NosotrosFragment() {
        setArguments(new Bundle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.nosotros_fragment, container, false);

        int[] idsTexts_ = getArguments().getIntArray("idsText");

        String[] textButtons = getResources().getStringArray(R.array.nosotros);

        for (int i = 0; i < idsText.length; i++) {
            imageViews.add((ImageView) view.findViewById(idsImages[i]));
        }

        enlaces_img.add(getString(R.string.img_quines_somos2));
        enlaces_img.add(getString(R.string.img_mision_ok));
        enlaces_img.add(getString(R.string.img_vision_ok));
        enlaces_img.add(getString(R.string.img_valores_ok));

//        if (reultado_final != null) {
        if (idsTexts_ != null) {
            for (int i = 0; i < textButtons.length; i++) {
                ((TextView) view.findViewById(idsTexts_[i])).setText(textButtons[i]);
            }
        } else {
            for (int i = 0; i < textButtons.length; i++) {
                ((TextView) view.findViewById(idsText[i])).setText(textButtons[i]);
            }
        }
//
//                if (getArguments().getStringArrayList("result_inicio") != null) {
//
//                    ArrayList<String> result_inicio = getArguments().getStringArrayList("result_inicio");
//
//                    for (int i = 1; i < 5; i++) {
//                        reultado_final.add(result_inicio.get(i));
//                        ((TextView) view.findViewById(idsText[i - 1])).setText(result_inicio.get(i));
//                    }
//                }
//                arg.putStringArrayList("titulos", reultado_final);
//                arg.putStringArrayList("parrafos", getArguments().getStringArrayList("parrafos"));

//            }
//        }

        cargarImagenes();

        (view.findViewById(R.id.cardViewNos1)).setOnClickListener(this);
        (view.findViewById(R.id.cardViewNos2)).setOnClickListener(this);
        (view.findViewById(R.id.cardViewNos3)).setOnClickListener(this);
        (view.findViewById(R.id.cardViewNos4)).setOnClickListener(this);


        return view;
    }


    private void cargarImagenes() {
        for (int i = 0; i < imageViews.size(); i++) {
            Glide.with(this).load(enlaces_img.get(i))
                    .placeholder(R.drawable.ic_child_care)
                    .override(50, 50)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViews.get(i));

        }
    }

    @Override
    public void onClick(View view) {

        Fragment fragment = new NosotrosVistas();

        int i = -1;

        switch (view.getId()) {
            case R.id.cardViewNos1:
                i = 1;
                break;
            case R.id.cardViewNos2:
                i = 2;
                break;
            case R.id.cardViewNos3:
                i = 3;
                break;
            case R.id.cardViewNos4:
                i = 4;
                break;
            default:
                break;
        }

//        arg.putBoolean("canGoBack", false);
        arg.putInt("select", i);
        fragment.setArguments(arg);
        getFragmentManager().beginTransaction().replace(R.id.conteiner, fragment)
                .addToBackStack(fragment.getTag()).commit();

    }

    @Override
    public void onPause() {
        super.onPause();
        getArguments().putIntArray("idsText", idsText);
    }

    @Override
    public void onResume() {
        super.onResume();
        listener.onCambiarSubtitulo("");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (OnNosotrosFragment) getActivity();
        } catch (ClassCastException ex) {
            throw new ClassCastException(getActivity().toString() + " no se implementó la interfaz OnNosotrosFragment");
        }
    }
}
