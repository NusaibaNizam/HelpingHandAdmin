package com.example.helpinghandadmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    RecyclerView jobApplicantsRV;
    ArrayList<AppNotification> jobs;
    FirebaseRecyclerOptions<AppNotification> jobOptions;
    FirebaseRecyclerAdapter<AppNotification, NotificationHolder> jobAdapter;
    private DatabaseReference adminNotificationDatabase;
    private DatabaseReference acceptedJobDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        database=FirebaseDatabase.getInstance();
        acceptedJobDatabase = database.getReference("accepted Jobs");

        acceptedJobDatabase.keepSynced(true);
        adminNotificationDatabase=database.getReference("adminNotifications");

        adminNotificationDatabase.keepSynced(true);
        jobs=new ArrayList<>();
        jobApplicantsRV=findViewById(R.id.jobApplicantsRV);
        jobApplicantsRV.setHasFixedSize(true);
        jobApplicantsRV.setLayoutManager(new LinearLayoutManager(this));
        jobOptions=new FirebaseRecyclerOptions.Builder<AppNotification>().setQuery(adminNotificationDatabase,AppNotification.class).build();
        jobAdapter = new FirebaseRecyclerAdapter<AppNotification, NotificationHolder>(jobOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final NotificationHolder holder, int position, @NonNull final AppNotification model) {

                acceptedJobDatabase.child(model.getWorkType()).child(model.getFromID()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final JobClass jobClass=dataSnapshot.getValue(JobClass.class);
                        holder.notifTV.setText(jobClass.getName()+model.getText());




                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(NotificationActivity.this,AcceptedWorkerProfileActivity.class);
                                intent.putExtra("profile",jobClass);
                                intent.putExtra("notif",model);
                                intent.putExtra("norejet","norejet");
                                startActivity(intent);
                            }
                        });


                        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(NotificationActivity.this);
                                builder1.setMessage("Remove Notification?");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                delete(model.getNotifID());
                                                dialog.cancel();
                                            }
                                        });

                                builder1.setNegativeButton(
                                        "No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();

                                return true;
                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public NotificationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new NotificationHolder(LayoutInflater.from(NotificationActivity.this)
                        .inflate(R.layout.row_notification,viewGroup,false));
            }
        };

        jobApplicantsRV.setAdapter(jobAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        jobAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        jobAdapter.stopListening();
    }



    private void delete(String notifID) {
        adminNotificationDatabase.child(notifID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(NotificationActivity.this,"Deleted",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(NotificationActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.notificatio_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.accepted_jobIT:
                Intent intent=new Intent(NotificationActivity.this,AcceptedWorkersActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.applicantIT:
                Intent intent1=new Intent(NotificationActivity.this,AppliedJobActivity.class);
                startActivity(intent1);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
