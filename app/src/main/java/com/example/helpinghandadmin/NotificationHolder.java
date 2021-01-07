package com.example.helpinghandadmin;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class NotificationHolder extends RecyclerView.ViewHolder {
    TextView notifTV;
    public NotificationHolder(@NonNull View itemView) {
        super(itemView);
        notifTV=itemView.findViewById(R.id.notifText);
    }
}
