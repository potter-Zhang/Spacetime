package edu.whu.spacetime.widget;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.BottomPopupView;

import edu.whu.spacetime.R;

public class AIResultDialog extends BottomPopupView {
    private TextView tv_ai_result;

    private StringBuilder builder;

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

        });

        findViewById(R.id.tv_ai_insert).setOnClickListener(v -> {

        });
    }
}
