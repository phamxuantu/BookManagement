package com.example.sev_user.bookmanagement;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.StringTokenizer;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by sev_user on 4/16/2018.
 */

public class AddActivity extends AppCompatActivity {

    Book book;
    private static final int mode_create = 1;
    private static final int mode_edit = 2;
    private int mode;

    private ImageButton imageButton;
    private String selectedImagePath;
    private EditText editTextName;
    private EditText editTextAut;
    private EditText editTextPub;
    private EditText editTextCon;
    private Uri selectedImauri;

    private  Button btnhuy;
    private boolean needRefresh;
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_main);

        if (EasyPermissions.hasPermissions(this, galleryPermissions)) {
//            pickImageFromGallery();
        } else {
            EasyPermissions.requestPermissions(this, "Access for storage",
                    101, galleryPermissions);
        }

        ///////////////////////////////////////////////////////////////
        this.editTextName = (EditText)findViewById(R.id.edName);
        this.editTextAut = (EditText)findViewById(R.id.edTacGia);
        this.editTextCon = (EditText)findViewById(R.id.edNoiDung);
        this.editTextPub = (EditText)findViewById(R.id.edNXB);

        // x? lý s? ki?n click vào imagebutton d? truy c?p vào gallery
        imageButton=(ImageButton) findViewById(R.id.Image);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select picture"), 102);


            }
        });

        ////////////////////////////////////////////////////////////
        Intent intent = this.getIntent();
        this.book = (Book)intent.getSerializableExtra("book");
        if(book==null){
            this.mode = mode_create;
        }
        else {
            this.mode = mode_edit;
            this.editTextName.setText(book.getName());
            this.editTextAut.setText(book.getAuthor());
            this.editTextCon.setText(book.getContent());
            this.editTextPub.setText(book.getPublisher());
//            Log.e("test img db", book.getImage());
            Uri uriImage = Uri.fromFile(new File(book.getImage()));
            this.imageButton.setImageURI(uriImage);
        }

        //dong y
        Button btok= (Button) findViewById(R.id.btnDongY);
        btok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                buttonSaveClicked();
                Intent intent= new Intent(AddActivity.this,MainActivity.class);
                //   startActivityForResult(intent,101);
                startActivity(intent);

            }

        });

    }
    // nguoi dung nhan dong y
    public  void  buttonSaveClicked(){
        MyDatabaseHelper db = new MyDatabaseHelper(this);

        List<Book> ls = db.getAllBooks();
        int id = ls.size()+1;
        String name = this.editTextName.getText().toString();
        String content = this.editTextCon.getText().toString();
        String author = this.editTextAut.getText().toString();
        String publisher = this.editTextPub.getText().toString();
        String uriImage = selectedImagePath;

        if(name.equals("") || content.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please enter name & content", Toast.LENGTH_LONG).show();

           return;
        }

        if(mode==mode_create ) {
            this.book= new Book(id,name,author,content,publisher,uriImage,"");
            db.addBook(book);
        } else  {
            this.book.setName(name);
            this.book.setContent(content);
            this.book.setPublisher(publisher);
            this.book.setImage(uriImage);
            this.book.setAuthor(author);
            db.updateBook(book);
        }

        this.needRefresh = true;
        // Tr? l?i MainActivity.
        this.onBackPressed();
    }
    // nguoi dung nhan huy
    public  void buttonCancelClicked(View view){
        this.onBackPressed();
    }

    // khi activity add hoan thanh
    // co the can gui phan hoi gi do ve cho activity da goi no.
    @Override
    public void finish(){
        Intent data = new Intent();
        data.putExtra("needRefresh",needRefresh);
        this.setResult(Activity.RESULT_OK,data);
        super.finish();
    }
    // x? lý l?y ?nh t? gallery
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data)
    {
        if(resultCode==RESULT_OK) {
            if (requestCode == 100) {
                Uri contactUri = data.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE};
                Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
                cursor.moveToFirst();
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
                String number = cursor.getString(column);
                imageButton.setImageResource(column);
            } else if (requestCode == 102) {
                selectedImauri = data.getData();
                selectedImagePath = getPath(this, selectedImauri);
                Log.e("test path", selectedImagePath);
                imageButton.setImageURI(selectedImauri);
            }
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    String getPath(Context context, Uri uri) {
        Boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)){
                String docID = DocumentsContract.getDocumentId(uri);
                String[] split = docID.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)){
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDowloadsDocument(uri)) {
                String id = DocumentsContract.getDocumentId(uri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_download"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                String docID = DocumentsContract.getDocumentId(uri);
                String[] split = docID.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)){
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)){
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = "_id=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())){
            if (isGooglePhotosUri(uri)) return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = "_data";
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()){
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return null;
    }

    private boolean isDowloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


}
