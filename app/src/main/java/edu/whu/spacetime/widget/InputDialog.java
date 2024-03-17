package edu.whu.spacetime.widget;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.toast.XToast;

import edu.whu.spacetime.R;

public class InputDialog extends BottomPopupView {
    private String title;

    private OnInputConfirmListener confirmListener;

    public InputDialog(@NonNull Context context, boolean createOrRename) {
        super(context);
        this.title = createOrRename ? "新建笔记本" : "重命名";
    }

    public void setOnInputConfirmListener(OnInputConfirmListener listener) {
        this.confirmListener = listener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.widget_popup_input;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        // 设置标题
        TextView tvTitle = findViewById(R.id.tv_input_title);
        tvTitle.setText(title);

        // 添加监听
        MaterialEditText inputText = findViewById(R.id.edit_input);
        findViewById(R.id.btn_confirm_input).setOnClickListener(v -> {
            // 点击确定按钮后触发确认事件，调用监听器的回调方法
            String input = inputText.getText().toString();
            if (input.length() == 0) {
                XToast.error(getContext(), "笔记本名称不能为空！").show();
            } else {
                this.confirmListener.onConfirm(input);
                dismiss();
            }
        });

        findViewById(R.id.btn_cancel_input).setOnClickListener(v -> {
            dismiss();
        });
    }
}
