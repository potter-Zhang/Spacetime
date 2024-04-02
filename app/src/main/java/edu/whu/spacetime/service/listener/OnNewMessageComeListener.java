package edu.whu.spacetime.service.listener;

/**
 * 流式输出监听器，每次接收到新的response时调用回调函数
 */
public interface OnNewMessageComeListener {
    void OnNewMessageCome(String message);
}
