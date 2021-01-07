package com.example.helpinghandadmin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AcceptedWorkerProfileActivity extends AppCompatActivity {
    JobClass job;
    CircleImageView profileIV;
    TextView nameTV;
    TextView addressTV;
    TextView preferredLocationTV;
    TextView workTypeTV;
    TextView genderTV;
    TextView salaryTV;
    TextView phoneTV;
    ImageView frontIV;
    ImageView backIV;
    private static final int CALL_REQUEST_CODE=7893;
    FirebaseDatabase database;
    ProgressDialog progress;
    DatabaseReference acceptedJobDatabase;
    DatabaseReference notificationDatabase;
    FirebaseStorage storage;
    Button rejectBT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_worker_profile);
        Intent intent=getIntent();
        job= (JobClass) intent.getSerializableExtra("profile");
        rejectBT=findViewById(R.id.rejectBT);
        if(intent.getStringExtra("norejet")!=null){
            rejectBT.setEnabled(false);
            rejectBT.setVisibility(View.GONE);
        }
        profileIV=findViewById(R.id.profileIV);
        nameTV=findViewById(R.id.nameTV);
        addressTV=findViewById(R.id.addressTV);
        preferredLocationTV=findViewById(R.id.preferredAddressTV);
        workTypeTV=findViewById(R.id.jobTV);
        genderTV=findViewById(R.id.genderTV);
        salaryTV=findViewById(R.id.expectedSalaryTV);
        phoneTV=findViewById(R.id.phoneTV);
        frontIV=findViewById(R.id.frontIV);
        backIV=findViewById(R.id.backIV);
        storage= FirebaseStorage.getInstance();
        database= FirebaseDatabase.getInstance();
        acceptedJobDatabase=database.getReference("accepted Jobs").child(job.getWorkType()).child(job.getId());
        notificationDatabase=database.getReference("Notifications").child(job.getId());
        progress=new ProgressDialog(this);
        progress.setTitle("Helping Hand");
        progress.setMessage("Wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.setProgress(0);




        Picasso.get().load(job.getProfileImage()).into(profileIV);
        nameTV.setText(job.getName());
        addressTV.setText("Address "+job.getAddress());
        preferredLocationTV.setText("Preferred Location "+job.getPreferredAddress());
        workTypeTV.setText(job.getWorkType());
        genderTV.setText(job.getGender());
        salaryTV.setText(job.getExpectedSalary()+" BDT");
        phoneTV.setText(job.getPhone());
        Picasso.get().load(job.getFrontImage()).into(frontIV);
        Picasso.get().load(job.getBackImage()).into(backIV);
    }

    public void call(View view) {
        if(ContextCompat.checkSelfPermission(AcceptedWorkerProfileActivity.this,
                Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AcceptedWorkerProfileActivity.this,
                    new String[] {Manifest.permission.CALL_PHONE},CALL_REQUEST_CODE);
        }else {
            String dial="tel:"+job.getPhone();
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }


    public void reject(View view) {
        progress.show();
        StorageReference front =storage.getReferenceFromUrl(job.getFrontImage());
        front.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    StorageReference back=storage.getReferenceFromUrl(job.getBackImage());
                    back.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){


                                acceptedJobDatabase.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Notification notification=new Notification(job.getId(),"" +
                                                    "You request for job "+job.getWorkType()+" at salary "+job.getExpectedSalary()+" BDT is rejected");
                                            String key=notificationDatabase.push().getKey();
                                            notificationDatabase.child(key).setValue(notification).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(AcceptedWorkerProfileActivity.this,"Notification Sent",Toast.LENGTH_SHORT).show();
                                                    }else {
                                                        Toast.makeText(AcceptedWorkerProfileActivity.this,task.getException().getLocalizedMessage()+"\nNotification Sent",Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });
                                            progress.dismiss();
                                            Toast.makeText(AcceptedWorkerProfileActivity.this,"Done",Toast.LENGTH_SHORT).show();
                                        }else {
                                            progress.dismiss();
                                            Toast.makeText(AcceptedWorkerProfileActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else {
                                progress.dismiss();
                                Toast.makeText(AcceptedWorkerProfileActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    progress.dismiss();
                    Toast.makeText(AcceptedWorkerProfileActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==CALL_REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                String dial="tel:"+job.getPhone();
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            } else {
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }
}