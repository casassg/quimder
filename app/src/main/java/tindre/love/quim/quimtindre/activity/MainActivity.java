package tindre.love.quim.quimtindre.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.io.IOException;

import tindre.love.quim.quimtindre.R;
import tindre.love.quim.quimtindre.model.GreetingCard;
import tindre.love.quim.quimtindre.utils.FirebaseAdapter;
import tindre.love.quim.quimtindre.utils.ImagesUtils;

public class MainActivity extends AppCompatActivity {

    public final static String GREETING_CARDS = "greetingCards";
    private FirebaseAdapter<GreetingCard> adapter;
    private boolean isSplashShown = true;
    private SwipeFlingAdapterView flingContainer;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (database==null){
            database = FirebaseDatabase.getInstance();
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.open_chat){
            Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void init() {
        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        assert flingContainer != null;
        showSplashScreen();

        DatabaseReference mGreetingCardDatabase = database.getReference().child(GREETING_CARDS);
        adapter = new FirebaseAdapter<GreetingCard>(mGreetingCardDatabase, GreetingCard.class, R.layout.tinder, this) {
            @Override
            protected void onKeyAdded(String key) {
                ImagesUtils.getImage(new ImagesUtils.SetDrawableCallback() {
                    @Override
                    public void giveMeMyDrawable(Bitmap bitmap) {
                    }
                }, getApplicationContext(), key);
            }

            @Override
            protected void populateView(View v, GreetingCard model) {
                TextView nameTextView = (TextView) v.findViewById(R.id.user_name);
                TextView ageTextView = (TextView) v.findViewById(R.id.user_age);
                TextView descriptionTextView = (TextView) v.findViewById(R.id.user_description);
                final ImageView imageView = (ImageView) v.findViewById(R.id.user_photo);
                nameTextView.setText(model.getAuthor());
                ageTextView.setText(Integer.toString(model.getAge()));
                descriptionTextView.setText(model.getDescription());
                try {
                    ImagesUtils.getImage(model, new ImagesUtils.SetDrawableCallback() {
                        @Override
                        public void giveMeMyDrawable(Bitmap bitmap) {
                            imageView.setImageBitmap(bitmap);
                            adapter.notifyDataSetChanged();
                            if (isSplashShown) {
                                hideSplashScreen();
                            }

                        }
                    }, getApplicationContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        flingContainer.setAdapter(adapter);
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int i, Object o) {
                GreetingCard card = (GreetingCard) o;

                Intent intent = new Intent(getApplicationContext(), GreetingActivity.class);
                intent.putExtra(GreetingActivity.USER_ID,card.getAuthor());
                startActivity(intent);
            }
        });
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

    private void showSplashScreen() {
        flingContainer.setVisibility(View.INVISIBLE);
        isSplashShown = true;

        Intent intent = new Intent(MainActivity.this, SplashScreen.class);
        startActivity(intent);

    }

    private void hideSplashScreen() {
        flingContainer.setVisibility(View.VISIBLE);
        isSplashShown = false;
        SplashScreen.shouldWeStopNow = true;
    }

}
