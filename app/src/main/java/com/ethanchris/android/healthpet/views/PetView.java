package com.ethanchris.android.healthpet.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.ethanchris.android.healthpet.R;
import com.ethanchris.android.healthpet.models.PetColor;
import com.ethanchris.android.healthpet.models.PetHat;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

public class PetView extends View {
    private Movie mMovie;
    private long movieStart;
    private boolean mListening = false;
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;

    public PetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics mMetrics = new DisplayMetrics();
        if (attrs != null) {
            TypedArray mAttrs = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PetView, 0, 0);
            if (R.styleable.PetView_state != 0) {
                InputStream stream = context.getResources().openRawResource(+ mAttrs.getResourceId(R.styleable.PetView_state, R.drawable.purple_no_hat));
                if (stream != null) {
                    mMovie = Movie.decodeStream(stream);
                }
            }
        } else {
            InputStream stream = context.getResources().openRawResource(+ R.drawable.purple_no_hat);
            mMovie = Movie.decodeStream(stream);
        }
        final WindowManager w = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (w != null) {
            final Display d = w.getDefaultDisplay();
            if (d != null) {
                d.getMetrics(mMetrics);
            }
        }

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        mSpeechRecognizer.setRecognitionListener(mSpeechRecognitionListener);
    }

    private RecognitionListener mSpeechRecognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            Log.d("HealthPet", "ready to listen for speech");
            mListening = true;
        }

        @Override
        public void onBeginningOfSpeech() {
            Log.d("HealthPet", "speech has started");
        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {

        }

        @Override
        public void onEndOfSpeech() {
            mListening = false;
        }

        @Override
        public void onError(int i) {
            Log.e("HealthPet", "speech recognizer error " + i);
        }

        @Override
        public void onResults(Bundle bundle) {
            String result = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
            Log.d("HealthPet", "got speech result: " + result);
            String petReply = PetViewSpeechResponse.getReply(new PetViewSpeechResponse(result).parse());
            Toast.makeText(getContext(), petReply, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    };

    @Override
    public void onDraw(Canvas canvas) {
        if (canvas != null) {
            super.onDraw(canvas);
            long now = android.os.SystemClock.uptimeMillis();
            if (movieStart == 0) { movieStart = now; }

            int gifTime;
            if (mMovie == null) {
                gifTime = 0;
            } else {
                gifTime = (int)((now - movieStart) % mMovie.duration());
            }
            if (mMovie != null) {
                mMovie.setTime(gifTime);
                mMovie.draw(canvas, 0, 0);
                if (mListening) {
                    Paint paint = new Paint();
                    paint.setColor(Color.WHITE);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawCircle(500, 500, 10.0f, paint);
                }
                this.invalidate();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d("HealthPet", "pet touched");
            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth, desiredHeight;
        if (mMovie != null) {
            desiredWidth = mMovie.width();
            desiredHeight = mMovie.height();
        } else {
            desiredWidth = 1024;
            desiredHeight = 1024;
        }

        Log.d("HealthPet", "width: " + desiredWidth + ", height: " + desiredHeight);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width, height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(widthSize, desiredWidth);
        } else {
            width = desiredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            height = Math.min(heightSize, desiredHeight);
        } else {
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);
    }

    private int getPetAppearanceId(PetColor color, PetHat hat) {
        return getResources().getIdentifier(color.name().toLowerCase() + "_" + hat.name().toLowerCase(), "drawable", "com.ethanchris.android.healthpet");
    }

    public void setName(String name) {

    }

    public void setGif(Context context, PetColor color, PetHat hat) {
        InputStream stream = context.getResources().openRawResource(+ getPetAppearanceId(color, hat));
        mMovie = Movie.decodeStream(stream);
        movieStart = 0;
        this.invalidate();
    }
}
