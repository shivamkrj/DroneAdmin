package in.shivamkrj.droneadmin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    AlertDialog dialog;
    FirebaseDatabase database;
    DatabaseReference reference;
    ProgressDialog pd;
    ArrayList<itemData> arrayList;
    ItemDonateAdapter adapter;
    ViewClickInterface viewClickInterface;
    String title;
    private boolean isNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_item);
        setContentView(R.layout.activity_beneficiaries);
        arrayList=new ArrayList<>();
        findViews();
        setRecyclerview();
        database = FirebaseDatabase.getInstance();
        Intent i = getIntent();
        String node = i.getStringExtra("node");
        title  = i.getStringExtra("title");
        if(node.equalsIgnoreCase("ADMIN-NOTIFICATION"))
            isNotification = true;
        setTitle(title);
        reference = database.getReference(node);
        fetchData();
    }

    private void setRecyclerview() {
        viewClickInterface = new ViewClickInterface() {
            @Override
            public void onViewClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                delete(position);

            }

        };

        adapter = new ItemDonateAdapter(this,arrayList,viewClickInterface);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void delete(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ItemActivity.this);

        builder.setTitle("Delete");
        builder.setCancelable(true);
        builder.setMessage("Are you sure to Delete ");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                reference.child(arrayList.get(position).key).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        arrayList.remove(position);
                        adapter.notifyDataSetChanged();
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

    ValueEventListener valueEventListener;
    private void fetchData() {
        pd = new ProgressDialog(this);
        pd.setMessage("Loading ...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        arrayList.clear();
//        reference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                itemData item = dataSnapshot.getValue(itemData.class);
//                pd.cancel();
//                if(item.key=="skr"){
//                    pd.cancel();
//                    return;
//                }
//                if(isNotification){
//                    Log.d("zzzSize","zzIsNotification");
//
//                    for(int i=0;i<arrayList.size();i++){
//                        String ss = arrayList.get(i).data;
//                        String title[] = ss.split("@");
//                        String a = title[0];
//                        String b = "";
//                        if(title.length>1)
//                            b = title[1];
//                        ss = a+"\n"+"is"+b;
//                        item.data = ss;
////                        Log.d("zzzstring",s+" # "+a+" # "+b);
////                        Log.d("zzzcube",ss);
//                    }
//                }
//                arrayList.add(item);
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot > dataSnapshotIterable = dataSnapshot.getChildren();
                if(dataSnapshotIterable==null||dataSnapshot.getChildrenCount()==0){
                    pd.dismiss();

                }
                Log.i("fetch",dataSnapshot.getChildren().toString()+" "+dataSnapshot.getChildrenCount());
                if(0 == dataSnapshot.getChildrenCount())pd.dismiss();
                for (DataSnapshot postSnapshot: dataSnapshotIterable) {
                    arrayList.add(postSnapshot.getValue(itemData.class));
                    if(isNotification){
//                        Log.d("zzzSize",arrayList.size()+"zz");
//                        for(int i=0;i<arrayList.size();i++){
//                            String s = arrayList.get(i).data;
//                            String ss = s;
//                            String title[] = s.split("@");
//                            String a = title[0];
//                            String b = "";
//                            if(title.length>1)
//                                b = title[1];
//                            s = a+"\n"+"is"+b;
//                            arrayList.get(i).data = s;
//                            Log.d("zzzstring",s+" # "+a+" # "+b);
//                            Log.d("zzzcube",ss);
//                        }
                    }
                    adapter.notifyDataSetChanged();

                }
                reference.removeEventListener(valueEventListener);
                pd.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        reference.addValueEventListener(valueEventListener);

    }

    private void findViews() {

        recyclerView =findViewById(R.id.recycler_view);

        floatingActionButton = findViewById(R.id.add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBeneficiaries();
            }
        });
    }

    private void addBeneficiaries() {
        final EditText input = new EditText(ItemActivity.this);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogView = input;
        builder.setTitle("Add "+title);
        builder.setView(dialogView);
        builder.setCancelable(false);

        builder.setPositiveButton("submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(input==null||input.getText().length()==0){
                    Toast.makeText(ItemActivity.this,"field are empty",Toast.LENGTH_LONG).show();
                    pd.dismiss();
                    addBeneficiaries();
                    return;
                }
                String key = reference.push().getKey();
                String data  = input.getText().toString();
                final itemData itemData = new itemData(key,data);
                if(isNotification){
                    reference = database.getReference("USER-NOTIFICATION");
                    Constant.sendGroupPush(ItemActivity.this,"Dear Users",data);
                    Toast.makeText(ItemActivity.this,"Successfully sent Notification",Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
                reference.child(key).setValue(itemData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        arrayList.add(0,itemData);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }
}
