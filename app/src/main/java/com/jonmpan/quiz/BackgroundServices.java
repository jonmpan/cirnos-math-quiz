package com.jonmpan.quiz;

import android.app.Application;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

public class BackgroundServices extends Application implements LifecycleObserver {
    MediaPlayer mp;

    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void created() {
        mp = MainActivity.getMp();
        Log.d("SampleLifeCycle", "ON_CREATE");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void started() {
        Log.d("SampleLifeCycle", "ON_START");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void resumed() {
        mp.start();
        Log.d("SampleLifeCycle", "ON_RESUME");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void paused() {
        try {
            mp.pause();
        } catch (Exception e){
            e.printStackTrace();
        }
        Log.d("SampleLifeCycle", "ON_PAUSE");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void stopped() {
        Log.d("SampleLifeCycle", "ON_STOP");
    }
}