package edu.whu.spacetime.widget;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.AttachPopupView;

import edu.whu.spacetime.R;
import edu.whu.spacetime.adapter.NoteBookListAdapter;
import edu.whu.spacetime.domain.NoteBook;

public class NoteBookPopupMenu extends AttachPopupView  {
    private NoteBookListAdapter adapter;
    private NoteBook noteBook;

    public NoteBookPopupMenu(@NonNull Context context, NoteBookListAdapter adapter, NoteBook notebook) {
        super(context);
        this.adapter = adapter;
        this.noteBook = notebook;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.widget_popup_menu;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.tv_rename).setOnClickListener(v -> {
            dismiss();
        });
        findViewById(R.id.tv_delete).setOnClickListener(v -> {
            adapter.remove(noteBook);
            dismiss();
        });
    }
}
