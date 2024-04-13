package edu.whu.spacetime.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lxj.xpopup.XPopup;

import java.time.format.DateTimeFormatter;
import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.SpacetimeApplication;
import edu.whu.spacetime.dao.TodoDao;
import edu.whu.spacetime.domain.Todo;
import edu.whu.spacetime.fragment.TodoBrowserFragment;
import edu.whu.spacetime.widget.TodoSetPopup;

public class TodoListAdapter extends ArrayAdapter<Todo> {
    /**
     * todo项check状态改变监听器
     */
    public interface OnItemCheckedChangeListener {
        void onItemCheckedChange(Todo todo);
    }

    /**
     * todo项被删除事件监听器
     */
    public interface OnItemDeleteListener {
        void onItemDelete(Todo todo);
    }

    private final int resourceId;
    private final TodoBrowserFragment Todo_view;
    private final TodoDao todoDao = SpacetimeApplication.getInstance().getDatabase().getTodoDao();
    private final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd   HH:mm");

    private OnItemCheckedChangeListener itemCheckedChangeListener;

    private OnItemDeleteListener itemDeleteListener;

    public void setOnItemCheckedChangeListener(OnItemCheckedChangeListener itemCheckedChangeListener) {
        this.itemCheckedChangeListener = itemCheckedChangeListener;
    }

    public void setOnItemDeleteListener(OnItemDeleteListener itemDeleteListener) {
        this.itemDeleteListener = itemDeleteListener;
    }

    public TodoListAdapter(@NonNull Context context, int resource, @NonNull List<Todo> objects, TodoBrowserFragment view) {
        super(context, resource, objects);
        this.resourceId = resource;
        this.Todo_view = view;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Todo todo = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView tvTitle = view.findViewById(R.id.tv_todo_title);
        CheckBox checkBox = view.findViewById(R.id.ckBox_todo_ok);
        assert todo != null;
        if(todo.getChecked()){
            tvTitle.setTextColor(Color.GRAY);
            checkBox.setChecked(true);
        }
        TextView tvAddr = view.findViewById(R.id.tv_todo_addr);
        TextView tvTime = view.findViewById(R.id.tv_todo_time);
        tvTitle.setText(todo.getTitle());
        tvAddr.setText(todo.getAddr());
//        if(!todo.getAddr().isEmpty()){
//            tvAddr.setText("地点：".concat(todo.getAddr()));
//        }else{
//            tvAddr.setText("");
//        }
        tvTime.setText(todo.getCreateTime().format(df));

        // 勾选后触发监听器
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            todo.setChecked(!todo.getChecked());
            todoDao.updateTodo(todo);
            if (this.itemCheckedChangeListener != null) {
                this.itemCheckedChangeListener.onItemCheckedChange(todo);
            }
        });

        //添加点击事件，打开编辑弹窗
        View layout = view.findViewById(R.id.layout_todo_main);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(getContext())
                        .asCustom(new TodoSetPopup(getContext(),Todo_view,todo))
                        .show();
            }
        });

        //左滑添加删除事件
        View btn_dele = view.findViewById(R.id.btn_todo_deleteItem);
        btn_dele.setOnClickListener(v -> {
            todoDao.deleteTodo(todo);
            if (this.itemDeleteListener != null) {
                itemDeleteListener.onItemDelete(todo);
            }
        });

        //左滑添加编辑事件
        View btn_edit = view.findViewById(R.id.btn_todo_edit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(getContext())
                        .asCustom(new TodoSetPopup(getContext(),Todo_view,todo))
                        .show();
            }
        });
        return view;
    }
}
