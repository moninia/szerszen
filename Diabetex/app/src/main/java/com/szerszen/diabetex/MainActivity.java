package com.szerszen.diabetex;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public Button button_exit;
    public Button button_restart;
    public Button button_pause;
    public Button button_newgame;
    public Button button_list;
    public Button button_setting;

    public Button button_human;

    private Context context;
    private TextView text_timer;
    private Thread timer_th;
    private Timer timer;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        context = this;

        button_exit = (Button) findViewById(R.id.button_exit);
        button_restart = (Button) findViewById(R.id.button_restart);
        button_pause = (Button) findViewById(R.id.button_pause);
        button_list = (Button) findViewById(R.id.button_list);
        button_setting = (Button) findViewById(R.id.button_setting);
        button_newgame = (Button) findViewById(R.id.button_newgame);

        button_human = (Button) findViewById(R.id.button);

        text_timer = (TextView) findViewById(R.id.text_timer);

        button_human.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                return true;
            }
        });

        button_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this is how you create new page
                Intent list_page = new Intent(MainActivity.this, ListActivity.class);
                startActivity(list_page);
            }
        });

        button_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumeTimer();
            }
        });

        button_newgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
                startTimer();
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
            @Override
            public void onClick(View v) {
                button_pause.setVisibility(View.INVISIBLE);
                showMenu();
                pauseTimer();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showMenu() {
        button_restart.setVisibility(View.VISIBLE);
        button_newgame.setVisibility(View.VISIBLE);
        button_list.setVisibility(View.VISIBLE);
        button_setting.setVisibility(View.VISIBLE);
        button_exit.setVisibility(View.VISIBLE);
        findViewById(R.id.text_restart).setVisibility(View.VISIBLE);
        findViewById(R.id.text_exit).setVisibility(View.VISIBLE);
        findViewById(R.id.text_list).setVisibility(View.VISIBLE);
        findViewById(R.id.text_newgame).setVisibility(View.VISIBLE);
        findViewById(R.id.text_setting).setVisibility(View.VISIBLE);
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

            timer.start();
        }
    }

    public void pauseTimer() {
        if(timer != null) {
            timer.stop();
        }
    }

    public void resetTimer() {
        if(timer != null) {
            //stop the timer
            timer.stop();
            //stop the thread
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
}

/*package com.szerszen.wat;


public class MainActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;
    Button button;
    public int change;

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


        //to działa na id/costam w xml
        setContentView(R.layout.content_main);

        //casting ref to things in
        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);


        button = (Button) findViewById(R.id.button);

        //ref do mojego layouta
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) button.getLayoutParams();

        // Get existing constraints into a ConstraintSet
        final ImageView object = new ImageView(this);

        Drawable d = getResources().getDrawable(android.R.drawable.star_big_off);
        object.setImageDrawable(d);
        object.setVisibility(View.VISIBLE);
        object.setId(View.generateViewId());
        ConstraintSet constraints = new ConstraintSet();
        constraints.clone(layout);

        layout.addView(object);
        // Now constrain the ImageView so it is centered on the screen.
        // There is also a "center" method that can be used here.
        constraints.constrainWidth(object.getId(), ConstraintSet.WRAP_CONTENT);
        constraints.constrainHeight(object.getId(), ConstraintSet.WRAP_CONTENT);
        constraints.center(object.getId(), ConstraintSet.PARENT_ID, ConstraintSet.LEFT,
                0, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0, 0.5f);
        constraints.center(object.getId(), ConstraintSet.PARENT_ID, ConstraintSet.TOP,
                0, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0, 0.5f);
        constraints.applyTo(layout);

        object.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (change == 1) {
                    button.setBackgroundColor(Color.parseColor("#55FF0000"));
                    change = 0;
                } else {
                    button.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    change = 1;
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                textView.setText(text);
                textView.setVisibility(View.VISIBLE);

                doCustomAnimation(object);

                *//* TranslateAnimation animation = new TranslateAnimation(0.0f, 400.0f,
                        0.0f, 0.0f);
                animation.setDuration(5000);
                animation.setRepeatCount(5);
                animation.setRepeatMode(2);
                animation.setFillAfter(true);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        button.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                });
                object.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button.setBackgroundColor(Color.parseColor("#55FF0000"));
                    }
                });
                object.startAnimation(animation); *//*
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onClick() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
            return true;

        return super.onOptionsItemSelected(item);
    }

    public void doCustomAnimation(ImageView imageView){
        final ImageView image = imageView;
        final Float startingPoint= image.getX();
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                image.setX(startingPoint - (int)(startingPoint/2 * interpolatedTime));
            }

        };
        animation.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationEnd(Animation animation) {
                //simpleLock= false;
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }
        });
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(1);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setDuration(5000);
        image.startAnimation(animation);
    }

}*/

