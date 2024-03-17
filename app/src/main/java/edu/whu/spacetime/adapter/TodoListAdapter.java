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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lxj.xpopup.XPopup;

import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.domain.Todo;
import edu.whu.spacetime.fragment.TodoBrowserFragment;
import edu.whu.spacetime.widget.NoteBookPopupMenu;
import edu.whu.spacetime.widget.TodoSetPopup;

public class TodoListAdapter extends ArrayAdapter<Todo> {
    private int resourceId;

    private List<Todo> todoList;

    private TodoBrowserFragment Todo_view;

    boolean type_ok;

    public TodoListAdapter(@NonNull Context context, int resource, @NonNull List<Todo> objects, boolean type_ok , TodoBrowserFragment view) {
        super(context, resource, objects);
        this.resourceId = resource;
        this.todoList = objects;
        this.type_ok = type_ok;
        this.Todo_view = view;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Todo todo = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView tvTitle = view.findViewById(R.id.tv_todo_title);
        if(!type_ok){
            tvTitle.setTextColor(Color.GRAY);
        }
        TextView tvAddr = view.findViewById(R.id.tv_todo_addr);
        TextView tvTime = view.findViewById(R.id.tv_todo_time);

        tvTitle.setText(todo.getTitle());
        tvAddr.setText(todo.getAddr());
        tvTime.setText(todo.getCreateTime().toLocalDate().toString());

        CheckBox checkBox = view.findViewById(R.id.ckBox_todo_ok);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Todo_view.moveToAno(todo,type_ok);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(getContext())
                        .asCustom(new TodoSetPopup(getContext()))
                        .show();
            }
        });
        return view;
    }
}
