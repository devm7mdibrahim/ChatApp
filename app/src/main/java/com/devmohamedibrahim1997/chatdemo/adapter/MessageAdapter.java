package com.devmohamedibrahim1997.chatdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.devmohamedibrahim1997.chatdemo.BR;
import com.devmohamedibrahim1997.chatdemo.R;
import com.devmohamedibrahim1997.chatdemo.pojo.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder>{

    private Context mContext;
    private List<Message> mMessagesList = null;

    public MessageAdapter(Context context){
        mContext = context;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), viewType, parent, false);
        return new MessageHolder(viewDataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        Message message = mMessagesList.get(position);
        holder.bindMessage(message);
    }

    @Override
    public int getItemCount() {
        return mMessagesList!= null? mMessagesList.size():0;
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        assert currentUser != null;
        if(mMessagesList.get(position).getSender().equals(currentUser.getUid())){
            return R.layout.chat_item_right;
        }else {
            return R.layout.chat_item_left;
        }
    }

    public void setMessages(List<Message> messageList){
        mMessagesList = messageList;
        notifyItemInserted(messageList.size() -1);
    }

    class MessageHolder extends RecyclerView.ViewHolder {

        ViewDataBinding mViewDataBinding;
        MessageHolder(@NonNull ViewDataBinding viewDataBinding) {
            super(viewDataBinding.getRoot());
            mViewDataBinding = viewDataBinding;
        }

        void bindMessage(Message message) {
            mViewDataBinding.setVariable(BR.message, message);
        }
    }
}
