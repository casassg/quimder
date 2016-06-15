package tindre.love.quim.quimtindre;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by casassg on 15/06/16.
 *
 * @author casassg
 */
public class QuimderApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
