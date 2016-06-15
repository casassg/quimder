package tindre.love.quim.quimtindre.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import tindre.love.quim.quimtindre.R;
import tindre.love.quim.quimtindre.model.ChatMessage;
import tindre.love.quim.quimtindre.utils.AnimalUtils;
import tindre.love.quim.quimtindre.utils.FirebaseAdapter;

public class ChatActivity extends AppCompatActivity {

    private final static String CHAT_MESSAGES = "chatMessages";
    private FirebaseAdapter<ChatMessage> adapter;
    private String animal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        animal = AnimalUtils.getRandomAnimal();

        final RecyclerView recycler = (RecyclerView) findViewById(R.id.chat_list);
        assert recycler != null;
        recycler.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        recycler.setLayoutManager(layout);

        DatabaseReference mChatMessageDatabase = FirebaseDatabase.getInstance().getReference().child(CHAT_MESSAGES);
        adapter = new FirebaseAdapter<ChatMessage>(mChatMessageDatabase, ChatMessage.class, R.layout.message, this) {
            @Override
            protected void onKeyAdded(String key) {
                if (adapter.getCount() > 0) {
                    recycler.smoothScrollToPosition(adapter.getCount() - 1);
                }
            }

            @Override
            protected void populateView(View v, ChatMessage model) {
                TextView author = (TextView) v.findViewById(R.id.author);
                TextView content = (TextView) v.findViewById(R.id.content);
                TextView date = (TextView) v.findViewById(R.id.date);
                ImageView icon = (ImageView) v.findViewById(R.id.user_icon);
                author.setText(model.getAuthor());
                content.setText(model.getContent());
                DateFormat sdf = SimpleDateFormat.getDateTimeInstance();
                date.setText(sdf.format(model.getCreatedAt()));
                icon.setImageDrawable(getDrawable(AnimalUtils.getAnimalImageId(animal)));
            }

        };
        recycler.setAdapter(adapter);
        setupSendAction(mChatMessageDatabase);
    }

    private void setupSendAction(final DatabaseReference mRef) {
        Button sendButton = (Button) findViewById(R.id.send);
        final EditText messageText = (EditText) findViewById(R.id.new_message);
        assert sendButton != null;
        assert messageText != null;
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageContent = messageText.getText().toString();
                if (messageContent.isEmpty()) {
                    Snackbar.make(v.findViewById(R.id.chat_layout), "Los mensajes vacios no són permitidos", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                ChatMessage message = new ChatMessage(animal, messageContent);
                mRef.push().setValue(message);
                messageText.setText("");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
    }
}
