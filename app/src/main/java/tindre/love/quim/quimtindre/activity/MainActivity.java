package tindre.love.quim.quimtindre.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import java.io.IOException;
import tindre.love.quim.quimtindre.utils.FirebaseAdapter;
import tindre.love.quim.quimtindre.utils.ImagesUtils;
import tindre.love.quim.quimtindre.R;
import tindre.love.quim.quimtindre.model.GreetingCard;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL = 32;
    private final static String GREETING_CARDS = "greetingCards";
    private DatabaseReference mGreetingCardDatabase;
    private FirebaseAdapter<GreetingCard> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (checkWritePermission(this)){
            init();
        }
    }

    private void init() {

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        mGreetingCardDatabase = FirebaseDatabase.getInstance().getReference().child(GREETING_CARDS);
        adapter = new FirebaseAdapter<GreetingCard>(mGreetingCardDatabase, GreetingCard.class, R.layout.tinder, this) {
            @Override
            protected void populateView(View v, GreetingCard model) {
                final TextView viewDescription = (TextView) v.findViewById(R.id.user_name);
                final ImageView imageView = (ImageView) v.findViewById(R.id.user_photo);
                viewDescription.setText(model.getAuthor());
                try {
                    ImagesUtils.getImage(model, new ImagesUtils.SetDrawableCallback() {
                        @Override
                        public void giveMeMyDrawable(Bitmap bitmap) {
                            imageView.setImageBitmap(bitmap);
                            adapter.notifyDataSetChanged();

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        assert flingContainer != null;
        flingContainer.setAdapter(adapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                adapter.removeFirst();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {

            }

            @Override
            public void onRightCardExit(Object dataObject) {

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onScroll(float v) {

            }

        });
    }

    public static Boolean checkWritePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_WRITE_EXTERNAL
            );
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_WRITE_EXTERNAL &&
                grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            init();
        }
    }

}
