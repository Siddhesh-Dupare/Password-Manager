package com.oto.passwordmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.MyViewHolder> {
    Context context;
    ArrayList<Account> list;
    public AccountAdapter(Context context, ArrayList<Account> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AccountAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.account, parent, false);
        return new  MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountAdapter.MyViewHolder holder, int position) {
        Account account = list.get(position);
        holder.name.setText(account.getName());
        holder.email.setText(account.getEmail());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, email;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.titleTextView);
            email = itemView.findViewById(R.id.emailTextView);
        }
    }
}
