package com.example.mycontactsdb;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.mycontactsdb.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.Update;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ContactAdapter.OnContactListener {

    private MyContactsDataBase myContactsDataBase;
    private List<Contact> myContactsList;
    private ContactAdapter adapter;

    private ActivityMainBinding activityMainBinding;
    private MainActivityButtonHandler buttonHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myContactsDataBase =
                Room.databaseBuilder(getApplicationContext(), MyContactsDataBase.class, "ContactsDB").build();

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        buttonHandler = new MainActivityButtonHandler(this);
        activityMainBinding.setButtonHandler(buttonHandler);

        RecyclerView recyclerView = activityMainBinding.layoutContentMain.contactsRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new ContactAdapter();
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addEditContact(false, null, -1);
//            }
//        });

        loadContacts();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                    @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Contact contact = myContactsList.get(viewHolder.getAdapterPosition());
                deleteContact(contact);
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadContacts() {
        new GetAllContactsAsyncTask().execute();
    }

    private void deleteContact(Contact contact) {
        new DeleteContactAsyncTask().execute(contact);
    }

    private void addContact(String firstName, String lastName, String email, String phoneNumber) {
        Contact contact = new Contact(0, firstName, lastName, email, phoneNumber);
        new AddContactAsyncTask().execute(contact);
    }

    private void updateContact(String firstName, String lastName, String email, String phoneNumber, int position) {
        Contact contact = myContactsList.get(position);
        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        contact.setEmail(email);
        contact.setPhoneNumber(phoneNumber);
        new UpdateContactAsyncTask().execute(contact);

        myContactsList.set(position, contact);
    }

    @Override
    public void onContactTapped(Contact contact, int position) {
        addEditContact(true, contact, position);
    }

    private class GetAllContactsAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            myContactsList = myContactsDataBase.getContactDao().getAllContacts();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.setContactList(myContactsList);
        }
    }

    private class DeleteContactAsyncTask extends AsyncTask<Contact, Void, Void> {
        @Override
        protected Void doInBackground(Contact... contacts) {
            myContactsDataBase.getContactDao().deleteContact(contacts[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            loadContacts();
        }
    }

    private class AddContactAsyncTask extends AsyncTask<Contact, Void, Void> {
        @Override
        protected Void doInBackground(Contact... contacts) {
            myContactsDataBase.getContactDao().insertContact(contacts[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            loadContacts();
        }
    }

    private class UpdateContactAsyncTask extends AsyncTask<Contact, Void, Void> {
        @Override
        protected Void doInBackground(Contact... contacts) {
            myContactsDataBase.getContactDao().updateContact(contacts[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            loadContacts();
        }
    }

    private void addEditContact(final boolean isUpdate, final Contact contact, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.add_edit_contact, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        TextView contactTitleTextView = view.findViewById(R.id.contact_title_text_view);
        final EditText firstNameEditText = view.findViewById(R.id.first_name_edit_text);
        final EditText lastNameEditText = view.findViewById(R.id.last_name_edit_text);
        final EditText emailEditText = view.findViewById(R.id.email_edit_text);
        final EditText phoneNumberEditText = view.findViewById(R.id.phone_number_edit_text);

        contactTitleTextView.setText(isUpdate ? "Edit Contact" : "Add Contact");
        if (isUpdate && contact != null) {
            firstNameEditText.setText(contact.getFirstName());
            lastNameEditText.setText(contact.getLastName());
            emailEditText.setText(contact.getEmail());
            phoneNumberEditText.setText(contact.getPhoneNumber());
        }
        builder.setCancelable(false);
        builder.setPositiveButton(isUpdate ? "Update" : "Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(firstNameEditText.getText())) {
                    Toast.makeText(MainActivity.this, "Enter first name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(lastNameEditText.getText())) {
                    Toast.makeText(MainActivity.this, "Enter last name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(phoneNumberEditText.getText())) {
                    Toast.makeText(MainActivity.this, "Enter phone number", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(emailEditText.getText())) {
                    Toast.makeText(MainActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                } else {
                    if (isUpdate && contact != null) {
                        updateContact(firstNameEditText.getText().toString(), lastNameEditText.getText().toString(),
                                      emailEditText.getText().toString(), phoneNumberEditText.getText().toString(),
                                      position);
                    } else if (!isUpdate) {
                        addContact(firstNameEditText.getText().toString(), lastNameEditText.getText().toString(),
                                   emailEditText.getText().toString(), phoneNumberEditText.getText().toString());
                    }
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public class MainActivityButtonHandler {
        private Context context;

        MainActivityButtonHandler(Context context) {
            this.context = context;
        }

        public void onButtonClicked(View view) {
            addEditContact(false, null, -1);
        }
    }
}
