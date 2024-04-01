package edu.whu.spacetime.service;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.MessageManager;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.Constants;

import edu.whu.spacetime.service.listener.OnNewMessageComeListener;
import io.reactivex.Flowable;

public class AIChatService {
    private static final String APIKEY = "sk-bd7a36da81b74033a786934763bf4856";

    private Generation gen;

    private MessageManager msgManager;

    private GenerationParam param;

    private OnNewMessageComeListener listener;

    private static final String PROMPT = "接下来我会给出一篇笔记，你需要根据这篇笔记的内容回答我接下来的多个问题。" +
            "要求:你只能根据我提供的笔记回答问题，不能根据其他知识来源回答。若笔记中没有与我提的问题相关的内容，请回答\"笔记中没有提到\"。" +
            "这篇笔记的具体内容如下:";

    public AIChatService() {
        Constants.apiKey = APIKEY;
        gen = new Generation();
        msgManager = new MessageManager(1000000);
    }

    public void setListener(OnNewMessageComeListener listener) {
        this.listener = listener;
    }

    /**
     * 判断设备是否联网
     * @param context 上下文
     */
    private boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 先对AI对话进行初始化，让AI获得笔记的内容
     * @param context 上下文，用于判断设备是否联网
     * @param noteContent 笔记的内容
     */
    public void initChat(Context context, String noteContent) throws InputRequiredException, NetworkErrorException {
        if (!isNetworkConnected(context)) {
            throw new NetworkErrorException();
        }
        Message systemMsg = Message.builder().role(Role.SYSTEM.getValue()).content("You are a helpful assistant.").build();
        String content = PROMPT + noteContent;
        Message userMsg = Message.builder().role(Role.USER.getValue()).content(content).build();
        msgManager.add(systemMsg);
        msgManager.add(userMsg);
        param = GenerationParam.builder()
                .model(Generation.Models.QWEN_TURBO)
                .messages(msgManager.get())
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .topP(0.8)
                .enableSearch(false)
                .incrementalOutput(true)
                .build();
        Flowable<GenerationResult> result;
        try {
            result = gen.streamCall(param);
        } catch (NoApiKeyException e) {
            // NoApiKeyException不用抛出
            return;
        }
        StringBuilder fullContent = new StringBuilder();
        result.blockingForEach(message -> {
            fullContent.append(message.getOutput().getChoices().get(0).getMessage().getContent());
        });
        Message assistantMsg = Message.builder().role(Role.ASSISTANT.getValue()).content(fullContent.toString()).build();
        msgManager.add(assistantMsg);
    }

    /**
     * 根据笔记内容回答用户问题
     * @param question 问题
     * @throws InputRequiredException 输入为空时抛出该错误
     */
    public void answer(String question) throws InputRequiredException {
        Message usrMessage = Message.builder().role(Role.USER.getValue()).content(question).build();
        msgManager.add(usrMessage);
        Flowable<GenerationResult> result;
        try {
            result = gen.streamCall(param);
        } catch (NoApiKeyException e) {
            return;
        }
        StringBuilder fullContent = new StringBuilder();

        result.blockingForEach(message -> {
            if (this.listener != null) {
                listener.OnNewMessageCome(message.getOutput().getChoices().get(0).getMessage().getContent());
            }
            fullContent.append(message.getOutput().getChoices().get(0).getMessage().getContent());
        });
        Message assistantMsg = Message.builder().role(Role.ASSISTANT.getValue()).content(fullContent.toString()).build();
        msgManager.add(assistantMsg);
    }
}
