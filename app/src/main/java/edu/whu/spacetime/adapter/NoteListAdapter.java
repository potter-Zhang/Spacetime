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
import edu.whu.spacetime.domain.Note;
import edu.whu.spacetime.fragment.NoteBrowserFragment;

public class NoteListAdapter extends ArrayAdapter<Note> {
    private int resourceId;

    public NoteListAdapter(@NonNull Context context, int resource, @NonNull List<Note> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Note note = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView tvTitle = view.findViewById(R.id.tv_note_title);
        TextView tvAbstract = view.findViewById(R.id.tv_note_abstract);
        TextView tvTime = view.findViewById(R.id.tv_note_time);

        tvTitle.setText(note.getTitle());
        tvAbstract.setText(note.getContent());
        tvTime.setText(note.getCreateTime().toLocalDate().toString());
        return view;
    }
}
