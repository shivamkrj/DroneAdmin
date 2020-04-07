package in.shivamkrj.droneadmin;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


//import com.google.android.gms.common.api.Response;
//import com.google.firebase.database.core.Constants;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Launcher extends AppCompatActivity {

    TextView sewaTv,sewaTV1,needTv,donateTv,beneficaryTv,ngoTv,itemTv, about;
    String token;

    AlertDialog dialog;
    String FCM_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        findViews();
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("fcms", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("zzzfcms", msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic("admin")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d("zzzubscribeToTopic", msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void findViews() {
        about = findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Launcher.this,About.class));
            }
        });
        sewaTv = findViewById(R.id.tv_sewa);
        sewaTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //donate/need
//                selectActionForDonate();
                Toast.makeText(Launcher.this,"Not for Admin App",Toast.LENGTH_LONG).show();
            }
        });
        sewaTV1 = findViewById(R.id.tv_sewa1);
        sewaTV1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        needTv = findViewById(R.id.needy_tv);
        needTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Launcher.this,MainActivity.class);
                intent.putExtra("isNeed",true);
                startActivity(intent);
            }
        });
        donateTv = findViewById(R.id.tv_doner);
        donateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Launcher.this,MainActivity.class);
                intent.putExtra("isNeed",false);
                startActivity(intent);
            }
        });
        beneficaryTv = findViewById(R.id.beneficiary);
        beneficaryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Launcher.this,Beneficiaries.class);
                i.putExtra("title","Beneficiaries");
                i.putExtra("node","Beneficiaries");
                startActivity(i);
//                finish();
            }
        });
        ngoTv = findViewById(R.id.tv_ngo);
        ngoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Launcher.this,ListNgo.class));

            }
        });
        itemTv=findViewById(R.id.tv_item_donate);
        itemTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Launcher.this,ItemActivity.class);
                i.putExtra("title","List of Item Donate");
                i.putExtra("node","DonateItem");
                startActivity(i);
            }
        });
    }

    private void selectActionForDonate() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_three_options,null);
        builder.setView(dialogView);
        builder.setCancelable(true);
        CardView need = dialogView.findViewById(R.id.mapCardView);
        CardView donateR = dialogView.findViewById(R.id.chatCardView);
        CardView donateO = dialogView.findViewById(R.id.cashCardView);
        need.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                dialog.dismiss();
                Intent intent = new Intent(Launcher.this,MainActivity.class);
                intent.putExtra("isNeed",true);
                startActivity(intent);
            }
        });
        donateR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                dialog.dismiss();
                Intent intent = new Intent(Launcher.this,MainActivity.class);
                intent.putExtra("isNeed",false);
                startActivity(intent);
            }
        });
        donateO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                dialog.dismiss();
                //webview link for donation using uri
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.launcher_menu, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.notification:
                notification();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void notification(){
        Intent i =new Intent(Launcher.this,Beneficiaries.class);
        i.putExtra("title","Notifications");
        i.putExtra("node","ADMIN-NOTIFICATION");
        startActivity(i);

    }
}
