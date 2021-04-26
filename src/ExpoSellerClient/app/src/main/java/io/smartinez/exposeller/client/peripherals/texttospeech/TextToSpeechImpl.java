package io.smartinez.exposeller.client.peripherals.texttospeech;

import android.app.Application;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ViewModelScoped;
import io.smartinez.exposeller.client.ExpoSellerApplication;
import io.smartinez.exposeller.client.util.Utilities;

@ViewModelScoped
public class TextToSpeechImpl implements ITextToSpeech, TextToSpeech.OnInitListener {
    private TextToSpeech mTextToSpeech;
    private boolean mIsInitialized = false;

    @Inject
    public TextToSpeechImpl(){
        try {
            mTextToSpeech = new TextToSpeech(Utilities.getApplicationUsingReflection().getApplicationContext(), this);
        } catch (Exception e) {
            mTextToSpeech = null;
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            mTextToSpeech.setLanguage(Locale.getDefault());
            mIsInitialized = true;
        }
    }

    @Override
    public void speak(String text) {
        if (!mIsInitialized) {
            Log.d(ExpoSellerApplication.LOG_TAG, "The text to speech is initializing...");
            return;
        }

        mTextToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
    }
}
