package com.qualidadhumana.applicacion.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qualidadhumana.applicacion.Activities.MainActivity;
import com.qualidadhumana.applicacion.Fragments.FragmentosIniciales.BuscoChamba;
import com.qualidadhumana.applicacion.Fragments.FragmentosIniciales.Contactos;
import com.qualidadhumana.applicacion.Fragments.FragmentosIniciales.CampusVirtual;
import com.qualidadhumana.applicacion.Fragments.FragmentosIniciales.Empleos;
import com.qualidadhumana.applicacion.Fragments.FragmentosIniciales.NosotrosFragment;
import com.qualidadhumana.applicacion.R;

import java.util.ArrayList;


/**
 * Created by Armando Acevedo on 23/8/2017.
 */

public class MenuInicialFragment extends Fragment implements View.OnClickListener {

    private int[] idsText = {R.id.initext1, R.id.initext2, R.id.initext3, R.id.initext4, R.id.initext5};

    private int[] idsImages = {R.id.img_izq_ini1, R.id.img_izq_ini2, R.id.img_izq_ini3, R.id.img_izq_ini4, R.id.img_izq_ini5};

    private int[] idsImgDraw = {R.drawable.ic_group, R.drawable.ic_work, R.drawable.ic_ondemand_video,
            R.drawable.account_search, R.drawable.ic_contact_mail};

    private Bundle arg = new Bundle();

    private ArrayList<String> arrayList = new ArrayList<>();

    public MenuInicialFragment() {
        setArguments(new Bundle());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.menu_inicial_fragment, container, false);

        String[] iniciales = getResources().getStringArray(R.array.inicial);


//        if (getArguments() != null) {
        if (getArguments().getIntArray("idsText") != null && getArguments().getIntArray("idsImages") != null) {
            int[] idsTexts_ = getArguments().getIntArray("idsText");
            int[] idsImages_ = getArguments().getIntArray("idsImages");

            if (!arrayList.isEmpty() && idsTexts_ != null && idsImages_ != null) {
                for (int i = 0; i < iniciales.length; i++) {
                    ((TextView) view.findViewById(idsTexts_[i])).setText(iniciales[i]);
                    ((ImageView) view.findViewById(idsImages_[i])).setImageDrawable(getResources()
                            .getDrawable(idsImgDraw[i]));
                }
            }
        } else {

//            ArrayList<String> result_inicio = getArguments().getStringArrayList("result_inicio");
//            arrayList.add(result_inicio.get(44));
//            for (int i = 12; i < 17; i++) {
//                arrayList.add(result_inicio.get(i));
//            }
//            arrayList.remove(4);

            for (int i = 0; i < iniciales.length; i++) {
                ((TextView) view.findViewById(idsText[i])).setText(iniciales[i]);
                ((ImageView) view.findViewById(idsImages[i])).setImageDrawable(getResources()
                        .getDrawable(idsImgDraw[i]));
            }

//            arg.putStringArray("result_inicio", iniciales);
//            arg.putStringArrayList("parrafos", getArguments().getStringArrayList("parrafos"));
        }
//        }


        (view.findViewById(R.id.cardViewIni1)).setOnClickListener(this);
        (view.findViewById(R.id.cardViewIni2)).setOnClickListener(this);
        (view.findViewById(R.id.cardViewIni3)).setOnClickListener(this);
        (view.findViewById(R.id.cardViewIni4)).setOnClickListener(this);
        (view.findViewById(R.id.cardViewIni5)).setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View view) {

        Fragment fragment = null;

        int i = -1;

        switch (view.getId()) {
            case R.id.cardViewIni1:
                i = 0;
                fragment = new NosotrosFragment();
                break;
            case R.id.cardViewIni2:
                i = 1;
                fragment = new MainActivity.MyWebViewFragment();
                arg.putInt("idWebs", i - 1);
//                fragment = new Empleos();
                break;
            case R.id.cardViewIni3:
                i = 2;
                fragment = new CampusVirtual();
                break;
            case R.id.cardViewIni4:
                i = 3;
                fragment = new MainActivity.MyWebViewFragment();
                arg.putInt("idWebs", i - 1);
//                fragment = new BuscoChamba();
                break;
            case R.id.cardViewIni5:
                i = 4;
                fragment = new Contactos();
                break;
            default:
                break;

        }

//        if (i == 0) {
//            fragment = new NosotrosFragment();
//            arg.putBoolean("canGoBack", false);
//        } else {
//            fragment = new MainActivity.MyWebViewFragment();
//            arg.putBoolean("canGoBack", true);
//        arg.putInt("idWebs", i - 1);
//        }

        fragment.setArguments(arg);
        getFragmentManager().beginTransaction().replace(R.id.conteiner, fragment)
                .addToBackStack(fragment.getTag()).commit();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (getArguments().getIntArray("idsText") != null && getArguments().getIntArray("idsImages") != null) {
            getArguments().putIntArray("idsImages", idsImages);
            getArguments().putIntArray("idsText", idsText);
        }


    }

}
