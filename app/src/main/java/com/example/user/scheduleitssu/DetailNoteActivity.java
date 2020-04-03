package com.example.user.scheduleitssu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.irshulx.Editor;

import java.io.IOException;


public class DetailNoteActivity extends AppCompatActivity {
FragmentManager fmswitch_edit_detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detailnote_toolbar);
        toolbar.setTitle("DetailNoteActivity");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        fmswitch_edit_detail=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fmswitch_edit_detail.beginTransaction();
        fragmentTransaction.add(R.id.fragment_detailOReditnote,new DetailNoteFragment(),"DETAILNOTEFRAGMENT");
        fragmentTransaction.commit();


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
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_detailOReditnote, new EditNoteFragment(),"EDITNOTEFRAGMENT");
                fragmentTransaction.commit();
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       // Log.d("onActivityResult!!", "" +requestCode+ resultCode);
        Fragment fragment=getSupportFragmentManager().findFragmentByTag("EDITNOTEFRAGMENT");
        fragment.onActivityResult(requestCode,resultCode,data);

    }

}

