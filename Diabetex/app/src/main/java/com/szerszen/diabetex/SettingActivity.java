package com.szerszen.diabetex;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Monika Ryczko on 1/7/18.
 * @author Monika Ryczko
 */

public class SettingActivity extends AppCompatActivity {

    private Context context;
    private Button button_OK;
    private TextView kcal_text;
    private TextView sex_text;
    private TextView activity_text;
    private TextView cukrzyca_text;
    private EditText kcal_field;
    private EditText height_field;
    private EditText age_field;
    private Spinner sex_spinner;
    private Spinner cukrzyca_spinner;
    private Spinner activity_spinner;
    private RadioButton tips;

    public String sex;
    public int weight;
    public int age;
    public int height;
    public String activity;
    public String cukrzyca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_list);

        context = this;
        final ConstraintSet set = new ConstraintSet();
        final ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.con_layout);

        button_OK = (Button) findViewById(R.id.button_OK);
        kcal_field = (EditText) findViewById(R.id.kcal_field);
        age_field = (EditText) findViewById(R.id.age_field);
        height_field = (EditText) findViewById(R.id.height_field);
        kcal_text = (TextView) findViewById(R.id.text1);
        cukrzyca_text = (TextView) findViewById(R.id.cukrzyca_text);
        activity_spinner = (Spinner) findViewById(R.id.activity_spinner);
        sex_spinner = (Spinner) findViewById(R.id.sex_spinner);
        cukrzyca_spinner = (Spinner) findViewById(R.id.cukrzyca_spinner);
        tips = (RadioButton) findViewById(R.id.radio_tips);

        /**
         * Wartość weight jest ustawiana po zmiane wartości w TextField,
         * pole to może przyjmować jedyne wartości int
         */
        kcal_field.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                weight = Integer.valueOf(kcal_field.getText().toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        /**
         * Wartość height jest ustawiana po zmiane wartości w TextField
         */
        height_field.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                height = Integer.valueOf(height_field.getText().toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        /**
         * Wartość age jest ustawiana po zmiane wartości w TextField
         */
        age_field.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                age = Integer.valueOf(age_field.getText().toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        sex_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                sex = sex_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                sex = "Kobieta";
            }
        });

        activity_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                activity = activity_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                activity = "Duza";
            }
        });

        cukrzyca_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                cukrzyca = cukrzyca_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                cukrzyca = "LADA";
            }
        });

        /**
         * Po naciśnięciu guzika przekazywane są do MainActivity dane ustawione przez gracza
         * i jednocześnie zmiana flagi setting na true, oznacza, że po kliknięciu OK gracz będzie
         * mógł wystartować grę
         */
        button_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent main = new Intent(SettingActivity.this, MainActivity.class);
                main.putExtra("sex", sex);  // pass your values and retrieve them in the other Activity using keyName
                main.putExtra("activity", activity);
                main.putExtra("weight", weight);
                main.putExtra("age", age);
                main.putExtra("height", height);
                main.putExtra("cukrzyca", cukrzyca);
                if(tips.isChecked()) {
                    main.putExtra("tips", "tak");
                } else {
                    main.putExtra("tips", "nie");
                }
                /**
                 * Przycisk nowej gry nie pokaże się jeśli gracz wpisze błędne dane
                 */
                if(age > 0 && weight > 0 && height > 0) {
                    main.putExtra("setting", "tak");
                    main.putExtra("restart", "nie");
                    finish(); //zabić SettingActivity
                    startActivity(main); //rozpoczynami MainActivity
                } else {
                    /**
                     * Jeśli gracz podał nie poprawne dane, zostanie wyświetlony komunikat,
                     * i dopóki gracz nie wpisze poprawnie danych
                     * nie powróci do menu głównego
                     */
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder.setTitle("UWAGA!")
                            .setMessage("Wszystkie pola są wymagane, podaj poprawne wartości dla\n" +
                                    "swojej wagi, wzrostu i wagi.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    main.putExtra("setting", "nie");
                                    main.putExtra("restart", "nie");
                                    finish(); //zabić SettingActivity
                                    startActivity(main); //rozpoczynami MainActivity
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            }
        });
    }

}
