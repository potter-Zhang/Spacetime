package edu.whu.spacetime.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lxj.xpopup.XPopup;

import java.util.List;
import java.util.NoSuchElementException;

import edu.whu.spacetime.R;
import edu.whu.spacetime.domain.NoteBook;
import edu.whu.spacetime.widget.NoteBookPopupMenu;

public class NoteBookListAdapter extends ArrayAdapter<NoteBook> {
    private int resourceId;

    private List<NoteBook> notebookList;

    public NoteBookListAdapter(@NonNull Context context, int resource, @NonNull List<NoteBook> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
        this.notebookList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        NoteBook noteBook = getItem(position);
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
        btnMore.setOnClickListener(v -> builder.asCustom(popup).show());
        return view;
    }
}
