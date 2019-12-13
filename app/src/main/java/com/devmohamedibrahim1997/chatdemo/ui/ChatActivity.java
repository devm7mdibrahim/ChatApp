package com.devmohamedibrahim1997.chatdemo.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.devmohamedibrahim1997.chatdemo.R;
import com.devmohamedibrahim1997.chatdemo.adapter.MessageAdapter;
import com.devmohamedibrahim1997.chatdemo.databinding.ActivityChatBinding;
import com.devmohamedibrahim1997.chatdemo.pojo.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.devmohamedibrahim1997.chatdemo.adapter.ContactsAdapter.USER_ID;
import static com.devmohamedibrahim1997.chatdemo.adapter.ContactsAdapter.USER_NAME;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding chatBinding;
    private List<Message> messageList = null;
    private MessageAdapter messageAdapter;
    private String receiverId;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatBinding = DataBindingUtil.setContentView(this,R.layout.activity_chat);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        initToolBar();
        initRecyclerView();
        getIntentData();



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("messages").child(receiverId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readMessages(currentUser.getUid(),receiverId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        onSendButtonClicked();
    }

    private void getIntentData() {
        receiverId = getIntent().getStringExtra(USER_ID);
        String receiverName = getIntent().getStringExtra(USER_NAME);
        chatBinding.chatUserNameTextView.setText(receiverName);
    }

    private void initToolBar() {
        Toolbar chatToolBar = chatBinding.chatToolBar;
        setSupportActionBar(chatToolBar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        chatToolBar.setNavigationOnClickListener(view -> finish());
    }

    private void initRecyclerView() {
        RecyclerView messagesRecyclerView = chatBinding.messagesRecyclerView;
        messagesRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messagesRecyclerView.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageAdapter(ChatActivity.this);
        messageAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int messageCount = messageAdapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (messageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    chatBinding.messagesRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        messagesRecyclerView.setAdapter(messageAdapter);
    }

    private void onSendButtonClicked() {

        chatBinding.sendMessageButton.setOnClickListener(view -> {
            String message = chatBinding.messageEditText.getText().toString();

            if(!message.equals("")){
                assert currentUser != null;
                sendMessage(currentUser.getUid(),receiverId,message);
                chatBinding.messageEditText.setText("");
            }
        });
    }

    private void sendMessage(String sender, String receiver,String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("messages").push().setValue(hashMap);
    }

    private void readMessages(String myId, String userId){
     messageList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("messages");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Message message = snapshot.getValue(Message.class);

                    assert message != null;
                    if ((message.getReceiver().equals(myId) && message.getSender().equals(userId))
                            || (message.getReceiver().equals(userId) && message.getSender().equals(myId))) {
                        messageList.add(message);

                    }
                }
                messageAdapter.setMessages(messageList);
                messageAdapter.notifyItemInserted(messageList.size() -1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
