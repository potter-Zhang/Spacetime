package edu.whu.spacetime.adapter;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.units.qual.N;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.SpacetimeApplication;
import edu.whu.spacetime.adapter.listener.OnRecyclerItemClickListener;
import edu.whu.spacetime.adapter.listener.OnRecyclerItemLongClickListener;
import edu.whu.spacetime.dao.NoteDao;
import edu.whu.spacetime.domain.Note;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvAbstract;
        private TextView tvTime;
        private CheckBox checkNote;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_note_title);
            tvAbstract = itemView.findViewById(R.id.tv_note_abstract);
            tvTime = itemView.findViewById(R.id.tv_note_time);
            checkNote = itemView.findViewById(R.id.check_note);
        }
    }

    private Context context;

    private List<Note> noteList;

    /**
     * checkbox被选中的笔记
     */
    private List<Note> checkedNote;

    private boolean editMode = false;

    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    private OnRecyclerItemLongClickListener onRecyclerItemLongClickListener;

    public NoteRecyclerAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
        this.checkedNote = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = noteList.get(position);
        ((ViewGroup) holder.itemView.findViewById(R.id.layout_note_item_body))
                .getLayoutTransition()
                .enableTransitionType(LayoutTransition.CHANGING);
        holder.tvTitle.setText(note.getTitle());
        // 摘要最长为50，否则setText会导致卡顿
        int maxLength = Math.min(note.getPlainText().length(), 50);
        holder.tvAbstract.setText(note.getPlainText().substring(0, maxLength));
        // 设置显示时间，超过一年时才显示年份
        LocalDateTime createTime = note.getCreateTime();
        int year = createTime.getYear();
        StringBuilder displayTime = new StringBuilder();
        if (LocalDateTime.now().getYear() - year >= 1) {
            displayTime.append(year).append("年");
        }
        displayTime.append(createTime.getMonthValue()).append("月").append(createTime.getDayOfMonth()).append("日");
        holder.tvTime.setText(displayTime.toString());
        // 编辑模式显示checkbox
        if (isAtEditMode()) {
            holder.checkNote.setVisibility(View.VISIBLE);
        } else {
            holder.checkNote.setVisibility(View.GONE);
            holder.checkNote.setChecked(false);
        }
        // checkbox状态改变时将note从checkNote添加或移除
        holder.checkNote.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                this.addCheckedNote(note);
            } else {
                this.removeCheckedNote(note);
            }
        });
        // 点击和长按事件
        holder.itemView.setOnClickListener(v -> {
            if (this.onRecyclerItemClickListener != null) {
                onRecyclerItemClickListener.OnItemClick(holder.itemView, note);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (this.onRecyclerItemLongClickListener != null) {
                onRecyclerItemLongClickListener.onItemLongClick(holder.itemView, note);
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    public void setOnRecyclerItemLongClickListener(OnRecyclerItemLongClickListener onRecyclerItemLongClickListener) {
        this.onRecyclerItemLongClickListener = onRecyclerItemLongClickListener;
    }

    public void editMode() {
        this.editMode = true;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void exitEditMode() {
        this.editMode = false;
        this.checkedNote.clear();
        notifyDataSetChanged();
    }

    public boolean isAtEditMode() {
        return editMode;
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
            int position = noteList.indexOf(note);
            noteList.remove(note);
            notifyItemRemoved(position);
            noteDao.deleteNote(note);
        }
        exitEditMode();
    }

    public void clear() {
        this.noteList.clear();
    }

    public void addAll(List<Note> noteList) {
        this.noteList.addAll(noteList);
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }
}
