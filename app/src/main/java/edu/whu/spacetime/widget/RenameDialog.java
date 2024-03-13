package edu.whu.spacetime.widget;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.xuexiang.xui.widget.edittext.ClearEditText;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.popupwindow.ViewTooltip;
import com.xuexiang.xui.widget.toast.XToast;

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

        // ClearEditText renameInput = findViewById(R.id.edit_rename_input);
        MaterialEditText renameInput = findViewById(R.id.edit_rename_input);
        findViewById(R.id.btn_confirm_rename).setOnClickListener(v -> {
            // 点击确定按钮后触发确认事件，调用监听器的回调方法
            String inputText = renameInput.getText().toString();
            if (inputText.length() == 0) {
                XToast.error(getContext(), "笔记本名称不能为空！").show();
            } else {
                this.confirmListener.onConfirm(renameInput.getText().toString());
                dismiss();
            }
        });

        findViewById(R.id.btn_cancel_rename).setOnClickListener(v -> {
            dismiss();
        });
    }
}
