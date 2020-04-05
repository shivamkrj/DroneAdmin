package in.shivamkrj.droneadmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListNgo extends AppCompatActivity {

    RecyclerView recyclerView;
    NgoAdapter adapter;
    ArrayList<NgoData> ngoDataArrayList;
    ViewClickInterface viewClickInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ngo);

        ngoDataArrayList=new ArrayList<>();
        for(int i=0;i<15;i++)ngoDataArrayList.add(new NgoData(i+"",i+"","9012384858","cdcd"));
        setRecyclerView();

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
