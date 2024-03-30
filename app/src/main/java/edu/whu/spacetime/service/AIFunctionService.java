package edu.whu.spacetime.service;

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

    private static final String APIKEY = "b587806232773fb8b507e8b36dc3abe6a13ef7d5";

    private static final String ABSTRACT_PROMPT = "接下来我会给出我的笔记，你需要提炼、 缩写我的笔记，尽量精简，要比之前的笔记字数少。\n" +
                                                    "要求：1、充分使用多级标题；" +
                                                    "2、最终不需要回答其他信息，返回缩写后的结果即可。"+
                                                    "给出的笔记是：\n";

    private static final String EXPAND_PROMPT = "接下来我会给出我的笔记，你需要根据关键信息扩写我的笔记，尽量详细。\n" +
                                                "要求：1、充分使用多级标题；" +
                                                "2、最终不需要回答其他信息，返回缩写后的结果即可。"+
                                                "给出的笔记是：\n";

    public AIFunctionService() {
        Constants.apiKey = APIKEY;
    }

    public void setOnNewMessageComeListener(OnNewMessageComeListener listener) {
        this.listener = listener;
    }

    private Flowable<GenerationResult> getStreamResult(String command) throws NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        // 要发送的消息
        Message userMsg = Message
                .builder()
                .role(Role.USER.getValue())
                .content(command)
                .build();
        // 模型信息参数
        GenerationParam param = GenerationParam.builder()
                .model("qwen-max")
                .messages(Arrays.asList(userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .topP(0.8).enableSearch(true)  // set streaming output
                .incrementalOutput(true)  // get streaming output incrementally
                .build();
        return gen.streamCall(param);
    }

    // 缩写笔记
    public void abstractNote(String noteContent) throws NoApiKeyException, InputRequiredException {
        String command = ABSTRACT_PROMPT + noteContent;
        Flowable<GenerationResult> result = getStreamResult(command);
        StringBuilder fullContent = new StringBuilder();
        result.blockingForEach(message -> {
            fullContent.append(message.getOutput().getChoices().get(0).getMessage().getContent());
            listener.OnNewMessageCome(message.getOutput().getChoices().get(0).getMessage().getContent());
        });
    }

    // 扩写笔记
    public void expandNote(String noteContent) throws NoApiKeyException, InputRequiredException {
        String command = EXPAND_PROMPT + noteContent;
        Flowable<GenerationResult> result = getStreamResult(command);
        StringBuilder fullContent = new StringBuilder();
        result.blockingForEach(message -> {
            fullContent.append(message.getOutput().getChoices().get(0).getMessage().getContent());
            listener.OnNewMessageCome(message.getOutput().getChoices().get(0).getMessage().getContent());
        });
    }
}
