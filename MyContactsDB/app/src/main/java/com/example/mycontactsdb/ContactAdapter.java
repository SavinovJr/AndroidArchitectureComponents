package com.example.mycontactsdb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycontactsdb.databinding.ContactsListItemBinding;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    public interface OnContactListener {
        void onContactTapped(Contact contact, int position);
    }

    private List<Contact> contactList = new ArrayList<>();

    private OnContactListener listener;

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
        notifyDataSetChanged();
    }

    public void setListener(OnContactListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContactsListItemBinding
                contactsListItemBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                                        R.layout.contacts_list_item, parent, false);

        return new ContactViewHolder(contactsListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, final int position) {
        final Contact contact = contactList.get(position);

        holder.contactsListItemBinding.setContact(contact);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onContactTapped(contact, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        private ContactsListItemBinding contactsListItemBinding;

        public ContactViewHolder(@NonNull ContactsListItemBinding contactsListItemBinding) {
            super(contactsListItemBinding.getRoot());

            this.contactsListItemBinding = contactsListItemBinding;
        }
    }
}
