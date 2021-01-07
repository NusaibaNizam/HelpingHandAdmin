package com.example.helpinghandadmin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class WorkersListActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference acceptedJobDatabase;
    RecyclerView jobApplicantsRV;
    ArrayList<JobClass> jobs;
    FirebaseRecyclerOptions<JobClass> jobOptions;
    FirebaseRecyclerAdapter<JobClass, JobViewHolder> jobAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers_list);
        jobApplicantsRV=findViewById(R.id.jobApplicantsRV);
        database=FirebaseDatabase.getInstance();
        if (database == null)
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Intent intent=getIntent();
        String type=intent.getStringExtra("type");
        acceptedJobDatabase=database.getReference("accepted Jobs").child(type);
        acceptedJobDatabase.keepSynced(true);
        jobs=new ArrayList<>();



        jobApplicantsRV.setHasFixedSize(true);
        jobApplicantsRV.setLayoutManager(new LinearLayoutManager(this));
        jobOptions=new FirebaseRecyclerOptions.Builder<JobClass>().setQuery(acceptedJobDatabase,JobClass.class).build();
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
                        Intent intent=new Intent(WorkersListActivity.this,AcceptedWorkerProfileActivity.class);
                        intent.putExtra("profile",model);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public JobViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new JobViewHolder(LayoutInflater.from(WorkersListActivity.this)
                        .inflate(R.layout.row_jobs,viewGroup,false));
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
}
