package edu.whu.spacetime.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.SpacetimeApplication;
import edu.whu.spacetime.activity.EditorActivity;
import edu.whu.spacetime.adapter.NoteListAdapter;
import edu.whu.spacetime.dao.NoteDao;
import edu.whu.spacetime.domain.Note;

import edu.whu.spacetime.widget.ImportDialog;
import edu.whu.spacetime.domain.Notebook;

public class NoteBrowserFragment extends Fragment {
    private static final String ARG_NOTEBOOK = "notebook";
    public static final String PPT = "application/vnd.ms-powerpoint";
    public static final String PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
    public static final String PDF = "application/pdf";
    public static final String AUDIO = "";

    private View fragmentView;

    private NoteDao noteDao;

    // 抽屉
    private DrawerLayout drawer;
    private FloatingActionButton btn_import_file;
    private Uri import_file_uri;

    // 侧边栏中的笔记本菜单fragment
    private NotebookBrowserFragment notebookBrowserFragment;

    // 当前选中的笔记本
    private Notebook currentNotebook;

    public NoteBrowserFragment() {
        // Required empty public constructor
    }

    // 由MainActivity负责将默认笔记本传入
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
        this.noteDao = SpacetimeApplication.getInstance().getDatabase().getNoteDao();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_note_browser, container, false);
        this.drawer = fragmentView.findViewById(R.id.drawer);
        this.btn_import_file = fragmentView.findViewById(R.id.btn_import_file);

        TextView tvNotebookName = fragmentView.findViewById(R.id.tv_notebookName);
        tvNotebookName.setText(currentNotebook.getName());

        // 动态注册侧边栏中的notebookFragment
        this.notebookBrowserFragment = registerNotebookFragment();

        // 设置笔记列表显示内容
        this.setNoteList();

        // 设置监听
        ImageButton btnDrawerOpen = fragmentView.findViewById(R.id.btn_drawer_open);
        btnDrawerOpen.setOnClickListener(v -> openDrawer());

        // 设置弹出导入文件
        this.btn_import_file.setOnClickListener(v -> {
            ImportDialog dialogView = new ImportDialog(getActivity());
            dialogView.setCanceledOnTouchOutside(true);
            dialogView.setOnChooseFileListener(type -> {
                openFolder(type);
            });
            dialogView.show();
        });

        fragmentView.findViewById(R.id.btn_create_note).setOnClickListener(v -> {
            jump2Editor(null);
            getActivity().overridePendingTransition(R.anim.from_bottom, R.anim.from_top);
        });

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 切换fragment回来后要重新动态注册notebookFragment
        if (this.notebookBrowserFragment != null)
            this.notebookBrowserFragment = registerNotebookFragment();
        // 从编辑界面返回时可能添加了新的笔记，因此重新加载笔记列表
        setNoteList();
    }

    /**
     * 设置要显示的笔记，绑定ListView中item的点击和长按事件
     */
    private void setNoteList() {
        ListView noteListView = fragmentView.findViewById(R.id.list_note);
        List<Note> noteList = noteDao.queryAllInNotebook(currentNotebook.getNotebookId());
        NoteListAdapter listAdapter = new NoteListAdapter(getContext(), R.layout.item_note_list, noteList);
        noteListView.setAdapter(listAdapter);
        TextView tvNotebookNumber = fragmentView.findViewById(R.id.tv_notebookNumber);
        tvNotebookNumber.setText(String.format("共%d篇笔记", noteList.size()));

        // 长按进入编辑模式，显示复选框
        noteListView.setOnItemLongClickListener((parent, view, position, id) -> {
            if (!listAdapter.isAtEditMode()) {
                listAdapter.editMode();
                listAdapter.notifyDataSetChanged();
                fragmentView.findViewById(R.id.bar_edit_btn).setVisibility(View.VISIBLE);
                return true;
            } else {
                return false;
            }
        });
        noteListView.setOnItemClickListener((parent, view, position, id) -> {
            // 编辑模式下点击item就选中checkbox，否则进入编辑器
            if (listAdapter.isAtEditMode()) {
                CheckBox checkBox = view.findViewById(R.id.check_note);
                checkBox.toggle();
                if (checkBox.isChecked()) {
                    listAdapter.addCheckedNote(listAdapter.getItem(position));
                } else {
                    listAdapter.removeCheckedNote(listAdapter.getItem(position));
                }
            } else {
                Note note = (Note)parent.getItemAtPosition(position);
                jump2Editor(note);
            }
        });

        // 取消按钮
        fragmentView.findViewById(R.id.btn_cancel_edit).setOnClickListener(v -> {
            listAdapter.exitEditMode();
            listAdapter.notifyDataSetChanged();
            fragmentView.findViewById(R.id.bar_edit_btn).setVisibility(View.INVISIBLE);
        });
        // 删除按钮
        fragmentView.findViewById(R.id.btn_del_notes).setOnClickListener(v -> {
            listAdapter.removeCheckedNoteInView();
            listAdapter.exitEditMode();
            listAdapter.notifyDataSetChanged();
            fragmentView.findViewById(R.id.bar_edit_btn).setVisibility(View.INVISIBLE);
        });
    }

    public void openDrawer() {
        if (this.drawer != null) {
            this.drawer.open();
        }
    }

    /**
     * 动态注册笔记本侧边栏
    */
    private NotebookBrowserFragment registerNotebookFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fs = fragmentManager.beginTransaction();
        NotebookBrowserFragment notebookFragment = new NotebookBrowserFragment();
        fs.add(R.id.container_notebook, notebookFragment);
        fs.commit();

        TextView tvNotebookName = fragmentView.findViewById(R.id.tv_notebookName);
        notebookFragment.setOnNotebookChangedListener(newNotebook -> {
            // 显示该笔记本中的笔记
            this.currentNotebook = newNotebook;
            tvNotebookName.setText(newNotebook.getName());
            this.setNoteList();
            drawer.close();
        });
        return notebookFragment;
    }

    /**
     * 跳转到编辑器
     * @param note 被点击选项对应的Note类
     */
    private void jump2Editor(Note note) {
        Intent intent = new Intent(getActivity(), EditorActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("note", note);
        bundle.putInt("notebookId", currentNotebook.getNotebookId());
        bundle.putString("notebookName", currentNotebook.getName());
        intent.putExtras(bundle);

        startActivity(intent);
        getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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