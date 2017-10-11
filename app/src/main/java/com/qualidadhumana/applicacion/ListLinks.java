package com.qualidadhumana.applicacion;

/**
 * Created by Armando Acevedo on 9/26/2017.
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

;

/**
 * Example program to list links from a URL.
 */
public class ListLinks {

    public ArrayList<String> parseHTML(String url, String key) throws IOException {

        ArrayList<String> result = new ArrayList<>();


        Document doc = Jsoup.connect(url).get();

        Elements seleccion = doc.select(key);

        for (Element link : seleccion) {
            if (!link.text().equals(""))
                if (link.text().codePointAt(0) != 160)
                    result.add(trim(link.text(), link.text().length()));
        }

        return result;
    }


    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width - 1) + ".";
        else
            return s;
    }
}
