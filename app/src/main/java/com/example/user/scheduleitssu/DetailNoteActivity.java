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
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.user.scheduleitssu.DataClass.Note;
import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorContent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class DetailNoteActivity extends AppCompatActivity {
    private static final int DETAILNOTEACTIVITY_REQUEST = 1234;
    FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
    DatabaseReference databaseReference=firebaseDatabase.getReference();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    Note note;
    String serialized;
    Editor renderer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detailnote_toolbar);
        toolbar.setTitle("DetailNoteActivity");
        processIntent();
        setnote();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    void processIntent(){
        Intent getIntent=getIntent();
        if(getIntent.getStringExtra("EXIST").equals("EXIST")&&getIntent.getStringExtra("DATATYPE").equals("NOTE")){
            if(getIntent.getStringExtra("NOTEINFOTYPE").equals("NOTEINFO_CONTENT")){
            note= getIntent.getParcelableExtra("DATA");
            serialized= note.getContent();
            }else{
            /*노트가 없으면 클릭 자체가 안되므로 else에 오는 경우는 없음*/
            }
        }
    }
void setnote(){
    renderer= (Editor)findViewById(R.id.renderer);
    String content= serialized;
    EditorContent Deserialized= renderer.getContentDeserialized(content);
    renderer.render(Deserialized);
}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DETAILNOTEACTIVITY_REQUEST && resultCode == RESULT_OK){
            String serialized= data.getStringExtra("NOTECONTENT");
            //Log.d("DETAILNOTEACTIVITY","Success: "+serialized);
            this.serialized=serialized;
            String content= serialized;
            EditorContent Deserialized2= renderer.getContentDeserialized(content);
            renderer.render(Deserialized2);

        }else if(requestCode == DETAILNOTEACTIVITY_REQUEST && resultCode == RESULT_CANCELED){
            //Log.d("DETAILNOTEACTIVITY","result-cancled..");
        }else{
           // Log.d("DETAILNOTEACTIVITY","what??? ");
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
                intent.putExtra("EXIST","EXIST");
                intent.putExtra("DATATYPE","NOTE");
                intent.putExtra("NOTEINFOTYPE","NOTEINFO_CONTENT");
                intent.putExtra("DATA",note);

                setResult(RESULT_OK, intent);
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
   /* public Map<Integer, String> getHeadingTypeface() {
        Map<Integer, String> typefaceMap = new HashMap<>();
        typefaceMap.put(Typeface.NORMAL, "fonts/GreycliffCF-Bold.ttf");
        typefaceMap.put(Typeface.BOLD, "fonts/GreycliffCF-Heavy.ttf");
        typefaceMap.put(Typeface.ITALIC, "fonts/GreycliffCF-Heavy.ttf");
        typefaceMap.put(Typeface.BOLD_ITALIC, "fonts/GreycliffCF-Bold.ttf");
        return typefaceMap;
    }
    public Map<Integer, String> getContentface() {
        Map<Integer, String> typefaceMap = new HashMap<>();
        typefaceMap.put(Typeface.NORMAL, "fonts/Lato-Medium.ttf");
        typefaceMap.put(Typeface.BOLD, "fonts/Lato-Bold.ttf");
        typefaceMap.put(Typeface.ITALIC, "fonts/Lato-MediumItalic.ttf");
        typefaceMap.put(Typeface.BOLD_ITALIC, "fonts/Lato-BoldItalic.ttf");
        return typefaceMap;
    }*/

}

