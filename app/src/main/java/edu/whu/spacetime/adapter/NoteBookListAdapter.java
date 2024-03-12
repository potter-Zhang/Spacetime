package edu.whu.spacetime.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.domain.NoteBook;

public class NoteBookListAdapter extends ArrayAdapter<NoteBook> {
    private int resourceId;

    public NoteBookListAdapter(@NonNull Context context, int resource, @NonNull List<NoteBook> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        NoteBook noteBook = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(this.resourceId, parent, false);

        TextView tv_notebookName = view.findViewById(R.id.tv_notebookName);
        tv_notebookName.setText(noteBook.getName());
        return view;
    }
}
