package edu.whu.spacetime.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lxj.xpopup.XPopup;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.SpacetimeApplication;
import edu.whu.spacetime.adapter.NoteBookListAdapter;
import edu.whu.spacetime.adapter.NoteListAdapter;
import edu.whu.spacetime.adapter.TodoListAdapter;
import edu.whu.spacetime.domain.Note;
import edu.whu.spacetime.domain.Notebook;
import edu.whu.spacetime.domain.Todo;
import edu.whu.spacetime.widget.TodoSetPopup;

public class TodoBrowserFragment extends Fragment {
    private TodoListAdapter todoListAdapter_unChecked;
    private TodoListAdapter todoListAdapter_Checked;
    private List<Todo> todoList_unChecked;
    private List<Todo> todoList_Checked;
    private View fragView;

    public TodoBrowserFragment(){
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragView = inflater.inflate(R.layout.fragment_todo_browser,container,false);
        setAddItemListener();
        setTodoList();
        return fragView;
    }
    private void setAddItemListener(){
        TodoBrowserFragment view = this;
        //add按钮
        fragView.findViewById(R.id.btn_todo_addItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(getContext())
                        .asCustom(new TodoSetPopup(getContext(),view))
                        .show();
            }
        });
    }
    private void setTodoList() {
        ListView todoListView = fragView.findViewById(R.id.list_todo);
        ListView todoListView_ok = fragView.findViewById(R.id.list_todo_ok);
        int userId = SpacetimeApplication.getInstance().getCurrentUser().getUserId();
        todoList_unChecked = SpacetimeApplication.getInstance().getDatabase().getTodoDao().getUnCheckedTodo(userId);
        todoList_Checked = SpacetimeApplication.getInstance().getDatabase().getTodoDao().getCheckedTodo(userId);

        TextView tv_numOfTodo = fragView.findViewById(R.id.tv_todo_numOfTodo);
        tv_numOfTodo.setText(String.valueOf(todoList_unChecked.size()).concat(" 条待办，").concat(String.valueOf(todoList_Checked.size())).concat(" 条已完成"));

        todoListAdapter_unChecked= new TodoListAdapter(getContext(), R.layout.item_todo_list, todoList_unChecked, this);
        todoListAdapter_Checked= new TodoListAdapter(getContext(), R.layout.item_todo_list, todoList_Checked, this);
        todoListView.setAdapter(todoListAdapter_unChecked);
        todoListView_ok.setAdapter(todoListAdapter_Checked);
    }
    public void refresh(){
        int userId = SpacetimeApplication.getInstance().getCurrentUser().getUserId();
        todoList_unChecked = SpacetimeApplication.getInstance().getDatabase().getTodoDao().getUnCheckedTodo(userId);
        todoList_Checked = SpacetimeApplication.getInstance().getDatabase().getTodoDao().getCheckedTodo(userId);
        TextView tv_numOfTodo = fragView.findViewById(R.id.tv_todo_numOfTodo);
        tv_numOfTodo.setText(String.valueOf(todoList_unChecked.size()).concat(" 条待办，").concat(String.valueOf(todoList_Checked.size())).concat(" 条已完成"));
        todoListAdapter_unChecked.clear();
        todoListAdapter_unChecked.addAll(todoList_unChecked);
        todoListAdapter_Checked.clear();
        todoListAdapter_Checked.addAll(todoList_Checked);
    }
}
