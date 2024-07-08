package com.andromou.chatgpt.androidgpt.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andromou.chatgpt.androidgpt.data.Message;
import com.andromou.chatgpt.androidgpt.databinding.MainChatItemsBinding;
import com.andromou.chatgpt.androidgpt.ui.activities.RecyclerViewInterface;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    public static String copiedText;
    private final RecyclerViewInterface recyclerViewInterface;
    List<Message> messageList;

    public MessageAdapter(List<Message> messageList, RecyclerViewInterface recyclerViewInterface) {
        this.messageList = messageList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MainChatItemsBinding binding = MainChatItemsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Message message = messageList.get(position);
        if (message.getSentBy().equals(Message.SENT_BY_ME)) {
            holder.binding.chatgptCardView.setVisibility(View.GONE);
            holder.binding.userCardView.setVisibility(View.VISIBLE);
            holder.binding.userTextView.setText(message.getMessage());
        } else {
            holder.binding.userCardView.setVisibility(View.GONE);
            holder.binding.chatgptCardView.setVisibility(View.VISIBLE);
            holder.binding.chatgptTextView.setText(message.getMessage());
        }

        holder.binding.btnCopy.setOnClickListener(view -> {
            if (recyclerViewInterface != null) {
                recyclerViewInterface.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        MainChatItemsBinding binding;

        public MyViewHolder(MainChatItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
