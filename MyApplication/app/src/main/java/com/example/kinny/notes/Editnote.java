package com.example.kinny.notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashSet;

public class Editnote extends AppCompatActivity implements TextWatcher {

    int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnote);

        EditText editText = (EditText) findViewById(R.id.multiAutoCompleteTextView);
        Intent i = getIntent();
        noteId = i.getIntExtra("noteId  ", 0);

        if(noteId != -1){
            editText.setText(MainActivity.notes.get(noteId));
        }

        editText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        MainActivity.notes.set(noteId, String.valueOf(s));
        MainActivity.arrayAdapter.notifyDataSetChanged();

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.kinny.notes", Context.MODE_PRIVATE);

        if(MainActivity.set == null){
            MainActivity.set = new HashSet<String>();
        }else{
            MainActivity.set.clear();
        }

        MainActivity.set.addAll(MainActivity.notes);
        sharedPreferences.edit().remove("notes").apply();
        sharedPreferences.edit().putStringSet("notes", MainActivity.set).apply();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
