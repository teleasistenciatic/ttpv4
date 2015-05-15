package com.local.android.teleasistenciaticplus.lib.sound;

/**
 * Created by MORUGE on 15/05/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.widget.Toast;

import java.util.Locale;

public class SintetizadorVoz {
    private TextToSpeech myTTS;
    private boolean readyToSpeak = false;
    private Context context;

    public SintetizadorVoz(Context baseContext) {
        this.context = baseContext;
        initOrInstallTTS();
    }

    public void initOrInstallTTS() {
        myTTS = new TextToSpeech(context, new OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    myTTS.setLanguage(Locale.getDefault());
                    readyToSpeak = true;
                } else
                    installTTS();
            }
        });
    }

    private void installTTS() {
        Intent installIntent = new Intent();
        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        context.startActivity(installIntent);
    }

    public void hablaPorEsaBoquita(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        if (readyToSpeak)
            myTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

}