package com.example.user.scheduleitssu;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.example.user.scheduleitssu.DataClass.Note;
import com.example.user.scheduleitssu.DataClass.Subject;
import com.github.irshulx.Components.CustomEditText;
import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorContent;
import com.github.irshulx.models.EditorTextStyle;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import top.defaults.colorpicker.ColorPickerPopup;

public class EditNoteActivity extends AppCompatActivity implements View.OnClickListener{
    Editor editor;
    private static final String CLOUD_VISION_API_KEY = "AIzaSyDQnNiMu_Q50EdL7ryz1CHnJjwfqWtdXxE";
    public static final String FILE_NAME = "temp.jpg";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final int MAX_LABEL_RESULTS = 10;
    private static final int MAX_DIMENSION = 1200;
    private static final String TAG = EditNoteActivity.class.getSimpleName();
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 11;
    public static final int CAMERA_PERMISSIONS_REQUEST = 22;
    public static final int CAMERA_IMAGE_REQUEST = 33;
    //나중에 mImageDetails지우고 Editor에 값을 넣는 거 해야 함.
    private TextView mImageDetails;
    private ImageView mMainImage;
    String serailized;
    EditorContent des;
    Note note;
    Subject subject;
    int position;
    EditText notetitle;

    ImageButton note_adddate;
    ImageButton note_addtime;
    //date, time 저장해놓는 변수
    Date date_date;
    Date time_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.editnote_toolbar);
        toolbar.setTitle("editNoteActivity");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editor = findViewById(R.id.editor);
        notetitle=findViewById(R.id.note_title);

        note_adddate=findViewById(R.id.note_adddate);
        note_addtime=findViewById(R.id.note_addtime);
        note_adddate.setOnClickListener(this);
        note_addtime.setOnClickListener(this);
        setUpEditor();
        //insertMacro();

        processIntent();
        //firebaseinital();
        if(note!=null&&note.getTitle()!=null) { notetitle.setText(note.getTitle()); }

    }
    @Override
    public void onClick(View v) {
        //현재 년도, 월, 일
        Calendar cal = Calendar.getInstance();
        int Current_year = cal.get ( cal.YEAR );
        int Current_month = cal.get ( cal.MONTH ) ;
        int Current_date = cal.get ( cal.DATE ) ;

        switch (v.getId()){
            case R.id.note_adddate:
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditNoteActivity.this, new DatePickerDialog.OnDateSetListener(){
                            public void onDateSet(DatePicker view, int year, int month, int day){
                                Toast.makeText(getApplicationContext(),year+"년 "+(month+1)+"월 "+day +"일을 선택했습니다",Toast.LENGTH_LONG).show();
                            }
                        },Current_year,Current_month,Current_date);
                datePickerDialog.show();
                break;
            case R.id.note_addtime:
                //timepicker
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditNoteActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                Toast.makeText(getApplicationContext(),hour+"시 "+ minute +"분을 선택했습니다",Toast.LENGTH_LONG).show();
                            }
                        }, 12,30,false);
                timePickerDialog.show();
               break;
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //맞는지 모르겠음
                finish();
                return true;
            }
            case R.id.editnote_add_menu:{//저장
                //Log.d("aaaaaaa",""+notetitle.getText());




               /**/ String title;
               if(notetitle.getText()==null){title="";}
               else{title=""+notetitle.getText();}
                String text = editor.getContentAsSerialized();
                Intent intent = new Intent();
                //text가 NULL이 아니라면
                intent.putExtra("RESULT","OK");
                intent.putExtra("NOTETITLE",title);
                intent.putExtra("NOTECONTENT",text);
                setResult(RESULT_OK, intent);
                Log.d("EDITNOTEACTIVITY",text);
                Log.d("EDITNOTEACTIVITY"," \n"+text+"\n"+editor.getContentAsHTML(text));
                /*text가 NULL이라면
                intent.putExtra("RESULT","CANCLED");
                setResult(RESULT_CANCELED, intent);
                */
                finish();
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    void processIntent(){
        Intent getContents=getIntent();

        if(getContents.getStringExtra("EXIST").equals("EXIST")&&getContents.getStringExtra("DATATYPE").equals("SUBJECT")) {
            if(getContents.getStringExtra("SUBJECTINFOTYPE").equals("SUBJECTINFO_CONTENT")){
                subject=getContents.getParcelableExtra("DATA");
                position=Integer.parseInt(getContents.getStringExtra("POSITION"));
                note=subject.getNotelist().get(position);
                serailized=note.getContent();
                des = editor.getContentDeserialized(serailized);
                editor.render(des);
                /*setSerialRenderInProgress*/
            }
            else if(getContents.getStringExtra("NOTEINFOTYPE").equals("NOTEINFO_DEFAULT")){
            }
        }else if(getContents.getStringExtra("EXIST").equals("NO")){

        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d( "onActivityResult",""+requestCode+" "+resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == editor.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                editor.insertImage(bitmap);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uploadImage(data.getData());
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            uploadImage(photoUri);
        }else if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
            Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            // editor.RestoreState();
        }
    }
    private void setUpEditor() {
        findViewById(R.id.action_camera).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(EditNoteActivity.this, "camera" , Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(EditNoteActivity.this);
                builder
                        .setMessage(R.string.dialog_select_prompt)
                        .setPositiveButton(R.string.dialog_select_gallery, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditNoteActivity.this.startGalleryChooser();
                            }
                        })
                        .setNegativeButton(R.string.dialog_select_camera, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditNoteActivity.this.startCamera();
                            }
                        });
                builder.create().show();
            }
        });
        findViewById(R.id.action_h1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H1);
            }
        });
        findViewById(R.id.action_h2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H2);
            }
        });
        findViewById(R.id.action_h3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H3);
            }
        });
        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.BOLD);
            }
        });
        findViewById(R.id.action_Italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.ITALIC);
            }
        });
        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.INDENT);
            }
        });
        findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.BLOCKQUOTE);
            }
        });
        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.OUTDENT);
            }
        });
        findViewById(R.id.action_bulleted).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertList(false);
            }
        });
        findViewById(R.id.action_unordered_numbered).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertList(true);
            }
        });
        findViewById(R.id.action_hr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertDivider();
            }
        });
        findViewById(R.id.action_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ColorPickerPopup.Builder(EditNoteActivity.this)
                        .initialColor(Color.RED) // Set initial color
                        .enableAlpha(true) // Enable alpha slider or not
                        .okTitle("Choose")
                        .cancelTitle("Cancel")
                        .showIndicator(true)
                        .showValue(true)
                        .build()
                        .show(findViewById(android.R.id.content), new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                Toast.makeText(EditNoteActivity.this, "picked" + colorHex(color), Toast.LENGTH_LONG).show();
                                editor.updateTextColor(colorHex(color));
                            }
                            @Override
                            public void onColor(int color, boolean fromUser) {
                            }
                        });
            }
        });
        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.openImagePicker();
            }
        });
        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertLink();
            }
        });
        findViewById(R.id.action_erase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clearAllContents();
            }
        });


        editor.setDividerLayout(R.layout.tmpl_divider_layout);
        editor.setEditorImageLayout(R.layout.tmpl_image_view);
        editor.setListItemLayout(R.layout.tmpl_list_item);
        editor.setEditorListener(new EditorListener() {
            @Override
            public void onTextChanged(EditText editText, Editable text) {
                //여기서 인식이 되는 거
                // Toast.makeText(EditNoteActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpload(Bitmap image, String uuid) {
                Toast.makeText(EditNoteActivity.this, uuid+image, Toast.LENGTH_LONG).show();
                /**
                 * TODO do your upload here from the bitmap received and all onImageUploadComplete(String url); to insert the result url to
                 * let the editor know the upload has completed
                 * 이곳에서 이미지를 파이어베이스에 올려야함.
                 */
                Log.d("onUpload!!", "gs://scheduleitssu-685f7.appspot.com/" + uuid);

                editor.onImageUploadComplete("gs://scheduleitssu-685f7.appspot.com", uuid);
            }

            @Override
            public View onRenderMacro(String name, Map<String, Object> props, int index) {
                View view = getLayoutInflater().inflate(R.layout.layout_authored_by, null);
                Log.d("onRenderMacro",""+view);
                /*TextView lblName = layout.findViewById(R.id.lbl_author_name);
                lblName.setText(props.get("author-name"));
                return layout;*/
                return view;
            }

        });

    }
    private View insertMacro() {
        View view = getLayoutInflater().inflate(R.layout.layout_authored_by, null);
        Map<String, Object> map = new HashMap<>();
        map.put("author-name", "Alex Wong");
        map.put("date","12 July 2018");
        editor.insertMacro("author-tag",view, map);
        return view;
    }
    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                MAX_DIMENSION);

                callCloudVision(bitmap);

            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }
    private void callCloudVision(final Bitmap bitmap) {
        // Switch text to loading
        // mImageDetails.setText(R.string.loading_message);
        // Do the real work in an async task, because we need to use the network anyway
        try {
            AsyncTask<Object, Void, String> labelDetectionTask = new LableDetectionTask(this, prepareAnnotationRequest(bitmap));
            labelDetectionTask.execute();
        } catch (IOException e) {
            Log.d(TAG, "failed to make API request because of other IOException " +
                    e.getMessage());
        }
    }

    private Vision.Images.Annotate prepareAnnotationRequest(final Bitmap bitmap) throws IOException {
        HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        VisionRequestInitializer requestInitializer =
                new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                    /**
                     * We override this so we can inject important identifying fields into the HTTP
                     * headers. This enables use of a restricted cloud platform API key.
                     */
                    @Override
                    protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                            throws IOException {
                        super.initializeVisionRequest(visionRequest);

                        String packageName = getPackageName();
                        visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                        String sig = PackageManagerUtils.getSignature(getPackageManager(), packageName);

                        visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                    }
                };

        Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
        builder.setVisionRequestInitializer(requestInitializer);

        Vision vision = builder.build();

        BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                new BatchAnnotateImagesRequest();
        batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
            AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

            // Add the image
            Image base64EncodedImage = new Image();
            // Convert the bitmap to a JPEG
            // Just in case it's a format that Android understands but Cloud Vision
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // Base64 encode the JPEG
            base64EncodedImage.encodeContent(imageBytes);
            annotateImageRequest.setImage(base64EncodedImage);

            // add the features we want
            annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                Feature labelDetection = new Feature();
                labelDetection.setType("TEXT_DETECTION");
                labelDetection.setMaxResults(MAX_LABEL_RESULTS);
                add(labelDetection);
            }});

            // Add the list of one thing to the request
            add(annotateImageRequest);
        }});

        Vision.Images.Annotate annotateRequest =
                vision.images().annotate(batchAnnotateImagesRequest);
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotateRequest.setDisableGZipContent(true);
        Log.d(TAG, "created Cloud Vision request object, sending request");

        return annotateRequest;
    }



    private static class LableDetectionTask extends AsyncTask<Object, Void, String> {
        private final WeakReference<EditNoteActivity> mActivityWeakReference;
        private Vision.Images.Annotate mRequest;
        LableDetectionTask(EditNoteActivity activity, Vision.Images.Annotate annotate) {
            mActivityWeakReference = new WeakReference<>(activity);
            mRequest = annotate;
        }

        @Override
        protected String doInBackground(Object... params) {
            try {
                Log.d(TAG, "created Cloud Vision request object, sending request");
                BatchAnnotateImagesResponse response = mRequest.execute();
                return convertResponseToString(response);

            } catch (GoogleJsonResponseException e) {
                Log.d(TAG, "failed to make API request because " + e.getContent());
            } catch (IOException e) {
                Log.d(TAG, "failed to make API request because of other IOException " +
                        e.getMessage());
            }
            return "Cloud Vision API request failed. Check logs for details.";
        }

        protected void onPostExecute(String result) {
            EditNoteActivity activity = mActivityWeakReference.get();
            if (activity != null && !activity.isFinishing()) {

               Editor editor=activity.findViewById(R.id.editor);
               editor.render("<p>"+result+"</p>");


            }
        }

    }
    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_editnote, menu);
        return true;
    }

    public void startGalleryChooser() {
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                    GALLERY_IMAGE_REQUEST);
        }
    }
    public void startCamera() {
        if (PermissionUtils.requestPermission(
                this,
                CAMERA_PERMISSIONS_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }
    public File getCameraFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }


    private String colorHex(int color) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return String.format(Locale.getDefault(), "#%02X%02X%02X", r, g, b);
    }



    private static String convertResponseToString(BatchAnnotateImagesResponse response) {
        StringBuilder message = new StringBuilder("");

        List<EntityAnnotation> labels = response.getResponses().get(0).getTextAnnotations();
        if (labels != null) {
                message.append(String.format(Locale.US, "%s", labels.get(0).getDescription()));
                message.append("\n");

        } else {
            message.append("nothing");
        }

        return message.toString();
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit Editor?")
                .setMessage("Are you sure you want to exit the editor?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }


}
   /* @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setGhost((Button) findViewById(R.id.editnote_add_menu));
    }
    public static void setGhost(Button button) {
        int radius = 4;
        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);
        background.setStroke(4, Color.WHITE);
        background.setCornerRadius(radius);
        button.setBackgroundDrawable(background);
    }*/
