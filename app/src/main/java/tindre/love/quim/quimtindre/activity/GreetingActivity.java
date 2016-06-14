package tindre.love.quim.quimtindre.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tindre.love.quim.quimtindre.R;
import tindre.love.quim.quimtindre.model.GreetingCard;
import tindre.love.quim.quimtindre.utils.ImagesUtils;

public class GreetingActivity extends AppCompatActivity {

    public static final String USER_ID = "user_id";
    public static final String TAG = GreetingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
// TODO: STILL NEED TO FIGURE THIS OUT
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (!intent.hasExtra(USER_ID)) {
            Log.e(TAG, USER_ID + " extra is required to start this activity. FINISH IT!");
            finish();
            // K.O.
        }
        String userId = intent.getStringExtra(USER_ID);
        DatabaseReference mGreetingCardDatabase = FirebaseDatabase.getInstance().getReference()
                .child(MainActivity.GREETING_CARDS)
                .child(userId);
        ImagesUtils.getImage(new ImagesUtils.SetDrawableCallback() {
            @Override
            public void giveMeMyDrawable(Bitmap bitmap) {
                ImageView appBarLayout = (ImageView) findViewById(R.id.user_image_detail);
                appBarLayout.setImageBitmap(bitmap);
            }
        },getApplicationContext(),userId);
        mGreetingCardDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GreetingCard card = dataSnapshot.getValue(GreetingCard.class);
                setFields(card);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "OH NO THE DATABASE CRASHED!", databaseError.toException());
            }
        });
    }

    private void setFields(GreetingCard card) {
        TextView authorView = (TextView) findViewById(R.id.user_name);
        TextView ageView = (TextView) findViewById(R.id.user_age);
        TextView descriptionView = (TextView) findViewById(R.id.user_description);
        assert authorView != null;
        assert ageView != null;
        assert descriptionView != null;

        authorView.setText(card.getAuthor());
        ageView.setText(String.valueOf(card.getAge()));
        descriptionView.setText(card.getDescription());


        //And now do the tricky part

    }
}
