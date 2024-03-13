package edu.whu.spacetime.widget;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.xuexiang.xui.widget.edittext.ClearEditText;

import org.w3c.dom.Text;

import edu.whu.spacetime.R;

public class RenameDialog extends BottomPopupView {

    private OnInputConfirmListener confirmListener;

    public RenameDialog(@NonNull Context context) {
        super(context);
    }

    public void setOnInputConfirmListener(OnInputConfirmListener listener) {
        this.confirmListener = listener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.widget_popup_rename;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        ClearEditText renameInput = findViewById(R.id.edit_rename_input);
        findViewById(R.id.btn_confirm_rename).setOnClickListener(v -> {
            // 点击确定按钮后触发确认事件，调用监听器的回调方法
            this.confirmListener.onConfirm(renameInput.getText().toString());
            dismiss();
        });

        findViewById(R.id.btn_cancel_rename).setOnClickListener(v -> {
            dismiss();
        });
    }
}
