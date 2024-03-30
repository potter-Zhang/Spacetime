package edu.whu.spacetime.service;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.Constants;

import java.util.Arrays;

import io.reactivex.Flowable;

public class AIFunctionService {
    // 自定义的流式输出监听器，当接收到流的新response时调用回调函数
    public interface OnNewMessageComeListener {
        void OnNewMessageCome(String message);
    }

    private OnNewMessageComeListener listener;

    private static final String APIKEY = "sk-bd7a36da81b74033a786934763bf4856";

    private static final String ABSTRACT_PROMPT = "接下来我会给出我的笔记，你需要提炼、 缩写我的笔记，尽量精简，要比之前的笔记字数少。\n" +
                                                    "要求：1、充分使用多级标题，标题需要使用序号进行编号，标题后跟换行符号" +
                                                    "2、最终不需要回答其他信息，返回缩写后的结果即可。"+
                                                    "给出的笔记是：\n";

    private static final String EXPAND_PROMPT = "接下来我会给出我的笔记，你需要根据关键信息扩写我的笔记，尽量详细。\n" +
                                                "要求：1、充分使用多级标题，标题需要使用序号进行编号，标题后跟换行符。" +
                                                "2、最终不需要回答其他信息，返回缩写后的结果即可。"+
                                                "给出的笔记是：\n";

    private static final String TRANSLATE_PROMPT = "接下来我会给出一段文本，你需要将这段文本翻译为英文。\n" +
                                                    "最终不需要回答其他信息，返回缩写后的结果即可。"+
                                                    "给出的文本是：\n";

    public AIFunctionService() {
        Constants.apiKey = APIKEY;
    }

    public void setOnNewMessageComeListener(OnNewMessageComeListener listener) {
        this.listener = listener;
    }

    // 判断是否联网
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

    // 调用流式输出API，会调用listener的回调函数
    private void startStreamCall(String command) {
        Generation gen = new Generation();
        // 要发送的消息
        Message userMsg = Message
                .builder()
                .role(Role.USER.getValue())
                .content(command)
                .build();
        // 模型信息参数
        GenerationParam param = GenerationParam.builder()
                // .model("qwen-max")
                .model("qwen-turbo")
                .messages(Arrays.asList(userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .topP(0.8).enableSearch(true)  // set streaming output
                .incrementalOutput(true)  // get streaming output incrementally
                .build();
        // 开始调用API，网络请求必须在子线程内进行
        new Thread(() -> {
            Flowable<GenerationResult> result;
            try {
                result = gen.streamCall(param);
            } catch (NoApiKeyException | InputRequiredException e) {
                throw new RuntimeException(e);
            }
            result.blockingForEach(message -> {
                // 调用回调函数
                listener.OnNewMessageCome(message.getOutput().getChoices().get(0).getMessage().getContent());
            });
        }).start();
    }

    // 缩写笔记
    public void abstractNote(Context context, String noteContent) throws NetworkErrorException {
        if (!isNetworkConnected(context)) {
            throw new NetworkErrorException("网络未连接！");
        }
        String command = ABSTRACT_PROMPT + noteContent;
        startStreamCall(command);
    }

    // 扩写笔记
    public void expandNote(Context context, String noteContent) throws NetworkErrorException {
        if (!isNetworkConnected(context)) {
            throw new NetworkErrorException("网络未连接！");
        }
        String command = EXPAND_PROMPT + noteContent;
        startStreamCall(command);
    }
}
