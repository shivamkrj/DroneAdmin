package in.shivamkrj.droneadmin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListNgo extends AppCompatActivity {

    RecyclerView recyclerView;
    NgoAdapter adapter;
    ArrayList<NgoData> ngoDataArrayList;
    ViewClickInterface viewClickInterface;
    DatabaseReference ngoListReference;
    ProgressDialog pd;
    FirebaseDatabase database;
    FloatingActionButton addNgoButton;
    AlertDialog dialog;
    EditText ngoName,userName,phoneNumber,detail;
    ImageView callButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ngo);
        setTitle("List of NGOs near Jhansi");
        ngoDataArrayList=new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        ngoListReference = database.getReference("NGOLISTS");


        addNgoButton = findViewById(R.id.add_ngo);
        addNgoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNgo();
            }
        });
        fetchData();
        setRecyclerView();

    }

    private void registerNgo() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogView = getLayoutInflater().inflate(R.layout.register_ngo_view,null);
        builder.setView(dialogView);
        builder.setCancelable(false);


        Button cancelButton,submitButton;

        ngoName = dialogView.findViewById(R.id.ngo_name);
        userName = dialogView.findViewById(R.id.user_name);
        phoneNumber = dialogView.findViewById(R.id.phone_number);
        detail = dialogView.findViewById(R.id.detail_ngo);
        callButton  = dialogView.findViewById(R.id.call_ngo);
        cancelButton = dialogView.findViewById(R.id.cancel_action);
        submitButton = dialogView.findViewById(R.id.submit_action);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ngoNamef=ngoName.getText().toString();
                String userNamef=userName.getText().toString();
                String phoneNUmberf=phoneNumber.getText().toString();
                String  detailf=detail.getText().toString();
                if(ngoNamef==null||ngoNamef.length()==0||userNamef==null||userNamef.length()==0||
                phoneNUmberf==null||phoneNUmberf.length()<10||detailf==null||detailf.length()==0){
                    Toast.makeText(ListNgo.this,"All field are to be filled",Toast.LENGTH_LONG).show();
                    return;
                }
                String key= ngoListReference.push().getKey();
                NgoData ngoData = new NgoData(ngoNamef,userNamef,"+91"+phoneNUmberf.substring(phoneNUmberf.length()-10),detailf,key);
                ngoListReference.child(key).setValue(ngoData);
                Toast.makeText(ListNgo.this,"Ngo Added",Toast.LENGTH_LONG).show();
                ngoDataArrayList.add(0,ngoData);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    ValueEventListener valueEventListener;
    private void fetchData() {
        pd = new ProgressDialog(this);
        pd.setMessage("Loading ...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        ngoDataArrayList.clear();
        valueEventListener =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot > dataSnapshotIterable = dataSnapshot.getChildren();
                if(dataSnapshotIterable==null||dataSnapshot.getChildrenCount()==0){
                    pd.dismiss();
                    ngoListReference.removeEventListener(valueEventListener);
                }

                Log.i("fetch"," "+dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: dataSnapshotIterable) {
                    ngoDataArrayList.add(postSnapshot.getValue(NgoData.class));
                    Log.i("fetch",ngoDataArrayList.size()+"");
                    adapter.notifyDataSetChanged();
                    ngoListReference.removeEventListener(valueEventListener);
                    pd.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ngoListReference.addValueEventListener(valueEventListener);
        Log.i("fetch",ngoDataArrayList.size()+"");

    }

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        viewClickInterface = new ViewClickInterface() {
            @Override
            public void onViewClick(View view, int position) {
                String mobileNumber = ngoDataArrayList.get(position).phoneNUmber;
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL); // Action for what intent called for
                intent.setData(Uri.parse("tel: " + mobileNumber)); // Data with intent respective action on intent
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

                delete(position);

            }
        };
        adapter = new NgoAdapter(this,ngoDataArrayList,viewClickInterface);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private void delete(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListNgo.this);

        builder.setTitle("Delete");
        builder.setCancelable(true);
        builder.setMessage("Are you sure to Delete ");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ngoListReference.child(ngoDataArrayList.get(position).key).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        ngoDataArrayList.remove(position);
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


}
