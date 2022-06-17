package com.docoding.clickcare.activities.pasien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.docoding.clickcare.R;
import com.docoding.clickcare.adapter.ChatAdapter;
import com.docoding.clickcare.databinding.ActivityChatDoctorBinding;
import com.docoding.clickcare.helper.PreferenceManager;
import com.docoding.clickcare.model.ChatMessage;
import com.docoding.clickcare.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatDoctorActivity extends AppCompatActivity {
    Button endChat;

    private ActivityChatDoctorBinding binding;
    private UserModel receiverUser;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;
    private String conversionId = null;
    private Boolean isReceiverAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadReceiverDetails();
        setListeners();
        init();
        listenMessage();

//        endChat = findViewById(R.id.end_chat);
//        endChat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent homeActivity = new Intent(ChatDoctorActivity.this, HomeActivity.class);
//                homeActivity.putExtra(HomeActivity.END_CHAT, "endchat");
//                startActivity(homeActivity);
//            }
//        });
    }

    private String getReadabledateTime(Date date){
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }
//
    private final EventListener<QuerySnapshot> eventListener = ((value, error) -> {
        if (error != null){
            return;
        }
        if (value != null){
            int count = chatMessages.size();
            for (DocumentChange documentChange : value.getDocumentChanges()){
                if (documentChange.getType() == DocumentChange.Type.ADDED){
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString("senderId");
                    chatMessage.receiverId = documentChange.getDocument().getString("receiverId");
                    chatMessage.message = documentChange.getDocument().getString("message");
                    chatMessage.dateTime = getReadabledateTime(documentChange.getDocument().getDate("timestamp"));
                    chatMessage.dateObject = documentChange.getDocument().getDate("timestamp");
                    chatMessages.add(chatMessage);
                }
            }
            Collections.sort(chatMessages, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if (count == 0){
                chatAdapter.notifyDataSetChanged();
            } else {
                chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size() -1);
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
//        }
        binding.progressBar.setVisibility(View.GONE);
//        if (conversionId == null){
//            checkForConversion();
        }
    });

    private void listenMessage(){
        database.collection("chat")
                .whereEqualTo("senderId", preferenceManager.getString("uid"))
                .whereEqualTo("receiverId", receiverUser.userUID)
                .addSnapshotListener(eventListener);
        database.collection("chat")
                .whereEqualTo("senderId", receiverUser.userUID)
                .whereEqualTo("receiverId", preferenceManager.getString("uid"))
                .addSnapshotListener(eventListener);
    }

    private void sendMessage(){
        HashMap<String, Object> message = new HashMap<>();
        message.put("senderId", preferenceManager.getString("uid"));
        message.put("receiverId", receiverUser.userUID);
        message.put("message", binding.inputMessage.getText().toString());
        message.put("timestamp", new Date());
        database.collection("chat").add(message);
        binding.inputMessage.setText(null);
//        if (conversionId != null){
//            updateConversion(binding.inputMessage.getText().toString());
//        }

//        else {
//            HashMap<String, Object> conversion = new HashMap<>();
//            conversion.put("senderId", preferenceManager.getString("uid"));
//            conversion.put("senderName", preferenceManager.getString("name"));
//            conversion.put("senderImage", preferenceManager.getString("image"));
//            conversion.put("receiverId", receiverUser.userUID);
//            conversion.put("receiverName", receiverUser.username);
//            conversion.put("receiverImage", receiverUser.image);
//            conversion.put("lastMessage", binding.inputMessage.getText().toString());
//            conversion.put("timestamp", new Date());
//            addConversion(conversion);
//        }
//        if(!isReceiverAvailable){
//            try {
//                JSONArray tokens = new JSONArray();
//                tokens.put(receiverUser.token);
//
//                JSONObject data = new JSONObject();
//                data.put("uid", preferenceManager.getString("uid"));
//                data.put("name", preferenceManager.getString("name"));
//                data.put("fcmToken", preferenceManager.getString("fcmToken"));
//                data.put("message", binding.inputMessage.getText().toString());
//
//                JSONObject body = new JSONObject();
//                body.put(Constants.REMOTE_MSG_DATA, data);
//                body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);
//
//                sendNotification(body.toString());
//            } catch (Exception exception){
//                showToast(exception.getMessage());
//            }
//        }
//        binding.inputMessage.setText(null);
    }



//    private void showToast(String message){
//        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//    }
//
//
//    private void updateConversion(String message){
//        DocumentReference documentReference =
//                database.collection("conversations").document(conversionId);
//        documentReference.update(
//                "LastMessage", message,
//                "timestamp", new Date()
//        );
//    }
//
//    private void addConversion(HashMap<String, Object> conversion){
//        database.collection("conversations")
//                .add(conversion)
//                .addOnSuccessListener(documentReference -> conversionId = documentReference.getId());
//    }
//
    private void init(){
        preferenceManager = new PreferenceManager(getApplicationContext());
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                preferenceManager.getString("uid")
        );
        binding.chatRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();
    }
    private Bitmap getBitmapFromEncodedString(String encodedImage){
        if (encodedImage != null){
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }
    }
//
//    private void checkForConversion(){
//        if (chatMessages.size() !=0){
//            checkForConversionRemotely(
//                    preferenceManager.getString("uid"),
//                    receiverUser.userUID
//            );
//            checkForConversionRemotely(
//                    receiverUser.userUID,
//                    preferenceManager.getString("uid")
//            );
//        }
//    }

    private void loadReceiverDetails(){
        receiverUser = (UserModel) getIntent().getSerializableExtra("user");
        binding.textName.setText(receiverUser.username);
    }

    private void setListeners(){
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.sendBtn.setOnClickListener(view -> sendMessage());
    }

//    private final OnCompleteListener<QuerySnapshot> conversionOnCompleteListener = task -> {
//        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0){
//            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
//            conversionId = documentSnapshot.getId();
//        }
//    };
//
//    private void checkForConversionRemotely(String senderId, String receiverId){
//        database.collection("conversations")
//                .whereEqualTo("senderId", senderId)
//                .whereEqualTo("receiverId", receiverId)
//                .get()
//                .addOnCompleteListener(conversionOnCompleteListener);
//    }
}