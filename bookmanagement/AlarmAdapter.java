package com.example.sev_user.bookmanagement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AlarmAdapter extends ArrayAdapter {
    private ArrayList<Book> items;
    private LayoutInflater inflater;
    Context context;

    public AlarmAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Book> items) {
        super(context, resource, items);
        this.items = items;
        this.context =context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.list_row, null);

        }
        Book info = items.get(position);
        if (info != null) {
            TextView tvTime =(TextView) view.findViewById(R.id.txttime);
            tvTime.setText(info.getAlarm());
            TextView tvTitle = (TextView) view.findViewById(R.id.txtbook);
            tvTitle.setText(info.getName());

        }
        return view;
    }
}
