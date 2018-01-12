package com.szerszen.diabetex;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

/**
 * Główna klasa gry, w której pokazuje się plansza i rozgrywa się gra.
 * Plansza gry składa się z ikony gracza, dwóch pasków głodu i insuliny,
 * przycisków menu, "domku" dla gracza, timera i jeśli gracz wybrał funkcję gry
 * z podpowiedziami to tekstu podpowiedzi
 * @author Monika Ryczko
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Liczba porduktów, liczba ta powinna być taka sama jak liczba podana w strings.xml
     */
    private int image_number = 3;

    /**
     * Liczba kalorii oblicza na podstawie danych gracza
     */
    private int kcal;

    /**
     * Defaultowe dane gracza, nie są wykorzystywane chyba, że zostanie
     * zmieniona w kodzie setting_flag na true;
     */
    public String sex = "Kobieta";
    public int weight = 50;
    public int age = 25;
    public int height = 160;
    public String activity = "Duza";
    public String cukrzyca = "LADA";

    /**
     * Elementy gry widoczne przez gracza
     */
    public Button button_exit;
    public Button button_restart;
    public Button button_pause;
    public Button button_newgame;
    public Button button_list;
    public Button button_setting;

    private Context context;
    private TextView text_timer;
    private Thread timer_th;
    private Timer timer;

    private ProgressBar progressBar;
    private ProgressBar progressBar_insuline;

    private ImageView player;
    private ImageView domek;

    String[] product_names;
    String[] product_kcal;
    String[] product_IG;
    TypedArray product_pics;

    /**
     * Pomocnicza zmienna wyświetlająca aktualny stan kalorii
     */
    private TextView score;

    public int current_score = 0;
    public int insuline_score = 0;
    public boolean pause_flag = true;

    /**
     * Jeśli setting_flag = false
     * gracz nie podał danych w settingu czyli przyciski NewGame i Restart są niedostępne
     * Jeśli setting_flag = true
     * wszystkie przyciski są dostępne (na wstępie oprócz restart - ten przycisk jest zależny od restart_flag;
     */
    public boolean setting_flag = false;

    /**
     * Jeśli restart_flag = false
     * gracz nie uruchomił nowej gdy i nie ma czego restartować
     * Jeśli restart_flag = true
     * gracz posiada opcję restartu zatrzymanej gry
     */
    public boolean restart_flag = false;

    @SuppressLint("ClickableViewAccessibility")

    /**
     * Tablica zawierająca ImageViews
     */
    private ImageView[] image_table = new ImageView[image_number];


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        context = this;
        final ConstraintSet set = new ConstraintSet();
        final ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.con_layout);

        button_exit = (Button) findViewById(R.id.button_exit);
        button_restart = (Button) findViewById(R.id.button_restart);
        button_pause = (Button) findViewById(R.id.button_pause);
        button_list = (Button) findViewById(R.id.button_list);
        button_setting = (Button) findViewById(R.id.button_setting);
        button_newgame = (Button) findViewById(R.id.button_newgame);

        player = (ImageView) findViewById(R.id.player);
        domek  = (ImageView) findViewById(R.id.domek);

        text_timer = (TextView) findViewById(R.id.text_timer);
        score = (TextView) findViewById(R.id.score);

        progressBar = (ProgressBar) findViewById(R.id.bar_hunger);
        progressBar_insuline = (ProgressBar) findViewById(R.id.bar_insulin);

        /**
         * Pobieramy wartości do tablic nazw, kalorii, IG i wartosci ImageView
         * z pliku strings.xml. Do wartosci z tych tablic (tekstowe są stringach
         * zaś ImageView jest TyppedArray) odwołujemy się poprzez indeks
         */
        product_names = getResources().getStringArray(R.array.product_nameArray);
        product_IG = getResources().getStringArray(R.array.product_IGArray);
        product_kcal = getResources().getStringArray(R.array.product_kcalArray);
        product_pics = getResources().obtainTypedArray(R.array.product_picArray);

        pause_flag = true; //na poczatku pausa jest wlaczona
        button_pause.setVisibility(View.INVISIBLE); //przycisk pauzy jest ukryty

        /**
         * Jedyne dane jakie przekazujemy przez extras są z SettingActivity
         * i są to dane wprowadzane przez gracza
         */
        Intent ii = getIntent();
        Bundle b = ii.getExtras();

        /**
         * Ustawiamy odpowiednie wartości, wartościami które podał gracz w settingach
         * a także ustawiamy odpowiednie flagi
         */
        if(b!=null)
        {
            int tmp_i;
            String tmp_s;
            tmp_s =(String) b.get("sex");
            sex = tmp_s;
            tmp_s =(String) b.get("cukrzyca");
            cukrzyca = tmp_s;
            tmp_s = (String) b.get("activity");
            activity = tmp_s;
            tmp_i = (int) b.get("weight");
            weight = tmp_i;
            tmp_i = (int) b.get("age");
            age = tmp_i;
            tmp_i = (int) b.get("height");
            height = tmp_i;
            tmp_s = (String) b.get("setting");
            if(tmp_s.equals("tak")) {
                setting_flag = true;
            } else {
                setting_flag = false;
            }
        }
        showMenu(); //następuje pokazanie menu w zależności od flag
        /**
         * Tworząc główną klasę gry na początku obliczamy potrzebne zapotrzebowanie kaloryczne
         * z nowymi danymi otrzymanymi z setting, setting_flag powoduje, że dopuki gracz
         * nie ustawi swoich danych, nie będzie w stanie uruchomić gry
         */
        double act;
        switch(activity) {
            case "Duza": act = 2.0;
                break;
            case "Srednia": act = 1.7;
                break;
            case "Mala": act = 1.4;
                break;
            default: act = 1.1;
                break;
        }
        if(sex.equals("Kobieta")) {
            //wzor obliczajacy BMR dla kobiet to 655 + [9,6 x masa ciała (kg)] + [1,8 x wzrost (cm)] - [4,7 x wiek (lata)]
            kcal = (int) ((655 + weight*9.6 + height*1.8 - age*4.7)*act);
        } else if(sex.equals("Mezczyzna")) {
            //wzor obliczajacy BMR dla mezczyzn to 66 + [13,7 x masa ciała (kg)] + [5 x wzrost (cm)] - [6,76 x wiek (lata)]
            kcal = (int) ((66 + weight*13.7 + height*5 - age*6.76)*act);
        } else {
            //wzor obliczajacy BMR dla innych, nieokreslonych, czyli wzór uproszczony
            kcal = (int) (weight*24 * act);
        }

        /**
         * Counter dzięki, któremu jedzenie spada co 5 sekund,
         * wybrane jedzenie jest randomowe i spada z randomowego miejsca
         */
        new CountDownTimer(5000,5000) {
            int j=0;
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                if(!pause_flag) {
                    /**
                     * Najpierw losowany jest indeks w tablicy product_pics,
                     * potem tworzony jest obiekt, który pojawia się losowym miejscu
                     * na szczycie ekranu po czym opada w dół
                     */
                    j = new Random().nextInt(product_names.length);
                    //final ImageView view = image_table[j];
                    ImageView view = new ImageView(getApplicationContext());
                    view.setImageResource(product_pics.getResourceId(j, -1));
                    layout.addView(view, 0);
                    set.clone(layout);
                    set.setHorizontalBias(view.getId(), 20 * (j + 10));
                    set.applyTo(layout);

                    view.getLayoutParams().height = 150; //rozmiary jedzenia
                    view.getLayoutParams().width = 150;
                    view.setX(new Random().nextInt(400) + 240); // zmiana zakresu gdzie spada jedzenie [240,400] px

                    view.setScaleType(ImageView.ScaleType.FIT_XY);

                    doCustomAnimationToPlayer((ImageView) view, j);
                    ((MainActivity) context).score.setText(String.valueOf(current_score)); //odświeżanie paska jedzenia
                    j = (j + 1) % image_number;
                }
                start();
            }
        }.start();

        /**
         * Pasek głodu spada co 2 sekundy o 15 kalorii jeśli w domku to o 5,
         * gracz musi uważać, żeby głód nie spadł do 0
         * TO DO INSULINA
         */
        new CountDownTimer(2000,2000) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                if(!pause_flag) {
                    if(player.getX() < getScreenWidth() && player.getX() > getScreenWidth() - 385) {
                        current_score -= 5;
                    } else {
                        current_score -= 15;
                    }
                    insuline_score -= 5;
                    score.setText(String.valueOf(current_score));
                    /**
                     * Jednym ze sposobów żeby umrzeć jest zagłodzenie się, jeśli głód gracza
                     * spadnie do 0, umiera
                     */
                    if(current_score <= 0) {
                        gameOver("Zagłodziłeś się!");
                    }
                    /**
                     * Do postawienia diagnozy hipoglikemii konieczne jest spełnienie jednocześnie
                     * 3 warunków (tzw. triada Whipple’a):
                     * • poziom glukozy poniżej 40 mg/dL (2.2 mmol/L)
                     * • występowanie objawów hipoglikemii
                     * • wycofanie się tych objawów po przyjęciu glukozy
                     */
                    if(insuline_score <= 40) {
                        gameOver("Hipoglikemia!");
                    }
                }
                start();
            }
        }.start();

        /**
         * Paski są odświeżane co 0.2 sekundy
         */
        new CountDownTimer(200,200) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                if(!pause_flag) {
                    progressBar_insuline.setProgress(insuline_score);
                    progressBar.setProgress(current_score);
                }
                start();
            }
        }.start();

        /**
         * Przycisk ListProduct otwiera nową aktywność
         * @see ListActivity
         */
        button_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent list_page = new Intent(MainActivity.this, ListActivity.class); //tworzenie nowej aktywność
                startActivity(list_page); //uruchomienie stworzonej aktywności
            }
        });

        /**
         * Przycisk Setting otwiera nową aktywność
         * @see SettingActivity
         */
        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_page = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(setting_page);
            }
        });

        button_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause_flag = false;
                resumeTimer();
                hideMenu();
                button_pause.setVisibility(View.VISIBLE);
            }
        });

        /**
         * Wraz z kliknieciem przycisku nowa gra przełączamy się z trybu pauzy,
         * ustawiamy paski głodu i insuliny na odpowiednie wartości zależne
         * od wcześniej wpisanych przez gracza wartosci w Settingach.
         * Ustawiamy wartości score są to wartości pomocnicze dla pasków.
         * Wraz z nową grę reseujemy Timer i uruchamiamy go na nowo,
         * ukrywamy Menu i pokazujemy przycisk pauzy.
         */
        button_newgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause_flag = false;
                current_score = kcal-(kcal/10);
                insuline_score = 250;

                progressBar.setProgress(kcal-(kcal/10)); //poziom głodu na stracie wynosi 90% maksymalnej wartości
                progressBar.setMax(kcal);

                progressBar_insuline.setProgress(250); //poziom insuliny jest w połowie czyli taki jaki powinniśmy trzymać
                progressBar_insuline.setMax(500);

                player.setX(getScreenWidth()/2 - 100); //ustaw gracza na poycji początkowej

                resetTimer();
                startTimer();
                hideMenu();
                button_pause.setVisibility(View.VISIBLE);
            }
        });

        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        button_pause.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                pause_flag = true;
                restart_flag = true;
                button_pause.setVisibility(View.INVISIBLE);
                showMenu();
                pauseTimer();

            }
        });

        /**
         * Graczem możemy poruszać poprzez dotyk i przesunięcie gracza w zdłuż osi X.
         * Gracz może poruszać się jedynie w swoim polu.
         * Gdy gracz wejdzie do "domku" zostaje uaktualniony pasek insuliny.
         */
        player.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_MOVE: {
                        /**
                         * Kolizja ze ścianami
                         */
                        if((int) event.getRawX() > 200 && (int) event.getRawX() < getScreenWidth()- 200) {
                            player.setX((int) event.getRawX());
                        } else if((int) event.getRawX() <= 160 ){
                            player.setX(200);
                        } else if((int) event.getRawX() >= getScreenWidth()) {
                            player.setX(getScreenWidth()-200);
                        }
                    }
                }
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Ukrywanie gracza, pasków głodu i insuliny,
     * jednocześnie odkrycie przycisków menu
     */
    private void showMenu() {
        if(setting_flag) {
            button_restart.setVisibility(View.VISIBLE);
            button_newgame.setVisibility(View.VISIBLE);
            findViewById(R.id.text_newgame).setVisibility(View.VISIBLE);
            findViewById(R.id.text_restart).setVisibility(View.VISIBLE);
        } else {
            button_restart.setVisibility(View.INVISIBLE);
            button_newgame.setVisibility(View.INVISIBLE);
            findViewById(R.id.text_newgame).setVisibility(View.INVISIBLE);
            findViewById(R.id.text_restart).setVisibility(View.INVISIBLE);
        }
        button_list.setVisibility(View.VISIBLE);
        button_setting.setVisibility(View.VISIBLE);
        button_exit.setVisibility(View.VISIBLE);
        progressBar_insuline.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        player.setVisibility(View.INVISIBLE);
        domek.setVisibility(View.INVISIBLE);
        findViewById(R.id.text_exit).setVisibility(View.VISIBLE);
        findViewById(R.id.text_list).setVisibility(View.VISIBLE);
        findViewById(R.id.text_setting).setVisibility(View.VISIBLE);
    }


    /**
     * Ukrywanie przycisków menu i zmiana widzialności gracza, pasków głodu, insuliny.
     */
    private void hideMenu() {
        button_restart.setVisibility(View.INVISIBLE);
        button_newgame.setVisibility(View.INVISIBLE);
        button_list.setVisibility(View.INVISIBLE);
        button_setting.setVisibility(View.INVISIBLE);
        button_exit.setVisibility(View.INVISIBLE);
        progressBar_insuline.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        player.setVisibility(View.VISIBLE);
        domek.setVisibility(View.VISIBLE);
        findViewById(R.id.text_restart).setVisibility(View.INVISIBLE);
        findViewById(R.id.text_exit).setVisibility(View.INVISIBLE);
        findViewById(R.id.text_list).setVisibility(View.INVISIBLE);
        findViewById(R.id.text_newgame).setVisibility(View.INVISIBLE);
        findViewById(R.id.text_setting).setVisibility(View.INVISIBLE);
    }

    public void startTimer(){
        //check if it was already started
        if(timer == null) {
            timer = new Timer(context);
            timer_th = new Thread(timer);
            //start thread of timer then start timer itself
            timer_th.start();
            timer.start();
        }
    }

    public void resumeTimer(){
        if(timer != null) {
            timer_th = new Thread(timer);
            timer_th.start();
            timer.start();
        }
    }

    public void pauseTimer() {
        if(timer != null) {
            timer.stop();
            if(timer_th != null) {
                timer_th.interrupt();
            }
            timer_th = null;
        }
    }

    public void resetTimer() {
        if(timer != null) {
            //stop the timer
            timer.stop();
            //stop the thread
            if(timer_th != null)
                timer_th.interrupt();
            timer_th = null;
            timer = null;
        }
    }

    public void updateTimerText(final String time) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text_timer.setText(time);
            }
        });
    }

    public void doCustomAnimationToPlayer(final ImageView view, final int i){
        final ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.con_layout);
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, 1370);
        animation.setDuration(3000);
        //animation.setFillAfter(true);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                //nastapila kolizja z graczem
                if(view.getX() >= player.getX()- 60 && view.getX() <= player.getX() + 60 ) {
                    layout.removeView(view);
                    current_score += Integer.parseInt(product_kcal[i]);
                    score.setText(String.valueOf(current_score));
                    /**
                     * Jedenym ze sposobów na śmierć jest przejedzenie:
                     * w momencie gdy gracz zje coś i jego aktualna ilość spożytych kalorii
                     * przekroczy trzykrotnie jego zapotrzebowanie kaloryczne, gracz umrze
                     */
                    if(current_score >= kcal*3) {
                        gameOver("Śmierć z przejedzenia!");
                    }
                } else {
                    doCustomAnimationFromPlayer((ImageView) view, i);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //layout.removeView(view);
            }

            @Override
            public void onAnimationStart(Animation animation) {
                //layout.removeView(view);
            }
        });
        view.startAnimation(animation);
    }

    /**
     * Animacja, która zdarza się jeśli gracz nie złapał produktu na czas
     * @param view
     *          Produkt do spadania
     * @param i
     *          Indeks w tablicach produktów
     */
    public void doCustomAnimationFromPlayer(final ImageView view, int i){
        final ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.con_layout);
        TranslateAnimation animation = new TranslateAnimation(0, 0, 1370, getScreenHeight());
        animation.setDuration(2000);
        animation.setFillAfter(true);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                layout.removeView(view);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                layout.removeView(view);
            }

            @Override
            public void onAnimationStart(Animation animation) {
                layout.removeView(view);
            }
        });
        view.startAnimation(animation);
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /**
     * Funkcja wykonująca zadania, które następują po śmierci gracza takie jak:
     * - zatrzymanie gry
     * - wyświetlenie komunikatu o śmierci
     * - przejście do ScoreScreena
     * @param death_type
     *          Rodzaj śmierci, o której zostanie poinformowany gracz w komunikacie
     */
    public void gameOver(String death_type) {
        /**
         * Wszystkie przyciski są niewidoczne, jedyne co gracz widzi to komunikat o śmierci
         */
        button_pause.setVisibility(View.INVISIBLE);
        button_restart.setVisibility(View.INVISIBLE);
        button_newgame.setVisibility(View.INVISIBLE);
        button_list.setVisibility(View.INVISIBLE);
        button_setting.setVisibility(View.INVISIBLE);
        button_exit.setVisibility(View.INVISIBLE);
        progressBar_insuline.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        player.setVisibility(View.INVISIBLE);
        domek.setVisibility(View.INVISIBLE);
        findViewById(R.id.text_restart).setVisibility(View.INVISIBLE);
        findViewById(R.id.text_exit).setVisibility(View.INVISIBLE);
        findViewById(R.id.text_list).setVisibility(View.INVISIBLE);
        findViewById(R.id.text_newgame).setVisibility(View.INVISIBLE);
        findViewById(R.id.text_setting).setVisibility(View.INVISIBLE);
        pauseTimer();
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setMessage(death_type + " Następnym razem pamiętaj\n" +
                "żeby odżywiać się rozważnie!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //TO DO SCORE SCREEN!!
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
