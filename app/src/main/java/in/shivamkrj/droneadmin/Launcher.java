package in.shivamkrj.droneadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Launcher extends AppCompatActivity {

    TextView sewaTv,sewaTV1,needTv,donateTv,beneficaryTv,ngoTv,itemTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        findViews();
    }

    private void findViews() {
        sewaTv = findViewById(R.id.tv_sewa);
        sewaTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

            }
        });

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
