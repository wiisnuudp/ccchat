package com.docoding.clickcare.activities.pasien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.docoding.clickcare.R;
import com.docoding.clickcare.adapter.UsersAdapter;
import com.docoding.clickcare.databinding.ActivityUsersBinding;
import com.docoding.clickcare.helper.PreferenceManager;
import com.docoding.clickcare.listeners.UsersListener;
import com.docoding.clickcare.model.UserModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity implements UsersListener {

    private ActivityUsersBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(binding.getRoot());
        getUsers();
    }

    private void getUsers(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("Users").get().addOnCompleteListener(
                task -> {
                    loading(false);
                    String currentUserId = preferenceManager.getString("uid");
                    if (task.isSuccessful() && task.getResult() != null){
                        List<UserModel> users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if (currentUserId.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }
                            UserModel userModel = new UserModel();
                            userModel.username = queryDocumentSnapshot.getString("name");
                            userModel.email = queryDocumentSnapshot.getString("email");
                            userModel.image = queryDocumentSnapshot.getString("image");
                            userModel.token = queryDocumentSnapshot.getString("fcmToken");
                            userModel.userUID = queryDocumentSnapshot.getId();
                            users.add(userModel);
                        }
                        if (users.size() > 0){
                            UsersAdapter usersAdapter = new UsersAdapter(users, this);
                            binding.usersRecyclerView.setAdapter(usersAdapter);
                            binding.usersRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            showErrorMessage();
                        }
                    } else {
                        showErrorMessage();
                    }
                }
        );
    }

    private void showErrorMessage(){
        binding.textErrorMessage.setText(String.format("%s,", "No user Available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading){
        if (isLoading){
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(UserModel userModel) {
        Intent intent = new Intent(getApplicationContext(), ChatDoctorActivity.class);
        intent.putExtra("user", userModel);
        startActivity(intent);
        finish();
    }
}