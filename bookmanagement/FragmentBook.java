package com.example.sev_user.bookmanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentBook extends Fragment {


    private static final int menu_item_view = 111;
    private static final int menu_item_edit = 222;
    private static final int menu_item_create = 333;
    private static final int menu_item_delete = 444;
    private static final int my_request_code = 1000;


    private ListView listView;

    private ArrayList<Book> lsBook = new ArrayList<Book>();
    private ListAdapter lsViewAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.listview, container, false);
        listView = (ListView) v.findViewById(R.id.ListView01);
        MyDatabaseHelper db = new MyDatabaseHelper(MainActivity.context);
//        db.createDefaultBook();
        List<Book> ls = db.getAllBooks();

        lsBook.addAll(ls);

        lsViewAdapter = new ListAdapter(MainActivity.context, R.layout.content_main, lsBook);

        // dang ki adapter cho listview
        this.listView.setAdapter(this.lsViewAdapter);

        // dang ki context menu cho listview
        registerForContextMenu(this.listView);
        return v;

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0, menu_item_view, 0, "View Book");
        menu.add(0, menu_item_create, 1, "Set Reminder");
        menu.add(0, menu_item_edit, 2, "Edit Book");
        menu.add(0, menu_item_delete, 4, "Delete Book");
    }
    private Calendar calendar = Calendar.getInstance();
    Button btnDate;
    Button btnTime;
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            btnDate.setText(DateFormat.getDateInstance().format(calendar.getTime()));
        }
    };
    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hour, int minutes) {
            calendar.set(Calendar.HOUR, hour);
            calendar.set(Calendar.MINUTE, minutes);
            btnTime.setText(DateFormat.getTimeInstance().format(calendar.getTime()));
        }
    };
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final Book selectedBook = (Book) this.listView.getItemAtPosition(info.position);

        if(item.getItemId() == menu_item_view){
            Intent intent = new Intent(MainActivity.context, ViewActivity.class);
            //   intent.putExtra("book", selectedBook);
            intent.putExtra("book",selectedBook);

            // Start AddEditNoteActivity, có ph?n h?i.
            this.startActivityForResult(intent,my_request_code);
        }

            else if(item.getItemId() == menu_item_edit ){
            Intent intent = new Intent(MainActivity.context, AddActivity.class);
            //   intent.putExtra("book", selectedBook);
            intent.putExtra("book",selectedBook);

            // Start AddEditNoteActivity, có ph?n h?i.
            this.startActivityForResult(intent,my_request_code);
        }
        else if(item.getItemId() == menu_item_delete){
            // H?i tru?c khi xóa.
            new AlertDialog.Builder(MainActivity.context)
                    .setMessage("Are you sure want to delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            deleteNote(selectedBook);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        else if (item.getItemId() == menu_item_create) {
            final Context context = MainActivity.context;
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Add a new reminder");
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View view = inflater.inflate(R.layout.select_time, null);
            btnDate = (Button) view.findViewById(R.id.date);
            btnTime = (Button) view.findViewById(R.id.time);
            btnDate.setText(DateFormat.getDateInstance().format(calendar.getTime()));
            btnTime.setText(DateFormat.getTimeInstance().format(calendar.getTime()));
            btnDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DatePickerDialog(MainActivity.context, d,
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                    ).show();

                }
            });
            btnTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new TimePickerDialog(MainActivity.context, t,
                            calendar.get(Calendar.HOUR),
                            calendar.get(Calendar.MINUTE), true
                    ).show();

                }

            });

            builder.setView(view);
            builder.setPositiveButton("Set Reminder", new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    selectedBook.setAlarm(btnDate.getText().toString()+" "+btnTime.getText().toString());
                    MyDatabaseHelper db = new MyDatabaseHelper(MainActivity.context);
                    db.updateBook(selectedBook);
                    FragmentAlarm.lsBook = new ArrayList<>();
                    List<Book> ls = db.getAllBooks();
                    FragmentAlarm.lsBook.addAll(ls);
                    FragmentAlarm.lsViewAdapter = new AlarmAdapter(MainActivity.context, R.layout.list_row, FragmentAlarm.lsBook);
                    FragmentAlarm.listView.setAdapter(FragmentAlarm.lsViewAdapter);
                    registerForContextMenu(FragmentAlarm.listView);
                    MainActivity.viewPager.setCurrentItem(1);
                    //   lsViewAdapter.notifyDataSetChanged();
                    Util.scheduleJob(MainActivity.context,calendar);
                }
            });
            builder.show();
        }
        else {
            return false;
        }
        return true;
    }

    // Ngu?i dùng d?ng ý xóa m?t Note.
    private void deleteNote(Book book)  {
        MyDatabaseHelper db = new MyDatabaseHelper(MainActivity.context);
        db.deleteBook(book);
        this.lsBook.remove(book);
        // Refresh ListView.
        this.lsViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == my_request_code ) {
            boolean needRefresh = data.getBooleanExtra("needRefresh",true);
            // Refresh ListView
            if(needRefresh) {
                this.lsBook.clear();
                MyDatabaseHelper db = new MyDatabaseHelper(MainActivity.context);
                List<Book> list=  db.getAllBooks();
                this.lsBook.addAll(list);
                // Thông báo d? li?u thay d?i (Ð? refresh ListView).
                this.lsViewAdapter.notifyDataSetChanged();
            }
        }
    }

}
