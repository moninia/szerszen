package com.szerszen.diabetex;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Movie;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Klasa aktywności dla wyświetlania listy produktów. Dostępne są również trzy przyciski
 * dzięki którym można posortować listę alfabetycznie, w zależności od IG lub kalorii.
 * Tworzone są tutaj tablice z nazwami, obrazkami, kaloriami i IG wszystkich produktów,
 * a także ListView z każdym rzędem.
 * Created by Monika Ryczko on 1/7/18.
 * @author Monika Ryczko
 */

public class ListActivity extends AppCompatActivity {

    String[] product_names;
    String[] product_kcal;
    String[] product_IG;
    TypedArray product_pics;

    List<RowItem> rowItems;
    ListView mylistview;

    Button button_sortName;
    Button button_sortIG;
    Button button_sortKcal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        rowItems = new ArrayList<RowItem>();

        button_sortName = (Button) findViewById(R.id.sort_alpha);
        button_sortIG = (Button) findViewById(R.id.sort_IG);
        button_sortKcal = (Button) findViewById(R.id.sort_kcal);

        product_names = getResources().getStringArray(R.array.product_nameArray);
        product_IG = getResources().getStringArray(R.array.product_IGArray);
        product_kcal = getResources().getStringArray(R.array.product_kcalArray);
        product_pics = getResources().obtainTypedArray(R.array.product_picArray);

        mylistview = (ListView) findViewById(R.id.list);
        mylistview.setFastScrollEnabled(true);

        /**
         * Pętla w której tworzymy tyle rzędów ile jest nazw produktów,
         * oznacza to, że wszystkich danych w każdej tablicy zawartej w
         * string.xml musi być po tyle samo.
         */
        for (int i = 0; i < product_names.length; i++) {
            final RowItem item = new RowItem(product_names[i],
                    product_pics.getResourceId(i, -1), product_kcal[i], product_IG[i]);
            rowItems.add(item);
        }

        final CustomAdapter adapter = new CustomAdapter(this, rowItems);
        mylistview.setAdapter(adapter);

        /**
         * Każdy z przycisków posiada swoją funkcję onClick, po kliknięciu
         * lista zostaje posortowana w zależności od wybranej opcji przy użyciu odpowiedniego
         * komparatora
         */
        button_sortName.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                rowItems.sort(new Comparator<RowItem>() {
                    @Override
                    public int compare(RowItem o1, RowItem o2) {
                        return o1.getProduct_name().compareTo(o2.getProduct_name());
                    }
                });
                mylistview.setAdapter(adapter);
            }
        });

        button_sortIG.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                rowItems.sort(new Comparator<RowItem>() {
                    @Override
                    public int compare(RowItem o1, RowItem o2) {
                        return Double.valueOf(o1.getProduct_IG()).compareTo(Double.valueOf(o2.getProduct_IG()));
                    }
                });
                mylistview.setAdapter(adapter);
            }
        });

        button_sortKcal.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                rowItems.sort(new Comparator<RowItem>() {
                    @Override
                    public int compare(RowItem o1, RowItem o2) {
                        return Integer.valueOf(o1.getProduct_kcal()).compareTo(Integer.valueOf(o2.getProduct_kcal()));
                    }
                });
                mylistview.setAdapter(adapter);
            }
        });

    }

}
