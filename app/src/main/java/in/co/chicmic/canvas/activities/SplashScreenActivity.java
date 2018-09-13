package in.co.chicmic.canvas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import in.co.chicmic.canvas.R;
import in.co.chicmic.canvas.utilities.Constants;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_splash_screen, null);
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(view);
        startTimer();
    }

    void startTimer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this
                        , CanvasActivity.class);
                startActivity(i);
                finish();
            }
        }, Constants.sSPLASH_TIME);
    }
}
