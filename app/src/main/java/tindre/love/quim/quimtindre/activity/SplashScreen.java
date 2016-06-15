package tindre.love.quim.quimtindre.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import tindre.love.quim.quimtindre.R;

public class SplashScreen extends AppCompatActivity {



    /** Called when the activity is first created. */
    Thread splashTread;
    public static boolean shouldWeStopNow = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        StartAnimations();
    }
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        assert l != null;
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        assert iv != null;
        iv.clearAnimation();
        iv.startAnimation(anim);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time (aprox until data is available)
                    while (waited < 3000) {
                        sleep(100);
                        waited += 100;
                    }
                    while (!shouldWeStopNow & waited < 10000) {
                        sleep(100);
                        waited += 100;
                    }


                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    SplashScreen.this.finish();
                }


            }
        };


        splashTread.start();

    }
}
