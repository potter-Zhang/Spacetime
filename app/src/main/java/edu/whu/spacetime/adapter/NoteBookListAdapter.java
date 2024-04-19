package edu.whu.spacetime.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lxj.xpopup.XPopup;

import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.SpacetimeApplication;
import edu.whu.spacetime.dao.NoteDao;
import edu.whu.spacetime.dao.NotebookDao;
import edu.whu.spacetime.domain.Notebook;
import edu.whu.spacetime.widget.NoteBookPopupMenu;

public class NoteBookListAdapter extends ArrayAdapter<Notebook> {
    public interface OnNotebookDeleteListener {
        void onNotebookDelete(Notebook notebook);
    }
    private int resourceId;

    private List<Notebook> notebookList;

    private NotebookDao notebookDao;

    private NoteDao noteDao;

    private OnNotebookDeleteListener onNotebookDeleteListener;

    public NoteBookListAdapter(@NonNull Context context, int resource, @NonNull List<Notebook> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
        this.notebookList = objects;
        this.notebookDao = SpacetimeApplication.getInstance().getDatabase().getNotebookDao();
        this.noteDao = SpacetimeApplication.getInstance().getDatabase().getNoteDao();
    }

    public void setOnNotebookDeleteListener(OnNotebookDeleteListener onNotebookDeleteListener) {
        this.onNotebookDeleteListener = onNotebookDeleteListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Notebook noteBook = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(this.resourceId, parent, false);

        TextView tv_notebookName = view.findViewById(R.id.tv_notebookName);
        tv_notebookName.setText(noteBook.getName());

        // 弹出菜单
        View btnMore = view.findViewById(R.id.btn_more);
        final XPopup.Builder builder = new XPopup.Builder(getContext()).watchView(btnMore);
        NoteBookPopupMenu popup = new NoteBookPopupMenu(getContext());

        // 默认笔记本不允许重命名和删除
        // FIXME: 用户创建了名为"默认笔记本"的笔记本时有bug
        if (noteBook.getName().equals("默认笔记本")) {
            btnMore.setVisibility(View.INVISIBLE);
            return view;
        }

        // 设置弹出菜单按钮的点击事件
        popup.setDeleteListener(() -> {
            notebookList.remove(noteBook);
            noteDao.deleteNotesInNotebook(noteBook.getNotebookId());
            notebookDao.deleteNotebook(noteBook);
            notifyDataSetChanged();

            if (this.onNotebookDeleteListener != null) {
                onNotebookDeleteListener.onNotebookDelete(noteBook);
            }
        });
        popup.setRenameListener(t -> {
            noteBook.setName(t);
            notebookDao.updateNotebook(noteBook);
            notifyDataSetChanged();
        });
        btnMore.setOnClickListener(v -> builder.asCustom(popup).show());
        return view;
    }
}
