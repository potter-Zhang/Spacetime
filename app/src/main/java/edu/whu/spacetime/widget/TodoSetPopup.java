package edu.whu.spacetime.widget;

import android.content.Context;
import android.icu.util.Calendar;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopupext.listener.TimePickerListener;
import com.lxj.xpopupext.popup.TimePickerPopup;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import edu.whu.spacetime.R;
import edu.whu.spacetime.domain.Todo;
import edu.whu.spacetime.fragment.TodoBrowserFragment;

public class TodoSetPopup extends CenterPopupView {
    private TodoBrowserFragment todoBrowserFragment;
    private boolean type;
    private Todo todo = null;
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd   HH:mm");
    //注意：自定义弹窗本质是一个自定义View，但是只需重写一个参数的构造，其他的不要重写，所有的自定义弹窗都是这样。
    public TodoSetPopup(@NonNull Context context, TodoBrowserFragment todoBrowserFragment , boolean type) {
        super(context);
        this.todoBrowserFragment = todoBrowserFragment;
        this.type = type;
    }
    public TodoSetPopup(@NonNull Context context, TodoBrowserFragment todoBrowserFragment , boolean type,Todo todo) {
        super(context);
        this.todoBrowserFragment = todoBrowserFragment;
        this.type = type;
        this.todo = todo;
    }
    // 返回自定义弹窗的布局
    @Override
    protected int getImplLayoutId() {
        return R.layout.widget_todo_set;
    }
    // 执行初始化操作，比如：findView，设置点击，或者任何你弹窗内的业务逻辑
    @Override
    protected void onCreate() {
        super.onCreate();
        TextView tv_time = findViewById(R.id.tv_todo_set_time);
        if(todo == null){
            tv_time.setText(LocalDateTime.now().format(df));
        }
        else{
            EditText edt_title = findViewById(R.id.edit_todo_title);
            EditText edt_addr = findViewById(R.id.edit_todo_addr);
            edt_title.setText(todo.getTitle());
            edt_addr.setText(todo.getAddr());
            tv_time.setText(todo.getCreateTime().format(df));
        }
        findViewById(R.id.btn_todo_time).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerPopup popup = new TimePickerPopup(getContext())
                        .setTimePickerListener(new TimePickerListener() {
                            @Override
                            public void onTimeChanged(Date date) {
                                //时间改变
                                Instant instant = date.toInstant();
                                ZoneId zoneId = ZoneId.systemDefault();

                                LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
                                Toast.makeText(getContext(), "选择的时间："+localDateTime.format(df), Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onTimeConfirm(Date date, View view) {
                                //点击确认时间
                                Instant instant = date.toInstant();
                                ZoneId zoneId = ZoneId.systemDefault();

                                LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
                                TextView tv = findViewById(R.id.tv_todo_set_time);
                                tv.setText(localDateTime.format(df));
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                popup.setMode(TimePickerPopup.Mode.YMDHM);
                popup.dividerColor=R.color.light_gray;
                popup.setItemTextSize(20);

                new XPopup.Builder(getContext())
                        .asCustom(popup)
                        .show();
            }
        });
        findViewById(R.id.btn_todo_set_confirm).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissOrHideSoftInput();
                EditText edt_title = findViewById(R.id.edit_todo_title);
                EditText edt_addr = findViewById(R.id.edit_todo_addr);
                TextView tv_time = findViewById(R.id.tv_todo_set_time);
                if(edt_title.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"请输入事件名",Toast.LENGTH_SHORT);
                }else{
                    Todo todo = new Todo(0001,0001,edt_title.getText().toString(),edt_addr.getText().toString(),LocalDateTime.parse(tv_time.getText().toString(),df));
                    todoBrowserFragment.addTodoItem(todo,type);
                    dismiss();
                }
            }
        });
    }
    // 设置最大宽度，看需要而定，
    @Override
    protected int getMaxWidth() {
        return super.getMaxWidth();
    }
    // 设置最大高度，看需要而定
    @Override
    protected int getMaxHeight() {
        return super.getMaxHeight();
    }
    // 设置自定义动画器，看需要而定
    @Override
    protected PopupAnimator getPopupAnimator() {
        return super.getPopupAnimator();
    }
    /**
     * 弹窗的宽度，用来动态设定当前弹窗的宽度，受getMaxWidth()限制
     *
     * @return
     */
    protected int getPopupWidth() {
        return 0;
    }

    /**
     * 弹窗的高度，用来动态设定当前弹窗的高度，受getMaxHeight()限制
     *
     * @return
     */
    protected int getPopupHeight() {
        return 0;
    }
}
