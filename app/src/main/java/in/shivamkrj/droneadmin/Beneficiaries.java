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
import android.view.ViewGroup;
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
    ArrayList<BeneficiariesData> arrayList;
    BeneficiariesAdapter adapter;
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

        adapter = new BeneficiariesAdapter(this,arrayList,viewClickInterface);
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
                    arrayList.add(postSnapshot.getValue(BeneficiariesData.class));

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

        final View dialogView = getLayoutInflater().inflate(R.layout.beneficiaries_reg_view,null);
        final EditText input =dialogView.findViewById(R.id.name);
        final EditText input1 = dialogView.findViewById(R.id.root_name);
        final EditText input2 = dialogView.findViewById(R.id.address);


//        LinearLayout layout = new LinearLayout(this);
//        layout.setOrientation(LinearLayout.VERTICAL);
//        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 900));
//
//
//        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1);
//        LinearLayout.LayoutParams lparams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,2);
//        input.setLayoutParams(lparams);
//        input1.setLayoutParams(lparams);
//        input2.setLayoutParams(lparams1);
//        input.setHint("Enter Name");
//        input1.setHint("Father's/Husband Name");
//        input2.setHint("Address");
//        layout.addView(input);
//        layout.addView(input1);
//        layout.addView(input2);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(isNotification)
            builder.setTitle("Add "+title+" for all users");
        else
            builder.setTitle("Add "+title);
        builder.setView(dialogView);
        builder.setCancelable(false);

        builder.setPositiveButton("submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(input==null||input.getText().length()==0||input1==null||input1.getText().length()==0||input2==null||input2.getText().length()==0){

                    Toast.makeText(Beneficiaries.this,"field are empty",Toast.LENGTH_LONG).show();
                    pd.dismiss();
                    addBeneficiaries();
                    return;
                }
                String key = reference.push().getKey();
                String name=input.getText().toString();
                String rootName= input1.getText().toString();
                String address = input2.getText().toString();
                final BeneficiariesData data = new BeneficiariesData(name,rootName,address,key);
                reference.child(key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(isNotification)
                            return;
                        arrayList.add(0,data);
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
