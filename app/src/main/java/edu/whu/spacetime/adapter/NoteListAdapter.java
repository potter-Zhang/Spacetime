package edu.whu.spacetime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import edu.whu.spacetime.R;
import edu.whu.spacetime.SpacetimeApplication;
import edu.whu.spacetime.dao.NoteDao;
import edu.whu.spacetime.domain.Note;
import edu.whu.spacetime.fragment.NoteBrowserFragment;

public class NoteListAdapter extends ArrayAdapter<Note> {
    private class ViewHolder {
        private TextView tvTitle;
        private TextView tvAbstract;
        private TextView tvTime;
        private CheckBox checkNote;
    }
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
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder.tvTitle = convertView.findViewById(R.id.tv_note_title);
            viewHolder.tvAbstract = convertView.findViewById(R.id.tv_note_abstract);
            viewHolder.tvTime = convertView.findViewById(R.id.tv_note_time);
            viewHolder.checkNote = convertView.findViewById(R.id.check_note);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Note note = getItem(position);
        viewHolder.tvTitle.setText(note.getTitle());
        // 摘要最长为50，否则setText会导致卡顿
        int maxLength = Math.min(note.getPlainText().length(), 50);
        viewHolder.tvAbstract.setText(note.getPlainText().substring(0, maxLength));
        // 设置显示时间，超过一年时才显示年份
        LocalDateTime createTime = note.getCreateTime();
        int year = createTime.getYear();
        StringBuilder displayTime = new StringBuilder();
        if (LocalDateTime.now().getYear() - year >= 1) {
            displayTime.append(year).append("年");
        }
        displayTime.append(createTime.getMonthValue()).append("月").append(createTime.getDayOfMonth()).append("日");
        viewHolder.tvTime.setText(displayTime.toString());
        if (isAtEditMode()) {
            viewHolder.checkNote.setVisibility(View.VISIBLE);
        } else {
            viewHolder.checkNote.setVisibility(View.GONE);
            viewHolder.checkNote.setChecked(false);
        }
        viewHolder.checkNote.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                this.addCheckedNote(note);
            } else {
                this.removeCheckedNote(note);
            }
        });
        return convertView;
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
        notifyDataSetChanged();
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
