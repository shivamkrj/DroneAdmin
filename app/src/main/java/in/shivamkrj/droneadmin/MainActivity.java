package in.shivamkrj.droneadmin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

//import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<UsersData> userItems;
    RecyclerView recyclerView;
    UsersAdapter adapter;
    DatabaseReference userReference;
    ProgressDialog pd;
    AlertDialog dialog;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        userReference = database.getReference("USERS");
        userItems = new ArrayList<>();
        fetchUsers();
        setRecyclerView();

    }

    private void fetchUsers() {
        pd = new ProgressDialog(this);
        pd.setMessage("Loading ...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        userItems.clear();
        userReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UsersData usersData = dataSnapshot.getValue(UsersData.class);
                if(usersData==null||usersData.username==null)
                    return;
                if(!usersData.username.equals("spclshivamkr@gmail.com"))
                    userItems.add(usersData);
                pd.dismiss();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UsersData usersData = dataSnapshot.getValue(UsersData.class);
                for(int i=0;i<userItems.size();i++){
                    if(userItems.get(i).username.equals(usersData.username)){
                        userItems.get(i).address = usersData.address;
                        userItems.get(i).latitude = usersData.latitude;
                        userItems.get(i).longitude = usersData.longitude;
                        userItems.get(i).time = usersData.time;
                    }
                }
                adapter.notifyDataSetChanged();
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

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        ViewClickInterface viewClickInterface = new ViewClickInterface() {
            @Override
            public void onViewClick(View view, int position) {
                //code for chat
                selectAction(position);

            }

            @Override
            public void onLongClick(View view, final int position) {
                //code for delete user

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Delete");
                builder.setCancelable(true);
                builder.setMessage("Are you sure to Delete ");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference ref  = database.getReference(userItems.get(position).username);
                        ref.removeValue();
                        Query deleteDB = userReference.orderByChild("username").equalTo(userItems.get(position).username);
                        deleteDB.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot d:dataSnapshot.getChildren()){
                                    d.getRef().removeValue();
                                }
                                userItems.remove(position);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        };
        adapter = new UsersAdapter(this,userItems,viewClickInterface);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void selectAction(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_singleclick,null);
        builder.setView(dialogView);
        builder.setCancelable(true);
        CardView englishCardView = dialogView.findViewById(R.id.mapCardView);
        CardView hindiCardView = dialogView.findViewById(R.id.chatCardView);
        englishCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s;
                s = userItems.get(position).latitude+","+userItems.get(position).longitude;
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+s);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                dialog.dismiss();
            }
        });
        hindiCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ChatActivity.class);
                intent.putExtra("USERNAME",userItems.get(position).username);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }
}
