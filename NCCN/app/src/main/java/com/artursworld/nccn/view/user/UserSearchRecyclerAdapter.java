package com.artursworld.nccn.view.user;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.util.Global;
import com.artursworld.nccn.model.entity.User;

import java.util.List;

public class UserSearchRecyclerAdapter extends RecyclerView.Adapter<UserSearchRecyclerAdapter.ViewHolder> {

    private String CLASS_NAME = UserSearchRecyclerAdapter.class.getSimpleName();
    private final List<User> data;

    public UserSearchRecyclerAdapter(final List<User> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_list_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textView.setText(data.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                User selectedUser = data.get(position);
                Log.i(CLASS_NAME, "clicked on: " + selectedUser);
                Global.setSelectedUserName(selectedUser.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
        }
    }

}
