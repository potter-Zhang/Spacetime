package edu.whu.spacetime.widget;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.AttachPopupView;

import edu.whu.spacetime.R;

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
            RenameDialog renameDialog = new RenameDialog(getContext());
            renameDialog.setOnInputConfirmListener(t -> {
                // 确认后触发重命名事件
                if (this.renameListener != null)
                    renameListener.OnRename(t);
            });
            new XPopup.Builder(getContext())
                    .isDestroyOnDismiss(true)
                    .asCustom(renameDialog)
                    .show();
            dismiss();
        });

        // 删除
        findViewById(R.id.tv_delete).setOnClickListener(v -> {
            ConfirmDeleteDialog deleteDialog = new ConfirmDeleteDialog(getContext());
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
        });
    }
}
