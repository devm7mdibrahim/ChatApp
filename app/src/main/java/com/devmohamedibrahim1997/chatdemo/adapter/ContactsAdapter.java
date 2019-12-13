package com.devmohamedibrahim1997.chatdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.devmohamedibrahim1997.chatdemo.R;
import com.devmohamedibrahim1997.chatdemo.databinding.ContactsDataBinding;
import com.devmohamedibrahim1997.chatdemo.pojo.Contact;
import com.devmohamedibrahim1997.chatdemo.ui.ChatActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsHolder> {


    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";
    private Context mContext;
    private List<Contact> mContactsList = null;

    public ContactsAdapter(Context context){
        mContext = context;
    }

    @NonNull
    @Override
    public ContactsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContactsDataBinding contactsDataBinding =DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.contacts_item,parent,false);
        return new ContactsHolder(contactsDataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsHolder holder, int position) {
        Contact contact = mContactsList.get(position);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ChatActivity.class);
            intent.putExtra(USER_ID, contact.getId());
            intent.putExtra(USER_NAME,contact.getUserName());
            mContext.startActivity(intent);
        });
        holder.bindContact(contact);
    }

    @Override
    public int getItemCount() {
        return mContactsList!= null? mContactsList.size():0;
    }

    public void setContacts(List<Contact> contactList){
        mContactsList = contactList;
        notifyDataSetChanged();
    }

    class ContactsHolder extends RecyclerView.ViewHolder {

        ContactsDataBinding mContactsDataBinding;
        ContactsHolder(@NonNull ContactsDataBinding contactsDataBinding) {
            super(contactsDataBinding.getRoot());
            mContactsDataBinding = contactsDataBinding;
        }

        void bindContact(Contact contact) {
            mContactsDataBinding.setContact(contact);
        }
    }
}
