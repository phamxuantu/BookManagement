package com.example.sev_user.bookmanagement;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

public class FragmentAlarm extends Fragment{

    public static ListView listView;

    public static ArrayList<Book> lsBook = new ArrayList<Book>();
    public static AlarmAdapter lsViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.listview, container, false);
        listView = (ListView) v.findViewById(R.id.ListView01);
        MyDatabaseHelper db = new MyDatabaseHelper(MainActivity.context);
        List<Book> ls = db.getAllBooks();
        lsBook.addAll(ls);
        lsViewAdapter = new AlarmAdapter(MainActivity.context, R.layout.list_row, lsBook);
        this.listView.setAdapter(this.lsViewAdapter);
        registerForContextMenu(this.listView);
        return v;
    }



}
