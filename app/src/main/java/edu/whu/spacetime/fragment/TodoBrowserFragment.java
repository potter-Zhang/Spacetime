package edu.whu.spacetime.fragment;

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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.adapter.NoteBookListAdapter;
import edu.whu.spacetime.adapter.NoteListAdapter;
import edu.whu.spacetime.adapter.TodoListAdapter;
import edu.whu.spacetime.domain.Note;
import edu.whu.spacetime.domain.Notebook;
import edu.whu.spacetime.domain.Todo;

public class TodoBrowserFragment extends Fragment {
    private TodoListAdapter todoListAdapter;
    private TodoListAdapter todoListAdapter_ok;
    public TodoBrowserFragment(){
    }
    public void moveToAno(Todo todo,boolean type){
        if(type){
            todoListAdapter.remove(todo);
            todoListAdapter_ok.add(todo);
        }else{
            todoListAdapter_ok.remove(todo);
            todoListAdapter.add(todo);
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_todo_browser,container,false);
        setTodoList(fragView);
        return fragView;
    }

    private void setTodoList(View fragmentView) {
        ListView todoListView = fragmentView.findViewById(R.id.list_todo);
        ListView todoListView_ok = fragmentView.findViewById(R.id.list_todo_ok);
        List<Todo> todoList = new ArrayList<>();
        List<Todo> todoList_ok = new ArrayList<>();
        todoList.add(new Todo(0,0,"测试1","didian", LocalDateTime.now()));
        todoList.add(new Todo(0,0,"测试2","didian", LocalDateTime.now()));

        todoList_ok.add(new Todo(0,0,"已完成","didian", LocalDateTime.now()));
        TextView tv_numOfTodo = fragmentView.findViewById(R.id.tv_todo_numOfTodo);
        tv_numOfTodo.setText(todoList.size() +" 条待办");

        todoListAdapter= new TodoListAdapter(getContext(), R.layout.item_todo_list, todoList, true,this);
        todoListAdapter_ok= new TodoListAdapter(getContext(), R.layout.item_todo_list, todoList_ok, false,this);
        todoListView.setAdapter(todoListAdapter);
        todoListView_ok.setAdapter(todoListAdapter_ok);
    }
}
