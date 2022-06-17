package com.docoding.clickcare.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.docoding.clickcare.databinding.ItemContainerUserBinding;
import com.docoding.clickcare.listeners.UsersListener;
import com.docoding.clickcare.model.UserModel;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>{

    private final List<UserModel> userModels;
    private final UsersListener usersListener;

    public UsersAdapter(List<UserModel> userModels, UsersListener usersListener) {
        this.userModels = userModels;
        this.usersListener = usersListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerUserBinding itemContainerUserBinding = ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false
        );
        return new UserViewHolder(itemContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(userModels.get(position));
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        ItemContainerUserBinding binding;

        UserViewHolder(ItemContainerUserBinding itemContainerUserBinding){
            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;
        }
        void setUserData(UserModel userModel) {
            binding.textName.setText(userModel.getUsername());
            binding.textEmail.setText(userModel.getEmail());
            binding.getRoot().setOnClickListener(v -> usersListener.onUserClicked(userModel));
        }
    }
}
