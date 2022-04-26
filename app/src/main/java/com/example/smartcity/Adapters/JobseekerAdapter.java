package com.example.smartcity.Adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcity.AdminViews.AdminAdd;
import com.example.smartcity.Globals;
import com.example.smartcity.Models.JobseekerModel;
import com.example.smartcity.UserViews.JobseekerActivity;
import com.example.smartcity.Models.TravelModel;
import com.example.smartcity.R;
import com.example.smartcity.UserViews.TravelActivity;
import com.example.smartcity.Utils.Database.DatabaseManager;

import java.util.List;

public class JobseekerAdapter extends RecyclerView.Adapter<JobseekerAdapter.ViewHolder> {
    List<JobseekerModel> jobseekerList;
    private JobseekerActivity jobseekerActivity;

    public JobseekerAdapter(JobseekerActivity jobseekerActivity) {
        this.jobseekerActivity = jobseekerActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.jobseeker_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(JobseekerAdapter.ViewHolder holder, int position) {
        JobseekerModel item = jobseekerList.get(position);
        holder.jobseekerName.setText(item.getJobseekerName());
        holder.jobseekerDescription.setText(item.getJobseekerDescription());
        holder.NumberOfVacancies.setText("10"); // item.getNumberOfvacancies;
        Log.d("job","JOB SEEKER: " + holder.NumberOfVacancies.getText().toString());

        if ((Integer.parseInt(holder.NumberOfVacancies.getText().toString()) == 0) || Globals.currentUser.equals("admin")) {
            holder.applyButton.setVisibility(View.INVISIBLE);
        }
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(jobseekerActivity.getApplicationContext(), "Editing mode", Toast.LENGTH_SHORT).show();

                // go to AdminAdd with the existing information
                final Intent i = new Intent(jobseekerActivity.getApplication(), AdminAdd.class);
                i.putExtra("category", "travel");
                i.putExtra("title", item.getJobseekerName());
                i.putExtra("description", item.getJobseekerDescription());
                i.putExtra("vacancies", item.getNumberOfVacancies());
                i.putExtra("id", item.getId());
                jobseekerActivity.startActivity(i);
                jobseekerActivity.finish();
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(jobseekerActivity.getApplicationContext(), "Delete mode", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                Toast.makeText(jobseekerActivity.getApplicationContext(), "Delete Confirmed!", Toast.LENGTH_SHORT).show();
                                DatabaseManager databaseManager = new DatabaseManager();
                                databaseManager.deleteData("job post", item.getId());
                                jobseekerList.remove(position);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();
            }
        });

        /*holder.applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(jobseekerActivity.getApplicationContext(), "Applied Successfully", Toast.LENGTH_SHORT).show();
                holder.applyButton.setEnabled(false);

                holder.NumberOfVacancies.setText(String.valueOf(Integer.parseInt(item.getNumberOfVacancies()) - 1));
            }
        });*/
    }





    @Override
    public int getItemCount() {
        return jobseekerList.size();
    }

    public void setJobseekerList(List<JobseekerModel> jobseekerList) {
        this.jobseekerList = jobseekerList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView jobseekerName;
        TextView jobseekerDescription;
        TextView NumberOfVacancies;
        Button editButton;
        Button deleteButton;
        Button applyButton;

        ViewHolder(View view) {
            super(view);
            jobseekerName = view.findViewById(R.id.jobseekerNameText);
            jobseekerDescription = view.findViewById(R.id.jobseekerDescription);
            NumberOfVacancies = view.findViewById(R.id.NumberOfvacancies);
            editButton = view.findViewById(R.id.jobseekerEdit);
            deleteButton = view.findViewById(R.id.jobseekerDelete);
            applyButton = view.findViewById(R.id.jobseekerApply);

            if (!Globals.currentUser.equals("admin")) {
                editButton.setVisibility(View.INVISIBLE);
                deleteButton.setVisibility(View.INVISIBLE);
            }
        }
    }
}

