package com.szerszen.diabetex;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Klasa ekranu wyniku, wyświetla się po śmierci (funkcja gameOver(String s) w MainActivity.
 * Z klasy MainActivity są przekazywane dane o graczu aby gdy ten zdecyduje się grać,
 * mógł zacząć od swoich danych.
 * Klasa przetwarza wynik czasu na wynik w dniach i godzinach:
 * 10 sekund = 1 godzina
 * 4 minuty = 1 dzień
 * Created by Monika Ryczko on 1/12/18.
 * @author Monika Ryczko
 */

public class ScoreScreen extends AppCompatActivity {

    private Context context;
    private TextView score;
    private Button button_playAgain;

    String time;

    public String sex;
    public int weight;
    public int age;
    public int height;
    public String activity;
    public String cukrzyca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_screen);

        context = this;
        score = (TextView) findViewById(R.id.score);
        button_playAgain = (Button) findViewById(R.id.button_playAgain);

        Intent ii = getIntent();
        Bundle b = ii.getExtras();

        /**
         * Ustawiamy odpowiednie wartości, wartościami które podał gracz w settingach
         * a także ustawiamy odpowiednie flagi
         */
        if(b!=null) {
            time = (String) b.get("time");
            sex =(String) b.get("sex");
            cukrzyca =(String) b.get("cukrzyca");
            activity = (String) b.get("activity");
            weight = (int) b.get("weight");
            age = (int) b.get("age");
            height = (int) b.get("height");
        }

        /**
         * Z MainActivity przekazywana jest forma czasu granego przez gracza 00:00
         * trzeba zmodyfikować ten format to postani dni i godzin
         */
        String[] parts = time.split(":");
        int sec_number = Integer.parseInt(parts[0])*60 + Integer.parseInt(parts[1]);
        sec_number /= 10; //liczba godzina przeżytych przez gracza
        score.setText(sec_number/24 + " dni i \n" + sec_number + " godzin");

        /**
         * Po kliknięciu na guzik do MainActivity przekazane są stare dane gracza
         */
        button_playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(ScoreScreen.this, MainActivity.class);
                main.putExtra("setting", "tak");
                main.putExtra("restart", "nie");
                main.putExtra("sex", sex);
                main.putExtra("activity", activity);
                main.putExtra("weight", weight);
                main.putExtra("age", age);
                main.putExtra("height", height);
                main.putExtra("cukrzyca", cukrzyca);
                finish();
                startActivity(main);
            }
        });
    }
}
