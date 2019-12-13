package com.devmohamedibrahim1997.chatdemo.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devmohamedibrahim1997.chatdemo.R;
import com.devmohamedibrahim1997.chatdemo.adapter.ContactsAdapter;
import com.devmohamedibrahim1997.chatdemo.databinding.FragmentContactsDataBinding;
import com.devmohamedibrahim1997.chatdemo.pojo.Contact;
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

public class ContactsFragment extends Fragment {

    private List<Contact> mContactsList;
    private ContactsAdapter contactsAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        FragmentContactsDataBinding contactsDataBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),R.layout.fragment_contacts,container,false);

        RecyclerView contactsRecyclerView = contactsDataBinding.contactsRecyclerView;
        contactsAdapter = new ContactsAdapter(getContext());
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        contactsRecyclerView.setHasFixedSize(true);
        contactsRecyclerView.setAdapter(contactsAdapter);
        mContactsList = new ArrayList<>();

        getContactsFromFireBase();
        return contactsDataBinding.getRoot();
    }

    private void getContactsFromFireBase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("contacts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mContactsList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Contact contact = snapshot.getValue(Contact.class);

                    assert contact != null;
                    assert currentUser != null;

                    if(!contact.getId().equals(currentUser.getUid())){
                        mContactsList.add(contact);
                    }
                }
                contactsAdapter.setContacts(mContactsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
