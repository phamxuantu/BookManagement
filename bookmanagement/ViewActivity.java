package com.example.sev_user.bookmanagement;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by sev_user on 4/17/2018.
 */

public class ViewActivity extends AppCompatActivity {


    Book book;

    private ImageView imageView;

    private TextView TextName;
    private TextView TextAut;
    private TextView TextPub;
    private TextView TextCon;
   // private Uri selectedImauri;
   // private String selectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_main);

        this.TextName = (TextView)findViewById(R.id.txtName);
        this.TextCon = (TextView)findViewById(R.id.txtCon);
        this.TextAut = (TextView)findViewById(R.id.txtAut);
        this.TextPub = (TextView)findViewById(R.id.txtPub);
        this.imageView = (ImageView) findViewById(R.id.imvBook);

        Intent intent = this.getIntent();
        this.book = (Book)intent.getSerializableExtra("book");
        this.TextName.setText(book.getName());
        this.TextAut.setText(book.getAuthor());
        this.TextCon.setText(book.getContent());
        this.TextPub.setText(book.getPublisher());
        Uri uriImage = Uri.parse(book.getImage());
        this.imageView.setImageURI(uriImage);
    }


}
