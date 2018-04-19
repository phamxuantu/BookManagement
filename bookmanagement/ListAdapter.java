package com.example.sev_user.bookmanagement;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
public class ListAdapter extends ArrayAdapter {
    private ArrayList<Book> items;
    private LayoutInflater inflater;
    Context context;

    public ListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Book> items) {
        super(context, resource, items);
        this.items = items;
        this.context =context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.content_main, null);

        }
        Book info = items.get(position);
        if (info != null) {
            ImageView ivImage = (ImageView) view.findViewById(R.id.image);
            Uri uriImage = Uri.fromFile(new File(info.getImage()));
            ivImage.setImageURI(uriImage);
            TextView tvTitle = (TextView) view.findViewById(R.id.name);
            tvTitle.setText(info.getName());
            TextView tvMessage = (TextView) view.findViewById(R.id.author);
            tvMessage.setText(info.getAuthor());
        }
        return view;
    }
}
