package edu.whu.spacetime.fragment;

import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.adapter.NoteBookListAdapter;
import edu.whu.spacetime.adapter.NoteListAdapter;
import edu.whu.spacetime.domain.Note;
import edu.whu.spacetime.domain.NoteBook;

public class NoteBrowserFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_NOTEBOOK = "notebookId";

    private DrawerLayout drawer;

    private String mParam1;
    private String mParam2;

    public NoteBrowserFragment() {
        // Required empty public constructor
    }

    public static NoteBrowserFragment newInstance(int param1) {
        NoteBrowserFragment fragment = new NoteBrowserFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NOTEBOOK, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_NOTEBOOK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_note_browser, container, false);
        this.drawer = fragmentView.findViewById(R.id.drawer);

        // 设置笔记列表显示内容
        this.setNoteList(fragmentView);
        // 设置笔记本列表显示内容
        setNotebookList(fragmentView);

        // 设置监听
        ImageButton btnDrawerOpen = fragmentView.findViewById(R.id.btn_drawer_open);
        btnDrawerOpen.setOnClickListener(this);
        return fragmentView;
    }

    private void setNoteList(View fragmentView) {
        ListView noteListView = fragmentView.findViewById(R.id.list_note);
        List<Note> noteList = new ArrayList<>();
        noteList.add(new Note("测试1", 0, 0, "测试内容", LocalDateTime.now()));
        noteList.add(new Note("测试2", 0, 0, "测试内容", LocalDateTime.now()));
        NoteListAdapter listAdapter = new NoteListAdapter(getContext(), R.layout.item_note_list, noteList);
        noteListView.setAdapter(listAdapter);
    }

    private void setNotebookList(View fragmentView) {
        ListView noteBookListView = fragmentView.findViewById(R.id.list_notebook);
        List<NoteBook> noteBookList = new ArrayList<>();
        noteBookList.add(new NoteBook("测试1", 0));
        noteBookList.add(new NoteBook("测试2", 0));
        NoteBookListAdapter adapter = new NoteBookListAdapter(getContext(), R.layout.item_notebook_list, noteBookList);
        noteBookListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_drawer_open) {
            this.drawer.open();
        }
    }
}