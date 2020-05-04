package com.example.user.scheduleitssu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorContent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;


public class DetailNoteActivity extends AppCompatActivity {
    private static final int DETAILNOTEACTIVITY_REQUEST = 1234;
    FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
    DatabaseReference databaseReference=firebaseDatabase.getReference();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DETAILNOTEACTIVITY_REQUEST && resultCode == RESULT_OK){
            String serialized= data.getStringExtra("NOTECONTENT");
            Log.d("DETAILNOTEACTIVITY","Success: "+serialized);
/*********************************************************************************************/
            /*이 코드어디에 넣을지 생각해서 넣어야 함*/
            /* Editor renderer= (Editor)findViewById(R.id.renderer);
            Map<Integer, String> headingTypeface = getHeadingTypeface();
            Map<Integer, String> contentTypeface = getContentface();
            renderer.setHeadingTypeface(headingTypeface);
            renderer.setContentTypeface(contentTypeface);
            renderer.setDividerLayout(R.layout.tmpl_divider_layout);
            renderer.setEditorImageLayout(R.layout.tmpl_image_view);
            renderer.setListItemLayout(R.layout.tmpl_list_item);
            String content= serialized;
            EditorContent Deserialized= renderer.getContentDeserialized(content);
            renderer.setEditorListener(new EditorListener() {
                @Override
                public void onTextChanged(EditText editText, Editable text) {
                }
                @Override
                public void onUpload(Bitmap image, String uuid) {
                }
                @Override
                public View onRenderMacro(String name, Map<String, Object> settings, int index) {
                    View view = getLayoutInflater().inflate(R.layout.layout_authored_by, null);
                    return view;
                }
            });
            renderer.render(Deserialized);*/
/*********************************************************************************************/
        }else if(requestCode == DETAILNOTEACTIVITY_REQUEST && resultCode == RESULT_CANCELED){
            Log.d("DETAILNOTEACTIVITY","result-cancled..");
        }else{
            Log.d("DETAILNOTEACTIVITY","what??? ");
        }
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
                startActivityForResult(intent,DETAILNOTEACTIVITY_REQUEST);
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

