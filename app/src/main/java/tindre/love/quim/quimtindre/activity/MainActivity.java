package tindre.love.quim.quimtindre.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import tindre.love.quim.quimtindre.R;
import tindre.love.quim.quimtindre.adapter.FirebaseAdapter;
import tindre.love.quim.quimtindre.model.GreetingCard;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mGreetingCardDatabase;
    private FirebaseAdapter<GreetingCard> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        mGreetingCardDatabase = FirebaseDatabase.getInstance().getReference().child("greetingCards");

        adapter = new FirebaseAdapter<GreetingCard>(mGreetingCardDatabase, GreetingCard.class, R.layout.tinder, this) {
            @Override
            protected void populateView(View v, GreetingCard model) {
                TextView viewDescription = (TextView) v.findViewById(R.id.description);
                viewDescription.setText(model.getText());
            }
        };

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

}
