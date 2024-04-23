package edu.whu.spacetime.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lxj.xpopup.XPopup;
import com.xuexiang.xui.widget.statelayout.SimpleAnimationListener;

import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.SpacetimeApplication;
import edu.whu.spacetime.adapter.TodoListAdapter;
import edu.whu.spacetime.domain.Todo;
import edu.whu.spacetime.widget.NoScrollListView;
import edu.whu.spacetime.widget.TodoSetPopup;

public class TodoBrowserFragment extends Fragment {
    private TodoListAdapter todoListAdapter_unChecked;
    private TodoListAdapter todoListAdapter_Checked;
    private NoScrollListView todoListView_unChecked;
    private NoScrollListView todoListView_Checked;
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
        //下面的add按钮
        fragView.findViewById(R.id.btn_todo_addItem2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(getContext())
                        .asCustom(new TodoSetPopup(getContext(),view))
                        .show();
            }
        });
    }
    private void setTodoList() {
        todoListView_unChecked = fragView.findViewById(R.id.list_todo);
        todoListView_Checked = fragView.findViewById(R.id.list_todo_ok);
        int userId = SpacetimeApplication.getInstance().getCurrentUser().getUserId();
        todoList_unChecked = SpacetimeApplication.getInstance().getDatabase().getTodoDao().getUnCheckedTodo(userId);
        todoList_Checked = SpacetimeApplication.getInstance().getDatabase().getTodoDao().getCheckedTodo(userId);

        TextView tv_numOfTodo = fragView.findViewById(R.id.tv_todo_numOfTodo);
        tv_numOfTodo.setText(String.valueOf(todoList_unChecked.size()).concat(" 条待办，").concat(String.valueOf(todoList_Checked.size())).concat(" 条已完成"));

        todoListAdapter_unChecked= new TodoListAdapter(getContext(), R.layout.item_todo_list, todoList_unChecked, this);
        todoListAdapter_Checked= new TodoListAdapter(getContext(), R.layout.item_todo_list, todoList_Checked, this);
        todoListView_unChecked.setAdapter(todoListAdapter_unChecked);
        todoListView_Checked.setAdapter(todoListAdapter_Checked);

        // unchecked中的todo被check后添加到另一个adapter中
        todoListAdapter_unChecked.setOnItemCheckedChangeListener((todo) -> {
            // todoListAdapter_unChecked.remove(todo);
            removeItemWithAnimation(todoListView_unChecked, todoListAdapter_unChecked, todo);
            todoListAdapter_Checked.add(todo);
            refreshSubTitle();
        });
        todoListAdapter_Checked.setOnItemCheckedChangeListener((todo) -> {
            removeItemWithAnimation(todoListView_Checked, todoListAdapter_Checked, todo);
            todoListAdapter_unChecked.add(todo);
            refreshSubTitle();
        });

        // todo被删除时从adapter中remove
        todoListAdapter_unChecked.setOnItemDeleteListener((todo -> {
            removeItemWithAnimation(todoListView_unChecked, todoListAdapter_unChecked, todo);
        }));
        todoListAdapter_Checked.setOnItemDeleteListener((todo -> {
            removeItemWithAnimation(todoListView_Checked, todoListAdapter_Checked, todo);
        }));
        showEmptyImg();
    }

    private void refreshSubTitle() {
        TextView tv_numOfTodo = fragView.findViewById(R.id.tv_todo_numOfTodo);
        // tv_numOfTodo.setText(String.valueOf(todoList_unChecked.size()).concat(" 条待办，").concat(String.valueOf(todoList_Checked.size())).concat(" 条已完成"));
        tv_numOfTodo.setText(String.valueOf(todoListAdapter_unChecked.getCount()).concat(" 条待办，").concat(String.valueOf(todoListAdapter_Checked.getCount())).concat(" 条已完成"));
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
        showEmptyImg();
    }

    /**
     * 从对应的list中删除条目，并带有删除动画
     * @param listView 条目所在的ListView
     * @param adapter ListView的adapter
     * @param todo 要删除的Todo对象
     */
    private void removeItemWithAnimation(NoScrollListView listView, TodoListAdapter adapter, Todo todo) {
        int position = adapter.getPosition(todo);
        View view = listView.getChildAt(position);
        Animator anim = ObjectAnimator.ofFloat(view, "translationX", 0,-50, -100, -250, -500,-750, -1000, -1500);
        anim.setDuration(1000);
        anim.start();
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                adapter.remove(todo);
                refreshSubTitle();
                showEmptyImg();
            }
        });
    }

    /**
     * 待办列表为空时显示提示图片
     */
    private void showEmptyImg() {
        if (todoListAdapter_unChecked.isEmpty() && todoListAdapter_Checked.isEmpty()) {
            fragView.findViewById(R.id.img_todo_list_empty).setVisibility(View.VISIBLE);
            fragView.findViewById(R.id.tv_completed).setVisibility(View.GONE);
        } else {
            fragView.findViewById(R.id.img_todo_list_empty).setVisibility(View.GONE);
            fragView.findViewById(R.id.tv_completed).setVisibility(View.VISIBLE);
        }
    }
}
