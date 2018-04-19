package com.example.sev_user.bookmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by sev_user on 4/11/2018.
 */
import com.example.sev_user.bookmanagement.Book;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLite";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Book_Management";

    private static final String TABLE_BOOK = "book";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String CONTENT = "content";
    private static final String AUTHOR = "author";
    private static final String PUBLISHER = "publisher";
    private static final String IMAGE = "image";
    private static final String ALARM = "alarm";
    Calendar cal;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String script = "Create table " + TABLE_BOOK + "("
                + ID + " INTEGER PRIMARY KEY, " + NAME + " TEXT, "
                + AUTHOR + " TEXT,"
                + CONTENT + " TEXT,"
                + PUBLISHER + " TEXT,"
                + IMAGE + " TEXT, "
                + ALARM + " TEXT" + ")";
        db.execSQL(script);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int onlVersion, int newVersion) {
        // drop neu da co
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK);
        // tao lai
        onCreate(db);
    }

//    public void createDefaultBook() {
//        int count = this.getNoteCount();
////        Log.e("test db", String.valueOf(count));
//        if (count == 0) {
//            Book book1 = new Book(1, "Toi thay hoa vang tren co xanh", "Nguyen Nhat Anh", " Chuyen ke doi thuong", "Kim Dong", "android.resource://com.example.sev_user.bookmanagement/" + R.drawable.one, "");
//            Book book2 = new Book(2, "Bong bong len troi", "Nguyen Nhat Anh", " Chuyen ke doi thuong", "Kim Dong", "android.resource://com.example.sev_user.bookmanagement/" + R.drawable.two, "");
//            this.addBook(book1);
//            this.addBook(book2);
//        }
//    }

    public Book getBook(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        cal = Calendar.getInstance();
        Cursor cursor = db.query(TABLE_BOOK, new String[]{ID, NAME, AUTHOR, CONTENT, PUBLISHER, IMAGE, ALARM}, ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Book book = new Book(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6));

        return book;
    }

    public List<Book> getAllBooks() {
        List<Book> lsBook = new ArrayList<Book>();

        String selectQuery = "SELECT * FROM " + TABLE_BOOK;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cal = Calendar.getInstance();
        // Duyet con tro va them vao danh sach

        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.setId(Integer.parseInt(cursor.getString(0)));
                book.setName(cursor.getString(1));
                book.setAuthor(cursor.getString(2));
                book.setContent(cursor.getString(3));
                book.setPublisher(cursor.getString(4));
                book.setImage(cursor.getString(5));
                book.setAlarm(cursor.getString(6));
                lsBook.add(book);
            }
            while (cursor.moveToNext());
        }
//        Log.e("test get all", String.valueOf(lsBook.size()));
        return lsBook;
    }

    public void addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        cal = Calendar.getInstance();

        ContentValues values = new ContentValues();
        values.put(ID, book.getId());
        values.put(NAME, book.getName());
        values.put(AUTHOR, book.getAuthor());
        values.put(CONTENT, book.getContent());
        values.put(PUBLISHER, book.getPublisher());
        values.put(IMAGE, book.getImage());
        values.put(ALARM, "");
        db.insert(TABLE_BOOK, null, values);

        db.close();
    }

//    public int getNoteCount() {
//        String countQuery = "SELECT * FROM " + TABLE_BOOK;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//
//        int count = cursor.getCount();
//        cursor.close();
//
//        return count;
//    }

    public int updateBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, book.getName());
        values.put(AUTHOR, book.getAuthor());
        values.put(CONTENT, book.getContent());
        values.put(PUBLISHER, book.getPublisher());
        values.put(IMAGE, book.getImage());
        values.put(ALARM, book.getAlarm());
        // updating now
        return db.update(TABLE_BOOK, values, ID + " =?",
                new String[]{String.valueOf(book.getId())});
    }

    public void deleteBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOK, ID + " = ?",
                new String[]{String.valueOf(book.getId())});
        db.close();
    }

}
