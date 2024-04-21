package edu.whu.spacetime.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xuexiang.xui.widget.toast.XToast;

import org.w3c.dom.Text;

import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.domain.ChatMessage;

public class ChatMsgAdapter extends ArrayAdapter<ChatMessage> {
    public ChatMsgAdapter(@NonNull Context context, @NonNull List<ChatMessage> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChatMessage msg = getItem(position);
        View view;
        switch (msg.getRole()) {
            case ChatMessage.AI:
                view = createAIMsgView(msg, parent);
                break;
            case ChatMessage.USER:
                view = createUserMsgView(msg, parent);
                break;
            default:
                view = createAIMsgView(msg, parent);
                break;
        }
        return view;
    }

    private View createAIMsgView(ChatMessage msg, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_ai_msg, parent, false);
        TextView tvMsg = view.findViewById(R.id.tv_ai_msg);
        tvMsg.setText(msg.getContent());

        ImageButton btnCopy = view.findViewById(R.id.btn_copy);
        // 将AI的回答内容复制到剪贴板
        btnCopy.setOnClickListener(v -> {
            ClipboardManager manager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData data = ClipData.newPlainText("AI", msg.getContent());
            manager.setPrimaryClip(data);
            XToast.info(getContext(), "已复制").show();
        });
        return view;
    }

    private View createUserMsgView(ChatMessage msg, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_user_msg, parent, false);
        TextView tvMsg = view.findViewById(R.id.tv_user_msg);
        tvMsg.setText(msg.getContent());
        return view;
    }
}
