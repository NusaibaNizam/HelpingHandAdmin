package com.example.helpinghandadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AcceptedWorkersActivity extends AppCompatActivity {

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_workers);


    }
    public void loadAssociates(View view) {
        switch (view.getId()){
            case R.id.cookBT:
                intent=new Intent(AcceptedWorkersActivity.this,WorkersListActivity.class);
                intent.putExtra("type","Cook");
                startActivity(intent);
                break;
            case R.id.cleanerBT:
                intent=new Intent(AcceptedWorkersActivity.this,WorkersListActivity.class);
                intent.putExtra("type","Cleaner");
                startActivity(intent);
                break;
            case R.id.pestControlBT:
                intent=new Intent(AcceptedWorkersActivity.this,WorkersListActivity.class);
                intent.putExtra("type","Pest Control");
                startActivity(intent);
                break;
            case R.id.carWashBT:
                intent=new Intent(AcceptedWorkersActivity.this,WorkersListActivity.class);
                intent.putExtra("type","Car Wash");
                startActivity(intent);
                break;
            case R.id.repairBT:
                intent=new Intent(AcceptedWorkersActivity.this,WorkersListActivity.class);
                intent.putExtra("type","Repairer");
                startActivity(intent);
                break;
            case R.id.driverBT:
                intent=new Intent(AcceptedWorkersActivity.this,WorkersListActivity.class);
                intent.putExtra("type","Driver");
                startActivity(intent);
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.accepted_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.applicantIT:
                Intent intent=new Intent(AcceptedWorkersActivity.this,AppliedJobActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.notificationIT:
                Intent intent1=new Intent(AcceptedWorkersActivity.this,NotificationActivity.class);
                startActivity(intent1);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
