package com.example.helpinghandadmin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.OutputStreamWriter;

import de.hdodenhof.circleimageview.CircleImageView;

public class ApplicantProfileActivity extends AppCompatActivity {
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
    DatabaseReference acceptedJobDatabase;
    ProgressDialog progress;
    DatabaseReference applyJobDatabase;
    DatabaseReference notificationDatabase;
    FirebaseStorage storage;
    private DatabaseReference appNotificationDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_profile);
        Intent intent=getIntent();
        job= (JobClass) intent.getSerializableExtra("profile");
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
        storage=FirebaseStorage.getInstance();
        database=FirebaseDatabase.getInstance();
        acceptedJobDatabase=database.getReference("accepted Jobs");
        applyJobDatabase=database.getReference("appliedJobs");
        notificationDatabase=database.getReference("Notifications").child(job.getId());
        appNotificationDatabase=database.getReference("appNotifications");
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
        if(ContextCompat.checkSelfPermission(ApplicantProfileActivity.this,
                Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ApplicantProfileActivity.this,
                    new String[] {Manifest.permission.CALL_PHONE},CALL_REQUEST_CODE);
        }else {
            String dial="tel:"+job.getPhone();
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    public void approve(View view) {
        progress.show();
        acceptedJobDatabase.child(job.getWorkType()).child(job.getId()).setValue(job).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    applyJobDatabase.child(job.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                String appkey=appNotificationDatabase.child(job.getId()).push().getKey();
                                AppNotification appNot=new AppNotification("admin",job.getId(),"Your Application Was Approved By Admin For "+job.workType,"admin",appkey,job.workType);
                                appNotificationDatabase.child(job.getId()).child(appkey).setValue(appNot).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Notification notification=new Notification(job.getId(),"" +
                                                    "You request for job "+job.getWorkType()+" at salary "+job.getExpectedSalary()+" BDT is accepted");
                                            String key=notificationDatabase.push().getKey();

                                            notificationDatabase.child(key).setValue(notification).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){

                                                        Toast.makeText(ApplicantProfileActivity.this,"Notification Sent",Toast.LENGTH_SHORT).show();
                                                    }else {
                                                        Toast.makeText(ApplicantProfileActivity.this,task.getException().getLocalizedMessage()+"\nNotification Sent",Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                                progress.dismiss();
                                Toast.makeText(ApplicantProfileActivity.this,"Done",Toast.LENGTH_SHORT).show();
                            }else {
                                progress.dismiss();
                                Toast.makeText(ApplicantProfileActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {

                    progress.dismiss();
                    Toast.makeText(ApplicantProfileActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
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


                                applyJobDatabase.child(job.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            String appkey=appNotificationDatabase.child(job.getId()).push().getKey();
                                            AppNotification appNot=new AppNotification("admin",job.getId(),"Your Application Was Rejected By Admin For "+job.workType,"admin",appkey,job.workType);
                                            appNotificationDatabase.child(job.getId()).child(appkey).setValue(appNot).addOnCompleteListener(new OnCompleteListener<Void>() {
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

                                                                    Toast.makeText(ApplicantProfileActivity.this,"Notification Sent",Toast.LENGTH_SHORT).show();
                                                                }else {
                                                                    Toast.makeText(ApplicantProfileActivity.this,task.getException().getLocalizedMessage()+"\nNotification Sent",Toast.LENGTH_SHORT).show();

                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                            progress.dismiss();
                                            Toast.makeText(ApplicantProfileActivity.this,"Done",Toast.LENGTH_SHORT).show();
                                        }else {
                                            progress.dismiss();
                                            Toast.makeText(ApplicantProfileActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else {
                                progress.dismiss();
                                Toast.makeText(ApplicantProfileActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    progress.dismiss();
                    Toast.makeText(ApplicantProfileActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
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
