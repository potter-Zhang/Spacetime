package edu.whu.spacetime.widget;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.AttachPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import edu.whu.spacetime.R;
import edu.whu.spacetime.adapter.NoteBookListAdapter;
import edu.whu.spacetime.domain.NoteBook;

public class NoteBookPopupMenu extends AttachPopupView  {

    public interface OnDeleteListener {
        void OnDelete();
    }

    private OnDeleteListener deleteListener;

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


    @Override
    protected void onCreate() {
        super.onCreate();
        // 重命名
        findViewById(R.id.tv_rename).setOnClickListener(v -> {
            dismiss();
        });
        // 删除
        findViewById(R.id.tv_delete).setOnClickListener(v -> {
            ConfirmDeleteDialog dialog = new ConfirmDeleteDialog(getContext());
            dialog.setOnConfirmListener(() -> {
                // 触发OnDelete事件
                if (deleteListener != null)
                    deleteListener.OnDelete();
            });
            new XPopup.Builder(getContext())
                    .isDestroyOnDismiss(true)
                    .asCustom(dialog)
                    .show();
            dismiss();
        });
    }
}
