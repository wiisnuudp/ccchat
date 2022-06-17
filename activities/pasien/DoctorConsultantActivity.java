package com.docoding.clickcare.activities.pasien;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.docoding.clickcare.adapter.DoctorAdapter;
import com.docoding.clickcare.adapter.RecentConversationsAdapter;
import com.docoding.clickcare.databinding.ActivityDoctorConsultantBinding;
import com.docoding.clickcare.dummydata.DoctorDummy;
import com.docoding.clickcare.helper.OnItemClickCallback;
import com.docoding.clickcare.listeners.ConversationsListener;
import com.docoding.clickcare.model.ChatMessage;
import com.docoding.clickcare.model.DoctorModel;
import com.docoding.clickcare.model.UserModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DoctorConsultantActivity extends AppCompatActivity{
    private ActivityDoctorConsultantBinding binding;
    private ArrayList<DoctorModel> listDoctors = new ArrayList<>();
    private List<ChatMessage> conversations;
    private RecentConversationsAdapter conversationsAdapter;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorConsultantBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        init();

//        binding.doctorList.setHasFixedSize(true);

        binding.filterDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerLayout.open();
            }
        });

        binding.fabNewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UsersActivity.class));
            }
        });

        listDoctors.addAll(DoctorDummy.ListData());
//        showRecycleListDoctor();
    }

//    private void init(){
//        conversations = new ArrayList<>();
//        conversationsAdapter = new RecentConversationsAdapter(conversations, this);
//        binding.conversationsRecyclerView.setAdapter(conversationsAdapter);
//        database = FirebaseFirestore.getInstance();
//    }

//    @Override
//    public void onConversationClicked(UserModel userModel) {
//        Intent intent = new Intent(getApplicationContext(), ChatDoctorActivity.class);
//        intent.putExtra("user", userModel);
//        startActivity(intent);
//    }

    //    public void showRecycleListDoctor() {
//        binding.doctorList.setLayoutManager(new LinearLayoutManager(this));
//        DoctorAdapter listNewsAdapter = new DoctorAdapter(listDoctors);
//        binding.doctorList.setAdapter(listNewsAdapter);
//
//        listNewsAdapter.setOnItemClickCallback(new OnItemClickCallback() {
//            @Override
//            public void onItemClicked(DoctorModel doctorModel) {
//                Intent detailFood = new Intent(DoctorConsultantActivity.this, DetailDoctorActivity.class);
//                detailFood.putExtra(DetailDoctorActivity.ITEM_EXTRA, doctorModel);
//                startActivity(detailFood);
//            }
//        });
//    }
}