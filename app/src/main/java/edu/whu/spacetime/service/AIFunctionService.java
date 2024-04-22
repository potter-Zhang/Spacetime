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
import edu.whu.spacetime.service.listener.OnNewMessageComeListener;

public class AIFunctionService {
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

    private static final String TRANSLATE_PROMPT = "接下来我会给出一段文本，你需要将这段文本从中文翻译为英文或者从英文翻译为中文。\n" +
                                                    "最终不需要回答其他信息，只返回翻译后的结果即可。"+
                                                    "给出的文本是：\n";

    private static final String SEGMENT_PROMPT = "接下来我会给出一篇笔记，请将其根据内容使用各级标题进行合理的分段.\n" +
                                                "要求：1、充分使用多级标题，标题需要使用序号进行编号，标题后跟换行符。" +
                                                "2、最终不需要回答其他信息，返回分段后的结果即可。"+
                                                "给出的笔记是：\n";


    public AIFunctionService() {
        Constants.apiKey = APIKEY;
    }

    public void setOnNewMessageComeListener(OnNewMessageComeListener listener) {
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
     * 调用流式输出API，每接收一个response时会调用listener的回调
     * @param command 给ai发送的指令
     */
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
                .model("qwen-max")
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

    /**
     * 对笔记进行缩写
     * @param context 上下文，用于判断是否连接网络
     * @param noteContent 笔记内容
     * @throws NetworkErrorException 网络未连接时抛出异常
     */
    public void abstractNote(Context context, String noteContent) throws NetworkErrorException {
        if (!isNetworkConnected(context)) {
            throw new NetworkErrorException("网络未连接！");
        }
        String command = ABSTRACT_PROMPT + noteContent;
        startStreamCall(command);
    }

    /**
     * 对笔记进行扩写
     * @param context 上下文，用于判断是否连接网络
     * @param noteContent 笔记内容
     * @throws NetworkErrorException 网络未连接时抛出异常
     */
    public void expandNote(Context context, String noteContent) throws NetworkErrorException {
        if (!isNetworkConnected(context)) {
            throw new NetworkErrorException("网络未连接！");
        }
        String command = EXPAND_PROMPT + noteContent;
        startStreamCall(command);
    }

    /**
     * 翻译文本内容
     * @param context 上下文，用于判断是否连接网络
     * @param content 要翻译的内容
     * @throws NetworkErrorException 网络未连接时抛出异常
     */
    public void translate(Context context, String content) throws NetworkErrorException {
        if (!isNetworkConnected(context)) {
            throw new NetworkErrorException("网络未连接！");
        }
        String command = TRANSLATE_PROMPT + content;
        startStreamCall(command);
    }

    /**
     * 将笔记分段
     * @param context 上下文，用于判断是否连接网络
     * @param content 要分段的内容
     * @throws NetworkErrorException 网络未连接时抛出异常
     */
    public void segment(Context context, String content) throws NetworkErrorException {
        if (!isNetworkConnected(context)) {
            throw new NetworkErrorException("网络未连接！");
        }
        String command = SEGMENT_PROMPT + content;
        startStreamCall(command);
    }
}
