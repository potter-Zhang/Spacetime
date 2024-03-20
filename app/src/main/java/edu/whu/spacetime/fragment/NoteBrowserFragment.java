package edu.whu.spacetime.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.activity.EditorActivity;
import edu.whu.spacetime.adapter.NoteListAdapter;
import edu.whu.spacetime.domain.Note;
import edu.whu.spacetime.widget.ImportDialog;

public class NoteBrowserFragment extends Fragment {
    private static final String ARG_NOTEBOOK = "notebookId";
    public static final String PPT = "application/vnd.ms-powerpoint";
    public static final String PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
    public static final String PDF = "application/pdf";
    public static final String AUDIO = "";

    private DrawerLayout drawer;
    private FloatingActionButton btn_import_file;
    private Uri import_file_uri;

    private NotebookBrowserFragment notebookBrowserFragment;

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
        this.btn_import_file = fragmentView.findViewById(R.id.btn_import_file);

        // 设置笔记列表显示内容
        this.setNoteList(fragmentView);

        this.notebookBrowserFragment = registerNotebookFragment();

        // 设置监听
        ImageButton btnDrawerOpen = fragmentView.findViewById(R.id.btn_drawer_open);
        btnDrawerOpen.setOnClickListener(v -> openDrawer());

        this.notebookBrowserFragment.setOnNotebookChangedListener(newNotebook -> {
            // 显示该笔记本中的笔记
            drawer.close();
        });

        // 设置弹出导入文件
        this.btn_import_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImportDialog dialogView = new ImportDialog(getActivity());
                dialogView.setCanceledOnTouchOutside(true);
                dialogView.setOnChooseFileListener(type -> {
                    openFolder(type);
                });
                dialogView.show();
            }
        });

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 切换fragment回来后要重新动态注册notebookFragment
        this.notebookBrowserFragment = registerNotebookFragment();
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

    void openFolder(String str) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (str.equals("pdf")) {
            intent.setType(PDF);
        }
        else if (str.equals("ppt")) {
            intent.setType(PPT);
        }
        else if (str.equals("audio")) {
            intent.setType("*/*");
        }

        startActivityForResult(intent, 0);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            if (resultData != null) {
                import_file_uri = resultData.getData();
            }
        }
    }
}