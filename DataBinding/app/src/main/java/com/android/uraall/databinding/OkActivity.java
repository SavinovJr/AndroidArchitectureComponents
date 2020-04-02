package com.android.uraall.databinding;

import android.os.Bundle;

import com.android.uraall.databinding.databinding.ActivityOkBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.view.View;

public class OkActivity extends AppCompatActivity {

    private ActivityOkBinding activityOkBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        activityOkBinding = DataBindingUtil.setContentView(this, R.layout.activity_ok);
        activityOkBinding.setBook(getCurrentBook());
    }

    private Book getCurrentBook() {

        Book book = new Book();
        book.setTitle("Hamlet");
        book.setAuthor("Shakespeare");
        return book;

    }

}
