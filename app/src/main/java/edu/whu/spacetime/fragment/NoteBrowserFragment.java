package edu.whu.spacetime.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.activity.EditorActivity;
import edu.whu.spacetime.adapter.NoteListAdapter;
import edu.whu.spacetime.domain.Note;
import edu.whu.spacetime.domain.Notebook;

public class NoteBrowserFragment extends Fragment {
    private static final String ARG_NOTEBOOK = "notebook";

    private DrawerLayout drawer;

    private NotebookBrowserFragment notebookBrowserFragment;

    private Notebook currentNotebook;

    public NoteBrowserFragment() {
        // Required empty public constructor
    }

    public static NoteBrowserFragment newInstance(Notebook notebook) {
        NoteBrowserFragment fragment = new NoteBrowserFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTEBOOK, notebook);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentNotebook = (Notebook) getArguments().getSerializable(ARG_NOTEBOOK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_note_browser, container, false);
        this.drawer = fragmentView.findViewById(R.id.drawer);

        TextView tvNotebookName = fragmentView.findViewById(R.id.tv_notebookName);
        tvNotebookName.setText(currentNotebook.getName());

        this.notebookBrowserFragment = registerNotebookFragment();

        // 设置笔记列表显示内容
        this.setNoteList(fragmentView);

        // 设置监听
        ImageButton btnDrawerOpen = fragmentView.findViewById(R.id.btn_drawer_open);
        btnDrawerOpen.setOnClickListener(v -> openDrawer());

        this.notebookBrowserFragment.setOnNotebookChangedListener(newNotebook -> {
            // 显示该笔记本中的笔记
            this.currentNotebook = newNotebook;
            tvNotebookName.setText(newNotebook.getName());
            drawer.close();
        });

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 切换fragment回来后要重新动态注册notebookFragment
        // this.notebookBrowserFragment = registerNotebookFragment();
    }

    private void setNoteList(View fragmentView) {
        ListView noteListView = fragmentView.findViewById(R.id.list_note);
        List<Note> noteList = new ArrayList<>();
        noteList.add(new Note("测试1", 0, 0, "测试内容", LocalDateTime.now()));
        noteList.add(new Note("测试2", 0, 0, "测试内容", LocalDateTime.now()));
        NoteListAdapter listAdapter = new NoteListAdapter(getContext(), R.layout.item_note_list, noteList);
        noteListView.setAdapter(listAdapter);

        noteListView.setOnItemClickListener((parent, view, position, id) -> {
            Note note = (Note)parent.getItemAtPosition(position);
            jump2Editor(note);
        });
    }

    public void openDrawer() {
        if (this.drawer != null) {
            this.drawer.open();
        }
    }

    // 动态注册笔记本侧边栏
    private NotebookBrowserFragment registerNotebookFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fs = fragmentManager.beginTransaction();
        NotebookBrowserFragment notebookFragment = new NotebookBrowserFragment();
        fs.add(R.id.container_notebook, notebookFragment);
        fs.commit();
        return notebookFragment;
    }

    // 跳转到编辑器
    private void jump2Editor(Note note) {
        Intent intent = new Intent(getActivity(), EditorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("note", note);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}