package in.shivamkrj.droneadmin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Beneficiaries extends AppCompatActivity {

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
    boolean isNotification = false;
    String node;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficiaries);
        arrayList=new ArrayList<>();
        findViews();
        setRecyclerview();
        database = FirebaseDatabase.getInstance();
        Intent i = getIntent();
        node = i.getStringExtra("node");
        title  = i.getStringExtra("title");
        if(node.equalsIgnoreCase("ADMIN-NOTIFICATION"))
            isNotification = true;
        if(isNotification)
            Toast.makeText(this,"id notification",Toast.LENGTH_LONG).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Beneficiaries.this);

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
                        for(int i=0;i<arrayList.size();i++){
                            String s = arrayList.get(i).data;
                            String title[] = s.split("@");
                            String a = title[0];
                            String b = title[1];
                            s = "<strong>"+" User: "+"</strong>"+a+"\n"+"<strong>"+" Message: "+"</strong>"+b;
                            arrayList.get(i).data = s;
                        }
                    }
                    adapter.notifyDataSetChanged();
                    pd.dismiss();
                }
                reference.removeEventListener(valueEventListener);
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
        if(isNotification)
            reference = database.getReference("USER-NOTIFICATION");
        final EditText input = new EditText(Beneficiaries.this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogView = input;
        if(isNotification)
            builder.setTitle("Add "+title+" for all users");
        else
            builder.setTitle("Add "+title);
        builder.setView(dialogView);
        builder.setCancelable(false);

        builder.setPositiveButton("submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(input==null||input.getText().length()==0){
                    Toast.makeText(Beneficiaries.this,"field are empty",Toast.LENGTH_LONG).show();
                    pd.dismiss();
                    addBeneficiaries();
                    return;
                }
                String key = reference.push().getKey();
                String data  = input.getText().toString();
                Constant.sendGroupPush(Beneficiaries.this," ",data);
                final itemData itemData = new itemData(key,data);
                reference.child(key).setValue(itemData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(isNotification)
                            return;
                        arrayList.add(itemData);
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
