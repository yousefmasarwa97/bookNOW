package com.myapp.booknow;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {



    private List<BusinessService> serviceList;


    ServiceAdapter(List<BusinessService> list){
        this.serviceList = list;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_item, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        BusinessService serviceItem = serviceList.get(position);
        holder.serviceNameTextView.setText(serviceItem.getName());

        // Handle edit service
        holder.editServiceButton.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(v.getContext(), BusinessServiceEditActivity.class);
                intent.putExtra("serviceId", serviceItem.getServiceId());
                v.getContext().startActivity(intent);
            }catch (Exception e){
                Log.e("ServiceAdapter", "Error starting EditServiceActivity", e);
            }

            });

        // Handle delete service
        holder.deleteServiceButton.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {

        return serviceList.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView serviceNameTextView;
        ImageView editServiceButton, deleteServiceButton;

        public ServiceViewHolder(View itemView) {
            super(itemView);
            serviceNameTextView = itemView.findViewById(R.id.serviceNameTextView);
            editServiceButton = itemView.findViewById(R.id.editServiceButton);
            deleteServiceButton = itemView.findViewById(R.id.deleteServiceButton);
        }
    }




}
