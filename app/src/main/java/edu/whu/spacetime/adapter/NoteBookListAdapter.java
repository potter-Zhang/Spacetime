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
import edu.whu.spacetime.domain.Notebook;
import edu.whu.spacetime.widget.NoteBookPopupMenu;

public class NoteBookListAdapter extends ArrayAdapter<Notebook> {
    private int resourceId;

    private List<Notebook> notebookList;

    public NoteBookListAdapter(@NonNull Context context, int resource, @NonNull List<Notebook> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
        this.notebookList = objects;
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
        popup.setDeleteListener(() -> {
            notebookList.remove(noteBook);
            notifyDataSetChanged();
        });
        popup.setRenameListener(t -> {
            noteBook.setName(t);
            notifyDataSetChanged();
        });
        btnMore.setOnClickListener(v -> builder.asCustom(popup).show());
        return view;
    }
}
