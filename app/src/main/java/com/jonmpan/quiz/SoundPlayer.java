package com.jonmpan.quiz;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer {
    private static SoundPool soundPool;
    private static int start;
    private static int correct;
    private static int incorrect;
    private static int orin;
    // files in raw cannot have capitalization
    private static int spellcard;
    private static int easybgm;
    private static int lunaticbgm;

    public SoundPlayer(Context context) {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        start = soundPool.load(context, R.raw.start,1);
        correct = soundPool.load(context, R.raw.correct,1);
        incorrect = soundPool.load(context, R.raw.incorrect,1);
        orin = soundPool.load(context, R.raw.orin,1);
        spellcard = soundPool.load(context, R.raw.spellcard,1);
        easybgm = soundPool.load(context, R.raw.easybgm,1);
        lunaticbgm = soundPool.load(context, R.raw.lunaticbgm,1);
    }

    public void playStartSound() {
        // play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(start, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playCorrectSound() {
        soundPool.play(correct, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playIncorrectSound() {
        soundPool.play(incorrect, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playOrinSound() {
        soundPool.play(orin, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playSpellcardSound() {
        soundPool.play(spellcard, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playEasybgmSound() {
        soundPool.play(easybgm, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void stopEasybgmSound() {
        soundPool.stop(easybgm);
    }

    public void playLunaticbgmSound() {
        soundPool.play(lunaticbgm, 1.0f, 1.0f, 1, -1, 1.0f);
    }

    public void stopLunaticbgm() {
        soundPool.stop(lunaticbgm);
    }
}
