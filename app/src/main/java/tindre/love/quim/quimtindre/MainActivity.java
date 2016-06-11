package tindre.love.quim.quimtindre;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;
import com.google.firebase.storage.FirebaseStorage;

public class MainActivity extends AppCompatActivity {

    private CardContainer mCardContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCardContainer = (CardContainer) findViewById(R.id.layoutview);

        FirebaseStorage storage = FirebaseStorage.getInstance();

        SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(this);
        CardModel card1 = new CardModel("Title1", "Description goes here", getResources().getDrawable(R.drawable.picture1));
        card1.setOnCardDimissedListener(new CardModel.OnCardDimissedListener() {
            @Override
            public void onLike() {
                Toast.makeText(MainActivity.this, "LIKEEE", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDislike() {
                Toast.makeText(MainActivity.this, "NOOOOO, SO UGLYY", Toast.LENGTH_SHORT).show();
            }
        });
        adapter.add(card1);
        adapter.add(card1);
        adapter.add(card1);
        adapter.add(card1);
        adapter.add(card1);
        adapter.add(card1);
        adapter.add(card1);
        mCardContainer.setAdapter(adapter);
    }
}
