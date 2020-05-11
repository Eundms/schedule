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
import com.example.user.scheduleitssu.DataClass.Subject;
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
    private static final int DETAILNOTEACTIVITY_REQUEST = 11;
    //////////////////////////////////////////////////////////////////
    /*firebase 을 위해서*/

    FirebaseDatabase database= FirebaseDatabase.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    String userid=user.getDisplayName()+"_"+user.getUid();
    DatabaseReference myRef=database.getReference().child("Student").child(userid).child("Subject");

    //////////////////////////////////////////////////////////////////
    Subject subject;
    Note note;
    String serialized;
    Editor renderer;
    int position;
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
        if(getIntent.getStringExtra("EXIST").equals("EXIST")&&getIntent.getStringExtra("DATATYPE").equals("SUBJECT")){
            if(getIntent.getStringExtra("SUBJECTINFOTYPE").equals("SUBJECTINFO_CONTENT")){
            subject= getIntent.getParcelableExtra("DATA");
            position=Integer.parseInt(getIntent.getStringExtra("POSITION"));
            note=subject.getNotelist().get(position);
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
            note.setContent(serialized);
            this.serialized=serialized;
            String content= serialized;
            EditorContent Deserialized2= renderer.getContentDeserialized(content);
            renderer.render(Deserialized2);

//////////////////////////////////////////////////////////////////
            /*firebase 함수수*/

            Map<String,Object>editnote=new HashMap<>();
            editnote.put(Integer.toString(position),note);
            myRef.child("Subject_"+subject.getClassname()).child("notelist").updateChildren(editnote);
//////////////////////////////////////////////////////////////////

        }else if(requestCode == DETAILNOTEACTIVITY_REQUEST && resultCode == RESULT_CANCELED){
        }else{
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
            case android.R.id.home:{
                finish();
                return true;
            }
            case R.id.detailnote_edit_menu:{
                Intent intent = new Intent(this, EditNoteActivity.class);
                intent.putExtra("EXIST","EXIST");
                intent.putExtra("DATATYPE","SUBJECT");
                intent.putExtra("SUBJECTINFOTYPE","SUBJECTINFO_CONTENT");
                intent.putExtra("POSITION",Integer.toString(position));
                intent.putExtra("DATA",subject);
                setResult(RESULT_OK, intent);

                startActivityForResult(intent,DETAILNOTEACTIVITY_REQUEST);
                return true;
            }
            case R.id.detailnote_delete_menu:{
                //삭제하는 기능(firebase) NOTELIST를 아제 올리고 내려야 함.
               ///////////////////////////////////////////////
                /*delete note하는 코드를 옮겨야함  */
                subject.getNotelist().remove(position);
                Map<String,Object>deletenote=new HashMap<>();
                deletenote.put("notelist",subject.getNotelist());
                myRef.child("Subject_"+subject.getClassname()).updateChildren(deletenote);


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

