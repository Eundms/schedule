package com.example.user.scheduleitssu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class DetailNoteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detailnote_toolbar);
        toolbar.setTitle("DetailNoteActivity");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_detailnote, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //맞는지 모르겠음
                finish();
                return true;
            }
            case R.id.detailnote_edit_menu:{
                Intent intent = new Intent(this, EditNoteActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.detailnote_delete_menu:{
                //삭제하는 기능(firebase)
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}

