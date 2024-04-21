package edu.whu.spacetime.widget;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.BottomPopupView;

import edu.whu.spacetime.R;

public class AIResultDialog extends BottomPopupView {
    /**
     * “替换”按钮点击监听器
     */
    public interface OnReplaceListener {
        void onReplaceBtnClicked(String content);
    }

    /**
     * "插入"按钮点击监听器
     */
    public interface OnInsertListener {
        void onInsertBtnClicked(String content);
    }

    private OnReplaceListener replaceListener;

    private OnInsertListener insertListener;

    private TextView tv_ai_result;

    private StringBuilder builder;

    public void setReplaceListener(OnReplaceListener replaceListener) {
        this.replaceListener = replaceListener;
    }

    public void setInsertListener(OnInsertListener insertListener) {
        this.insertListener = insertListener;
    }

    public void appendText(String msg) {
        builder.append(msg);
        getActivity().runOnUiThread(() -> {
            tv_ai_result.setText(builder.toString());
            // 自动滚动到底部
            int offset=tv_ai_result.getLineCount() * tv_ai_result.getLineHeight();
            if (offset > tv_ai_result.getHeight()) {
                tv_ai_result.scrollTo(0, offset - tv_ai_result.getHeight());
            }
        });
    }

    public AIResultDialog(@NonNull Context context) {
        super(context);
        builder = new StringBuilder();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.widget_ai_result;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        tv_ai_result = findViewById(R.id.tv_ai_result);
        // 设置垂直滑动
        tv_ai_result.setMovementMethod(ScrollingMovementMethod.getInstance());

        findViewById(R.id.tv_ai_cancel).setOnClickListener(v -> dismiss());

        findViewById(R.id.tv_ai_replace).setOnClickListener(v -> {
            if (this.replaceListener != null) {
                replaceListener.onReplaceBtnClicked(builder.toString().replaceAll("\n", ""));
            }
        });

        findViewById(R.id.tv_ai_insert).setOnClickListener(v -> {
            if (this.insertListener != null) {
                insertListener.onInsertBtnClicked(builder.toString().replaceAll("\n", ""));
            }
        });
    }
}
