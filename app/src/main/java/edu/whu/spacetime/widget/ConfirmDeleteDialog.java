package edu.whu.spacetime.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import edu.whu.spacetime.R;

public class ConfirmDeleteDialog extends BottomPopupView {
    private OnConfirmListener listener;

    public ConfirmDeleteDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.widget_confirm_delete;
    }

    public  void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.btn_confirm_del).setOnClickListener(v -> {
            if (this.listener != null) {
                this.listener.onConfirm();
            }
            dismiss();
        });
        findViewById(R.id.btn_cancel_del).setOnClickListener(v -> {
            dismiss();
        });
    }
}
