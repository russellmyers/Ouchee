package com.genevol.ouchee;

import android.media.MediaPlayer;

/**
 * Created by RussellM on 15/06/2016.
 */
public class Song {

    float songTempo;

    MediaPlayer mp;



    public Song(MediaPlayer medPl,float initTempo) {
        mp = medPl;
        songTempo = initTempo;
    }

    float getCurrTempo() {
        return songTempo;
    }

    float getTempoInMilliSecsPerBeat() {
        float beatsPerSec = getCurrTempo() / 60;
        float beatsPerMilli = beatsPerSec / 1000;
        return beatsPerMilli == 0f ? 0f :  1 / beatsPerMilli;


    }

    double getCurrentBeatNum() {
        if (getTempoInMilliSecsPerBeat() == 0) {
            return 1;
        }

        int pos = mp.getCurrentPosition();
        return Math.floor(pos / getTempoInMilliSecsPerBeat()) + 1;

    }


}
