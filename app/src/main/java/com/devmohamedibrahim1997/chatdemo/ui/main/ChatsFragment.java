package com.devmohamedibrahim1997.chatdemo.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devmohamedibrahim1997.chatdemo.R;
import com.devmohamedibrahim1997.chatdemo.adapter.ContactsAdapter;
import com.devmohamedibrahim1997.chatdemo.databinding.FragmentChatsDataBinding;
import com.devmohamedibrahim1997.chatdemo.pojo.Contact;
import com.devmohamedibrahim1997.chatdemo.pojo.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatsFragment extends Fragment {

    private FragmentChatsDataBinding chatsDataBinding;
    private ContactsAdapter chatsAdapter;
    private List<String> mChatsUsersList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        chatsDataBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),R.layout.fragment_chats,container,false);

        initRecyclerView();
        getChatsUsers();

        return chatsDataBinding.getRoot();
    }

    private void initRecyclerView() {
        RecyclerView chatsRecyclerView = chatsDataBinding.chatsRecyclerView;
        chatsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        chatsRecyclerView.setLayoutManager(layoutManager);
        chatsAdapter = new ContactsAdapter(getContext());
        chatsRecyclerView.setAdapter(chatsAdapter);
    }

    private void getChatsUsers() {
        mChatsUsersList = new ArrayList<>();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("messages");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChatsUsersList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Message message = snapshot.getValue(Message.class);

                    assert message != null;
                    assert currentUser != null;

                    if(message.getSender().equals(currentUser.getUid())){
                        mChatsUsersList.add(message.getReceiver());
                    }

                    if(message.getReceiver().equals(currentUser.getUid())){
                        mChatsUsersList.add(message.getSender());
                    }
                }

                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readChats() {
        List<Contact> mContactsList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("contacts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mContactsList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Contact mContact = snapshot.getValue(Contact.class);

                    for(String chatUserId: mChatsUsersList){
                        assert mContact != null;
                        if(mContact.getId().equals(chatUserId)){
                            if(mContactsList.size()!= 0){
                                for(Contact contact: mContactsList){
                                    if(!mContact.getId().equals(contact.getId())){
                                        mContactsList.add(mContact);
                                    }
                                }
                            }else{
                                mContactsList.add(mContact);
                            }
                        }
                    }
                }

                chatsAdapter.setContacts(mContactsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
