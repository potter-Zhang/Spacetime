package edu.whu.spacetime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import edu.whu.spacetime.R;
import edu.whu.spacetime.SpacetimeApplication;
import edu.whu.spacetime.dao.NoteDao;
import edu.whu.spacetime.domain.Note;
import edu.whu.spacetime.fragment.NoteBrowserFragment;

public class NoteListAdapter extends ArrayAdapter<Note> {
    private int resourceId;

    private boolean atEditMode = false;

    /**
     * checkbox被选中的笔记
     */
    private List<Note> checkedNote;

    public NoteListAdapter(@NonNull Context context, int resource, @NonNull List<Note> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
        checkedNote = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Note note = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView tvTitle = view.findViewById(R.id.tv_note_title);
        TextView tvAbstract = view.findViewById(R.id.tv_note_abstract);
        TextView tvTime = view.findViewById(R.id.tv_note_time);
        CheckBox checkNote = view.findViewById(R.id.check_note);

        tvTitle.setText(note.getTitle());
        tvAbstract.setText(note.getPlainText());
        tvTime.setText(note.getCreateTime().toLocalDate().toString());
        if (isAtEditMode()) {
            checkNote.setVisibility(View.VISIBLE);
        }
        checkNote.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                this.addCheckedNote(note);
            } else {
                this.remove(note);
            }
        });
        return view;
    }

    public boolean isAtEditMode() {
        return atEditMode;
    }

    public void editMode() {
        atEditMode = true;
    }

    public void exitEditMode() {
        atEditMode = false;
        checkedNote.clear();
    }

    public void addCheckedNote(Note note) {
        checkedNote.add(note);
    }

    public void removeCheckedNote(Note note) {
        checkedNote.remove(note);
    }

    /**
     * 将checkedNote列表中的笔记从视图上删除，同时从数据库中删除
     */
    public void removeCheckedNoteInView() {
        NoteDao noteDao = SpacetimeApplication.getInstance().getDatabase().getNoteDao();
        for (Note note : checkedNote) {
            this.remove(note);
            noteDao.deleteNote(note);
        }
        exitEditMode();
        notifyDataSetChanged();
    }
}
