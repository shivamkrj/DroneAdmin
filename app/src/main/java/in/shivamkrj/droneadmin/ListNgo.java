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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ngo);

        ngoDataArrayList=new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        ngoListReference = database.getReference("NGOLISTS");

        for(int i=0;i<15;i++){
            ngoDataArrayList.add(new NgoData(i+"",i+"","9012384858","cdcd"));
//            ngoListReference.push().setValue(ngoDataArrayList.get(i));

        }
        fetchData();
        setRecyclerView();

    }

    private void fetchData() {
        pd = new ProgressDialog(this);
        pd.setMessage("Loading ...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        ngoDataArrayList.clear();
        ngoListReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot > dataSnapshotIterable = dataSnapshot.getChildren();
                if(dataSnapshotIterable==null)return;
                Log.i("fetch",dataSnapshot.getChildren().toString()+" "+dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: dataSnapshotIterable) {
//                    Log.i("data111",postSnapshot.getValue()+"");
                    ngoDataArrayList.add(postSnapshot.getValue(NgoData.class));

                    Log.i("fetch",ngoDataArrayList.size()+"");
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.i("fetch",ngoDataArrayList.size()+"");
        pd.dismiss();
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

            }
        };
        adapter = new NgoAdapter(this,ngoDataArrayList,viewClickInterface);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


}
