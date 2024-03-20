package edu.whu.spacetime.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lxj.xpopup.XPopup;

import java.time.format.DateTimeFormatter;
import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.SpacetimeApplication;
import edu.whu.spacetime.dao.TodoDao;
import edu.whu.spacetime.domain.Todo;
import edu.whu.spacetime.fragment.TodoBrowserFragment;
import edu.whu.spacetime.widget.NoteBookPopupMenu;
import edu.whu.spacetime.widget.TodoSetPopup;

public class TodoListAdapter extends ArrayAdapter<Todo> {
    private final int resourceId;
    private final TodoBrowserFragment Todo_view;
    private final TodoDao todoDao = SpacetimeApplication.getInstance().getDatabase().getTodoDao();
    private final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd   HH:mm");

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
        assert todo != null;
        if(todo.getChecked()){
            tvTitle.setTextColor(Color.GRAY);
        }
        TextView tvAddr = view.findViewById(R.id.tv_todo_addr);
        TextView tvTime = view.findViewById(R.id.tv_todo_time);
        tvTitle.setText(todo.getTitle());
        if(!todo.getAddr().isEmpty()){
            tvAddr.setText("地点：".concat(todo.getAddr()));
        }
        tvTime.setText(todo.getCreateTime().format(df));
        CheckBox checkBox = view.findViewById(R.id.ckBox_todo_ok);
        //勾选改变位置
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(todo.getChecked()){
                    Toast.makeText(getContext(),"已取消勾选",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(),"已勾选",Toast.LENGTH_SHORT).show();
                }
                todo.setChecked(!todo.getChecked());
                todoDao.updateTodo(todo);
                Todo_view.refresh();
            }
        });

        //添加点击事件，打开编辑弹窗
        view.setOnClickListener(new View.OnClickListener() {
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
