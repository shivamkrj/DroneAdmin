package in.shivamkrj.droneadmin;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Launcher extends AppCompatActivity {

    TextView sewaTv,sewaTV1,needTv,donateTv,beneficaryTv,ngoTv,itemTv,about;

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        findViews();
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
                selectActionForDonate();
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

            }
        });
        donateTv = findViewById(R.id.tv_doner);
        donateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

            }
        });
        ngoTv = findViewById(R.id.tv_ngo);
        ngoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Launcher.this,ListNgo.class));
//                finish();

            }
        });
        itemTv=findViewById(R.id.tv_item_donate);
        itemTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Launcher.this,Beneficiaries.class);
                i.putExtra("title","List of Item Donate");
                i.putExtra("node","DonateItem");
                startActivity(i);
//                finish();
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

    private void notification() {
        Toast.makeText(this,"notification  clicked",Toast.LENGTH_LONG).show();
    }
}
