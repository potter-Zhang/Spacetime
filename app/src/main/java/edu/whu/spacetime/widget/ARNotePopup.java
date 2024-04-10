package edu.whu.spacetime.widget;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BubbleAttachPopupView;

import edu.whu.spacetime.R;

public class ARNotePopup extends BubbleAttachPopupView {
    public interface OnDeleteListener {
        void onDelete();
    }

    public interface OnEditConfirmListener {
        void onEditConfirm(String newTitle);
    }

    private OnDeleteListener onDeleteListener;

    private OnEditConfirmListener onEditConfirmListener;

    public ARNotePopup(@NonNull Context context) {
        super(context);
        this.setBubbleBgColor(context.getColor(R.color.white));
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.widget_popup_arnote;
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    public void setOnEditConfirmListener(OnEditConfirmListener onEditConfirmListener) {
        this.onEditConfirmListener = onEditConfirmListener;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.btn_arnote_del).setOnClickListener(v -> {
            if (this.onDeleteListener != null) {
                onDeleteListener.onDelete();
            }
        });

        findViewById(R.id.btn_arnote_edit).setOnClickListener(v -> {
            InputDialog inputDialog = new InputDialog(getContext(), "重命名");
            inputDialog.setOnInputConfirmListener(text -> {
                if (this.onEditConfirmListener != null) {
                    this.onEditConfirmListener.onEditConfirm(text);
                    inputDialog.dismiss();
                }
            });
            new XPopup.Builder(getContext())
                    .isDestroyOnDismiss(true)
                    .asCustom(inputDialog)
                    .show();
        });
    }
}
