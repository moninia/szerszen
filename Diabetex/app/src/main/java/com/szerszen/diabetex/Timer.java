package com.szerszen.diabetex;

import android.content.Context;

public class Timer implements Runnable{

    private Context context;
    private boolean is_running;
    private long start_time;
    private long pause_time;

    public Timer(Context context) {
        this.context = context;
    }

    public Timer(Context context, long start_time) {
        this.context = context;
        this.start_time = start_time;
    }

    public long getStartTime() {
        return start_time;
    }

    public void start() {
        if(start_time == 0) {
            start_time = System.currentTimeMillis();
            pause_time = 0;
        } else {
            long resume_time = System.currentTimeMillis();
            pause_time = resume_time - pause_time;
        }
        is_running = true;
    }

    public void stop() {
        is_running = false;
        pause_time = System.currentTimeMillis();
    }

    @Override
    public void run() {
        while(is_running) {
            long since = System.currentTimeMillis() - start_time - pause_time;
            int seconds = (int) ((since/1000) % 60);
            int minutes = (int) (((since / 60000 )) % 60);

            ((MainActivity)context).updateTimerText(String.format(
                    "%02d:%02d", minutes, seconds
            ));
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}
