package com.example.helpinghandadmin;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class JobViewHolder extends RecyclerView.ViewHolder {
    CircleImageView profileIV;
    TextView nameTV;
    TextView jobTV;
    TextView preferredLocationTV;
    TextView salaryTV;
    TextView genderTV;
    public JobViewHolder(@NonNull View itemView) {
        super(itemView);
        profileIV=itemView.findViewById(R.id.profileIV);
        nameTV=itemView.findViewById(R.id.nameTV);
        jobTV=itemView.findViewById(R.id.jobTV);
        preferredLocationTV=itemView.findViewById(R.id.preferredLocationTV);
        salaryTV=itemView.findViewById(R.id.salaryTV);
        genderTV=itemView.findViewById(R.id.genderTV);
    }
}
