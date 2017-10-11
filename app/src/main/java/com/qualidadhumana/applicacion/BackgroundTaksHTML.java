package com.qualidadhumana.applicacion;


import android.os.AsyncTask;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Armando Acevedo on 9/6/2017.
 */

public class BackgroundTaksHTML extends AsyncTask<String, Void, ArrayList<String>> {

    public interface OnTaskExcute {
        void onParseHTML(ArrayList<String> result);
    }

    public OnTaskExcute delegate = null;


    public BackgroundTaksHTML(OnTaskExcute onTaskExcute) {
        this.delegate = onTaskExcute;
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        String url = params[0];
        String key = params[1];
        ArrayList<String> result = new ArrayList<>();

        try {
            result = new ListLinks().parseHTML(url, key);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;


    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {

        delegate.onParseHTML(result);


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


}
