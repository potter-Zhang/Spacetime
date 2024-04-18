package edu.whu.spacetime.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import edu.whu.spacetime.R;

public class ConfirmDialog extends CenterPopupView {
    private OnConfirmListener confirmListener;

    private OnCancelListener cancelListener;

    private int color;

    private String title;

    private String btnText;

    /**
     *
     * @param context 上下文
     * @param color 按钮颜色
     * @param title 对话框标题
     * @param btnText 确认按钮的文本
     */
    public ConfirmDialog(@NonNull Context context, int color, String title, String btnText) {
        super(context);
        this.color = color;
        this.title = title;
        this.btnText = btnText;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.widget_confirm;
    }

    public  void setOnConfirmListener(OnConfirmListener listener) {
        this.confirmListener = listener;
    }

    public void setCancelListener(OnCancelListener cancelListener) {
        this.cancelListener = cancelListener;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        TextView tvTitle = findViewById(R.id.tv_confirm_title);
        tvTitle.setText(this.title);
        TextView btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setText(this.btnText);
        btn_confirm.setBackgroundTintList(ColorStateList.valueOf(this.color));
        btn_confirm.setTextColor(this.color);
        btn_confirm.setOnClickListener(v -> {
            if (this.confirmListener != null) {
                this.confirmListener.onConfirm();
            }
            dismiss();
        });
        findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            if (this.cancelListener != null) {
                cancelListener.onCancel();
            }
            dismiss();
        });
    }
}
