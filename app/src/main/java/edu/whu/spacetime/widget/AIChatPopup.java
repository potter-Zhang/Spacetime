package edu.whu.spacetime.widget;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.core.BubbleAttachPopupView;
import com.xuexiang.xui.widget.toast.XToast;

import java.util.ArrayList;
import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.adapter.ChatMsgAdapter;
import edu.whu.spacetime.domain.ChatMessage;
import edu.whu.spacetime.service.AIChatService;

public class AIChatPopup extends BottomPopupView {
    private ChatMsgAdapter adapter;

    private ListView msgListView;

    private AIChatService chatService;

    /**
     * 用户是否已经联网
     */
    private boolean online;

    private String noteContent;

    /**
     * 创建用于AI问答的弹出对话框
     * @param context 上下文
     * @param noteContent 传给AI的笔记内容
     */
    public AIChatPopup(@NonNull Context context, String noteContent) {
        super(context);
        chatService = new AIChatService();
        this.noteContent = noteContent;
        try {
            chatService.initChat(context, noteContent);
            online = true;
        } catch (NetworkErrorException e) {
            online = false;
            XToast.error(context, "网络未连接！").show();
        }
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.widget_ai_chat;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        setMsgList();
        EditText editSend = findViewById(R.id.edit_send_msg);
        findViewById(R.id.btn_send_msg).setOnClickListener(v -> {
            // 未联网时重试
            if (!online) {
                try {
                    chatService.initChat(getContext(), noteContent);
                    online = true;
                } catch (NetworkErrorException e) {
                    XToast.error(getContext(), "网络未连接！").show();
                    return;
                }
            }
            String msg = editSend.getText().toString();
            // 添加用户消息
            ChatMessage userMsg = new ChatMessage(ChatMessage.USER, msg);
            adapter.add(userMsg);
            editSend.setText("");
            msgListView.setSelection(msgListView.getBottom());

            // 调用API获取回答
            chatService.answer(msg);
            ChatMessage aiMsg = new ChatMessage(ChatMessage.AI, "");
            adapter.add(aiMsg);
            chatService.setListener(message -> {
                // 只有UI线程能操作UI线程上创建的对象
                getActivity().runOnUiThread(() -> {
                    aiMsg.setContent(aiMsg.getContent() + message);
                    adapter.notifyDataSetChanged();
                    msgListView.setSelection(msgListView.getBottom());
                });
            });
        });
    }

    private void setMsgList() {
        msgListView = findViewById(R.id.list_chat_msg);
        List<ChatMessage> msgs = new ArrayList<>();
        msgs.add(new ChatMessage(ChatMessage.AI, "您好，请问有什么可以帮助您的吗"));
        msgs.add(new ChatMessage(ChatMessage.USER, "你好"));
        adapter = new ChatMsgAdapter(getContext(), msgs);
        msgListView.setAdapter(adapter);
    }
}
