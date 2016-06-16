package tindre.love.quim.quimtindre.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tindre.love.quim.quimtindre.R;
import tindre.love.quim.quimtindre.model.ChatMessage;
import tindre.love.quim.quimtindre.utils.AnimalUtils;

public class ChatActivity extends AppCompatActivity {

    private final static String CHAT_MESSAGES = "chatMessages";
    private String animal;
    private FirebaseRecyclerAdapter<ChatMessage, ChatHolder> mAdapter;
    private RecyclerView recycler;
    private View chatLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        animal = AnimalUtils.getRandomAnimal();
        chatLayout = findViewById(R.id.chat_layout);
        recycler = (RecyclerView) findViewById(R.id.chat_list);
        assert chatLayout != null;
        assert recycler != null;
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference mChatMessageDatabase = FirebaseDatabase.getInstance().getReference().child(CHAT_MESSAGES);

        mAdapter = new FirebaseRecyclerAdapter<ChatMessage, ChatHolder>(ChatMessage.class, R.layout.message, ChatHolder.class, mChatMessageDatabase) {
            @Override
            public void populateViewHolder(ChatHolder chatMessageViewHolder, ChatMessage chatMessage, int position) {
                chatMessageViewHolder.setAuthor(chatMessage.getAuthor());
                chatMessageViewHolder.setContent(chatMessage.getContent());
            }
        };

        mChatMessageDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mAdapter.getItemCount() > 0) {
                    recycler.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
        recycler.setAdapter(mAdapter);

        notifyAnimal();

        setupSendAction(mChatMessageDatabase);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    private void notifyAnimal() {
        Snackbar.make(chatLayout, "In this chat session you are a " + animal, Snackbar.LENGTH_LONG)
                .show();
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
                if (messageContent.trim().isEmpty()) {
                    Snackbar.make(chatLayout, "Los mensajes vacios no són permitidos", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (messageContent.trim().contains("/animal ")) {
                    String[] parts = messageContent.trim().split(" ");
                    String newAnimal = parts[1];
                    if (AnimalUtils.contains(newAnimal)) {
                        animal = newAnimal;
                        notifyAnimal();
                    }
                    else if (newAnimal.equals("BOSS")) {
                        animal = newAnimal;
                        Snackbar.make(chatLayout, "YOU ARE THE FUCKING BOSS!", Snackbar.LENGTH_SHORT)
                                .show();
                    }
                    else {
                        Snackbar.make(chatLayout, "Wrong animal", Snackbar.LENGTH_SHORT)
                                .show();
                    }
                    messageText.setText("");
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
        if (id == R.id.down) {
            recycler.smoothScrollToPosition(mAdapter.getItemCount() - 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

    public static class ChatHolder extends RecyclerView.ViewHolder {
        View mView;

        public ChatHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setAuthor(String author) {
            TextView field = (TextView) itemView.findViewById(R.id.author);
            field.setText("by " + author);
            ImageView icon = (ImageView) itemView.findViewById(R.id.user_icon);
            icon.setImageResource(AnimalUtils.getAnimalImageId(author));
        }

        public void setContent(String content) {
            TextView field = (TextView) itemView.findViewById(R.id.content);
            field.setText(content);
        }

    }
}
