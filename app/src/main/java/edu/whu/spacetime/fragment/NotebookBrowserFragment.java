package edu.whu.spacetime.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.xuexiang.xui.widget.toast.XToast;

import java.util.ArrayList;
import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.SpacetimeApplication;
import edu.whu.spacetime.adapter.NoteBookListAdapter;
import edu.whu.spacetime.dao.NotebookDao;
import edu.whu.spacetime.domain.Notebook;
import edu.whu.spacetime.widget.InputDialog;

public class NotebookBrowserFragment extends Fragment {

    private NoteBookListAdapter adapter;

    private NotebookDao notebookDao;

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
        notebookDao = SpacetimeApplication.getInstance().getDatabase().getNotebookDao();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_notebook_browser, container, false);
        setNotebookList(fragmentView);

        // 设置监听
        fragmentView.findViewById(R.id.btn_create_notebook).setOnClickListener(v -> {
            // 弹出新建对话框
            this.openInputDialog();
        });
        return fragmentView;
    }

    // 设置ListView要显示的笔记本和相应的监听器
    private void setNotebookList(View fragmentView) {
        ListView notebookListView = fragmentView.findViewById(R.id.list_notebook);
        List<Notebook> notebookList = notebookDao.getNotebooksByUserId(SpacetimeApplication
                .getInstance().getCurrentUser().getUserId());
        adapter = new NoteBookListAdapter(getContext(), R.layout.item_notebook_list, notebookList);
        notebookListView.setAdapter(adapter);

        adapter.setOnNotebookDeleteListener(notebook -> {
            if (this.notebookChangedListener != null) {
                notebookChangedListener.OnNotebookChanged(adapter.getItem(0));
            }
        });

        notebookListView.setOnItemClickListener((parent, view, position, id) -> {
            Notebook notebook = (Notebook) parent.getItemAtPosition(position);
            // 触发切换笔记本事件
            if (notebookChangedListener != null) {
                notebookChangedListener.OnNotebookChanged(notebook);
            }
        });
    }
    
    private void openInputDialog() {
        InputDialog inputDialog = new InputDialog(getContext(), "新建笔记本");
        inputDialog.setOnInputConfirmListener(text -> {
            Notebook newNotebook = new Notebook(text, SpacetimeApplication.getInstance().getCurrentUser().getUserId());
            List<Long> rowId = notebookDao.insertNotebook(newNotebook);
            newNotebook.setNotebookId(rowId.get(0).intValue());
            adapter.add(newNotebook);
            adapter.notifyDataSetChanged();
        });
        new XPopup.Builder(getContext())
                .isDestroyOnDismiss(true)
                .asCustom(inputDialog)
                .show();
    }
}