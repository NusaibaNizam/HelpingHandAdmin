package com.example.helpinghandadmin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AppliedJobActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    RecyclerView jobApplicantsRV;
    FirebaseDatabase database;
    DatabaseReference applyJobDatabase;
    ArrayList<JobClass> jobs;
    FirebaseRecyclerOptions<JobClass> jobOptions;
    FirebaseRecyclerAdapter<JobClass, JobViewHolder> jobAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        jobApplicantsRV=findViewById(R.id.jobApplicantsRV);
        database=FirebaseDatabase.getInstance();
        if (database == null)
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        applyJobDatabase=database.getReference("appliedJobs");
        applyJobDatabase.keepSynced(true);
        jobs=new ArrayList<>();



        jobApplicantsRV.setHasFixedSize(true);
        jobApplicantsRV.setLayoutManager(new LinearLayoutManager(this));
        jobOptions=new FirebaseRecyclerOptions.Builder<JobClass>().setQuery(applyJobDatabase,JobClass.class).build();
        jobAdapter = new FirebaseRecyclerAdapter<JobClass, JobViewHolder>(jobOptions) {
            @Override
            protected void onBindViewHolder(@NonNull JobViewHolder holder, int position, @NonNull final JobClass model) {
                Picasso.get().load(model.getProfileImage()).into(holder.profileIV);
                holder.nameTV.setText(model.getName());
                holder.jobTV.setText(model.getWorkType());
                holder.preferredLocationTV.setText("Preferred Location "+model.getPreferredAddress());
                holder.salaryTV.setText("Expected Salary "+model.getExpectedSalary()+" BDT");
                holder.genderTV.setText(model.getGender());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(AppliedJobActivity.this,ApplicantProfileActivity.class);
                        intent.putExtra("profile",model);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public JobViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new JobViewHolder(LayoutInflater.from(AppliedJobActivity.this)
                        .inflate(R.layout.row_jobs,viewGroup,false));
            }
        };

        jobApplicantsRV.setAdapter(jobAdapter);

    }
    @Override
    public void onStart() {
        super.onStart();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        SharedPreferences sp = getSharedPreferences("My ADMIN PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String y=sp.getString("key", "n");
        editor.commit();
        if(currentUser==null || !currentUser.isEmailVerified()){
            mAuth.signOut();
            Intent intent=new Intent(AppliedJobActivity.this,RegisterActivity.class);
            startActivity(intent);
            finish();
        }else {
            FirebaseDatabase database=FirebaseDatabase.getInstance();
            DatabaseReference admin=database.getReference("admin").child(currentUser.getUid());
            admin.setValue(currentUser.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        SharedPreferences sp = getSharedPreferences("My ADMIN PREFERENCES", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("key", "y");
                        editor.commit();
                    }else {
                        Toast.makeText(AppliedJobActivity.this,"An Error Has Occurred You Won't Get Admin Notifications.",Toast.LENGTH_SHORT).show();

                    }
                }
            });


            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    if(task.isSuccessful()){

                        FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid()).child("device_token")
                                .setValue(task.getResult().getToken()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                }else {}
                            }
                        });
                    }
                }
            });
        }
        jobAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        jobAdapter.stopListening();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.applied_job_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.accepted_jobIT:
                Intent intent=new Intent(AppliedJobActivity.this,AcceptedWorkersActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.notificationIT:
                Intent intent1=new Intent(AppliedJobActivity.this,NotificationActivity.class);
                startActivity(intent1);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
