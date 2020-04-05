package in.shivamkrj.droneadmin;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    String emailid,username;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userReference;
    DatabaseReference adminNotify;
    RecyclerView recyclerView;
    ArrayList<Message> messageArrayList;
    MessageAdapter adapter;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        emailid = getIntent().getStringExtra("USERNAME");
        findUserName();
        firebaseDatabase =FirebaseDatabase.getInstance();
//        userReference = firebaseDatabase.getReference(emailid);
//        adminNotify = firebaseDatabase.getReference("USERS").child(username).child("MESSAGE");
        adminNotify = firebaseDatabase.getReference(username);
        editText = findViewById(R.id.editText);
        recyclerView = findViewById(R.id.recycler_view);
        getMessages();
    }

    private void findUserName() {
        username = "";
        for (int i = 0; i < emailid.length(); i++) {
            char c = emailid.charAt(i);
            if (c == '.' || c == '@') {
                username += 'k';
            } else
                username += c;
        }
    }

    private void getMessages() {
        messageArrayList = new ArrayList<>();
        adapter = new MessageAdapter(this,messageArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);

        adminNotify.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                messageArrayList.add(message);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sendMessage(View view) {
        String text = editText.getText().toString();
        if(text.length()==0)
            return;
        editText.setText("");
        Message message = new Message();
        message.text = text;
        message.isAdmin = true;
        adminNotify.push().setValue(message);
    }
}
