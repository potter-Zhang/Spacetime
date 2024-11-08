package edu.whu.spacetime.widget;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.AttachPopupView;

import edu.whu.spacetime.R;

// 在NoteBookListAdapter中使用
public class NoteBookPopupMenu extends AttachPopupView  {

    // 自定义删除事件
    public interface OnDeleteListener {
        void OnDelete();
    }

    // 自定义重命名事件
    public interface OnRenameListener{
        void OnRename(String text);
    }

    private OnDeleteListener deleteListener;
    private OnRenameListener renameListener;


    public NoteBookPopupMenu(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.widget_popup_menu;
    }

    public void setDeleteListener(OnDeleteListener listener) {
        this.deleteListener = listener;
    }

    public void setRenameListener(OnRenameListener listener) {
        this.renameListener = listener;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        // 重命名
        findViewById(R.id.tv_rename).setOnClickListener(v -> {
            openRenameDialog();
        });

        // 删除
        findViewById(R.id.tv_delete).setOnClickListener(v -> {
            openDeleteDialog();
        });
    }

    private void openRenameDialog() {
        InputDialog inputDialog = new InputDialog(getContext(), "重命名");
        inputDialog.setOnInputConfirmListener(t -> {
            // 确认后触发重命名事件
            if (this.renameListener != null)
                renameListener.OnRename(t);
        });
        new XPopup.Builder(getContext())
                .isDestroyOnDismiss(true)
                .asCustom(inputDialog)
                .show();
        dismiss();
    }

    private void openDeleteDialog() {
        ConfirmDialog deleteDialog = new ConfirmDialog(getContext(), Color.RED, "确定要删除吗？", "删除");
        deleteDialog.setOnConfirmListener(() -> {
            // 确认后触发OnDelete事件
            if (deleteListener != null)
                deleteListener.OnDelete();
        });
        new XPopup.Builder(getContext())
                .isDestroyOnDismiss(true)
                .asCustom(deleteDialog)
                .show();
        dismiss();
    }
}
