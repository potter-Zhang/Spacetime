package edu.whu.spacetime.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xuexiang.xui.widget.toast.XToast;

import java.util.ArrayList;
import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.adapter.NoteBookListAdapter;
import edu.whu.spacetime.domain.Notebook;

public class NotebookBrowserFragment extends Fragment implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_create_notebook) {
            // 新建笔记本
        }
    }

    // 自定义笔记本切换事件监听器
    public interface OnNotebookChangedListener {
        void OnNotebookChanged(Notebook newNotebook);
    }

    private OnNotebookChangedListener notebookChangedListener;

    public void setOnNotebookChangedListener(OnNotebookChangedListener listener) {
        this.notebookChangedListener = listener;
    }

    public NotebookBrowserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_notebook_browser, container, false);
        setNotebookList(fragmentView);

        // 设置监听
        fragmentView.findViewById(R.id.btn_create_notebook).setOnClickListener(this);
        return fragmentView;
    }

    // 设置ListView要显示的笔记本和相应的监听器
    private void setNotebookList(View fragmentView) {
        ListView notebookListView = fragmentView.findViewById(R.id.list_notebook);
        List<Notebook> notebookList = new ArrayList<>();
        notebookList.add(new Notebook("测试1", 0));
        notebookList.add(new Notebook("测试2", 0));
        NoteBookListAdapter adapter = new NoteBookListAdapter(getContext(), R.layout.item_notebook_list, notebookList);
        notebookListView.setAdapter(adapter);

        notebookListView.setOnItemClickListener((parent, view, position, id) -> {
            Notebook notebook = (Notebook) parent.getItemAtPosition(position);
            // 触发切换笔记本事件
            if (notebookChangedListener != null) {
                notebookChangedListener.OnNotebookChanged(notebook);
            }
        });
    }
}