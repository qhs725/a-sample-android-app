package edu.utc.vat.flanker;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;


public class Chime {

    private final int numberSounds = 12;
    private int chime = edu.utc.vat.R.raw.chime;
    private int yay = edu.utc.vat.R.raw.yay;

    private SoundPool.Builder soundPoolBuilder;
    private SoundPool soundPool;
    private int audio;
    private int currentSound;
    Context context;

    public Chime (Context context) {
        this.context = context;
        //soundPoolBuilder = new SoundPool.Builder();
        //soundPoolBuilder.setMaxStreams(1);
        soundPool = new SoundPool(1,AudioManager.STREAM_MUSIC,0);//Builder.build();
        audio = soundPool.load(context, yay, 1);
    }

    void chime() {
        stopSound();
        playSound();
    }

    public void playSound() {
        AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        float volume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float volumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = volume/volumeMax;
        soundPool.play(audio, volume, volume, 1, 0, 1f);
    }

    public void stopSound() {
        soundPool.stop(audio);
    }

    public void resumeSound() {
        soundPool.resume(audio);
    }

    public int getCurrentSound() {
        return currentSound;
    }

    public final void cleanUp() {
        context = null;
        soundPool.release();
        soundPool = null;
    }
}
