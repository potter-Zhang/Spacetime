package edu.whu.spacetime.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.SpacetimeApplication;
import edu.whu.spacetime.activity.EditorActivity;
import edu.whu.spacetime.adapter.NoteListAdapter;
import edu.whu.spacetime.adapter.NoteRecyclerAdapter;
import edu.whu.spacetime.dao.NoteDao;
import edu.whu.spacetime.domain.Note;
import edu.whu.spacetime.domain.Notebook;
import edu.whu.spacetime.service.ConvertService;
import edu.whu.spacetime.util.PickUtils;
import edu.whu.spacetime.widget.ImportDialog;

public class NoteBrowserFragment extends Fragment {
    private static final String ARG_NOTEBOOK = "notebook";
    private static final String PPT = "application/vnd.ms-powerpoint";
    private static final int PPT_REQUEST_CODE = 0;
    private static final String PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
    private static final String PDF = "application/pdf";
    private static final int PDF_REQUEST_CODE = 1;
    private static final String WORD = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    private static final int AUDIO_REQUEST_CODE = 2;

    /**
     * 文件转文字
     */
    private ConvertService convertService;

    private View fragmentView;

    private NoteDao noteDao;

    /**
     * 抽屉视图
     */
    private DrawerLayout drawer;
    private FloatingActionButton btn_import_file;

    // 侧边栏中的笔记本菜单fragment
    private NotebookBrowserFragment notebookBrowserFragment;

    // 当前选中的笔记本
    private Notebook currentNotebook;

    private NoteListAdapter noteListAdapter;
    // private NoteRecyclerAdapter noteListAdapter;

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
        this.convertService = new ConvertService(getContext());
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
            jump2Editor(null, null);
            getActivity().overridePendingTransition(R.anim.from_bottom_to_top, R.anim.zoom_out);
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
        refreshNoteList();
    }

    /**
     * @return 如果处于编辑模式则退出编辑模式并返回true，或者抽屉尚未关闭时关闭抽屉返回true，否则返回false
     */
    public boolean onBackPressed() {
        if (noteListAdapter.isAtEditMode()) {
            noteListAdapter.exitEditMode();
            noteListAdapter.notifyDataSetChanged();
            fragmentView.findViewById(R.id.bar_edit_btn).setVisibility(View.INVISIBLE);
            return true;
        } else if (drawer.isOpen()) {
            drawer.close();
            return true;
        }
        return false;
    }

    /**
     * 刷新要显示笔记
     */
    private void refreshNoteList() {
        List<Note> noteList = noteDao.queryAllInNotebook(currentNotebook.getNotebookId());
        noteListAdapter.clear();
        noteListAdapter.addAll(noteList);
        TextView tvNotebookNumber = fragmentView.findViewById(R.id.tv_notebookNumber);
        tvNotebookNumber.setText(String.format("共%d篇笔记", noteList.size()));
        noteListAdapter.notifyDataSetChanged();
        showEmptyImg();
    }

    /**
     * 设置要显示的笔记，绑定ListView中item的点击和长按事件
     */
    private void setNoteList() {
        // RecyclerView noteListView = fragmentView.findViewById(R.id.list_note);
        ListView noteListView = fragmentView.findViewById(R.id.list_note);
        List<Note> noteList = noteDao.queryAllInNotebook(currentNotebook.getNotebookId());
        noteListAdapter = new NoteListAdapter(getContext(), R.layout.item_note_list, noteList);
        noteListView.setAdapter(noteListAdapter);
        TextView tvNotebookNumber = fragmentView.findViewById(R.id.tv_notebookNumber);
        tvNotebookNumber.setText(String.format("共%d篇笔记", noteList.size()));

        noteListView.setOnItemClickListener((parent, view, position, id) -> {
            Note note = noteListAdapter.getItem(position);
            // 编辑模式下点击item就选中checkbox，否则进入编辑器
            if (noteListAdapter.isAtEditMode()) {
                CheckBox checkBox = view.findViewById(R.id.check_note);
                checkBox.toggle();
                if (checkBox.isChecked()) {
                    noteListAdapter.addCheckedNote(note);
                } else {
                    noteListAdapter.removeCheckedNote(note);
                }
            } else {
                jump2Editor(note, null);
            }
        });

        noteListView.setOnItemLongClickListener((parent, view, position, id) -> {
            if (!noteListAdapter.isAtEditMode()) {
                noteListAdapter.editMode();
                noteListAdapter.notifyDataSetChanged();
                fragmentView.findViewById(R.id.bar_edit_btn).setVisibility(View.VISIBLE);
            }
            return true;
        });

        // 取消按钮，退出编辑模式
        fragmentView.findViewById(R.id.btn_cancel_edit).setOnClickListener(v -> {
            noteListAdapter.exitEditMode();
            fragmentView.findViewById(R.id.bar_edit_btn).setVisibility(View.INVISIBLE);
        });
        // 删除按钮，确认删除选中的笔记
        fragmentView.findViewById(R.id.btn_del_notes).setOnClickListener(v -> {
            noteListAdapter.removeCheckedNoteInView();
            noteListAdapter.exitEditMode();
            fragmentView.findViewById(R.id.bar_edit_btn).setVisibility(View.INVISIBLE);
            tvNotebookNumber.setText(String.format("共%d篇笔记", noteList.size()));
            showEmptyImg();
        });
    }

    /**
     * 点击左上方按钮弹出抽屉并播放动画
     */
    public void openDrawer() {
        if (this.drawer != null) {
            this.drawer.open();
        }
    }

    /**
     * adapter为空时显示空状态图片
     */
    private void showEmptyImg() {
        if (noteListAdapter.isEmpty()) {
            fragmentView.findViewById(R.id.img_note_list_empty).setVisibility(View.VISIBLE);
        } else {
            fragmentView.findViewById(R.id.img_note_list_empty).setVisibility(View.GONE);
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
            if (this.noteListAdapter.isAtEditMode()) {
                noteListAdapter.exitEditMode();
                fragmentView.findViewById(R.id.bar_edit_btn).setVisibility(View.INVISIBLE);
            }
            // 显示该笔记本中的笔记
            this.currentNotebook = newNotebook;
            tvNotebookName.setText(newNotebook.getName());
            this.refreshNoteList();
            drawer.close();
        });
        return notebookFragment;
    }

    /**
     * 跳转到编辑器
     * @param note 被点击选项对应的Note类，如果是新建则传入null
     * @param tmpPath 如果没有Note类，可以设置该字段来展示临时文件内的内容
     */
    private void jump2Editor(@Nullable Note note, String tmpPath) {
        Intent intent = new Intent(getActivity(), EditorActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("note", note);
        bundle.putInt("notebookId", currentNotebook.getNotebookId());
        bundle.putString("notebookName", currentNotebook.getName());
        bundle.putString("tmpPath", tmpPath);
        intent.putExtras(bundle);

        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.move_in_from_right, R.anim.small_move_to_left);
    }

    void openFolder(String str) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (str.equals("pdf")) {
            intent.setType(PDF);
            startActivityForResult(intent, PDF_REQUEST_CODE);
        }
        else if (str.equals("ppt")) {
            intent.setType(PPT);
            startActivityForResult(intent, PPT_REQUEST_CODE);
        }
        else if (str.equals("word")) {
            intent.setType(WORD);
            startActivityForResult(intent, AUDIO_REQUEST_CODE);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                Uri import_file_uri = resultData.getData();
                convert2TextAndShow(import_file_uri, requestCode);
            }
        }
    }

    /**
     * 将PPT、PDF、音频转换为文字然后打开编辑器
     * @param uri 文件Uri
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void convert2TextAndShow(Uri uri, int requestCode) {
        fragmentView.findViewById(R.id.progress_importing).setVisibility(View.VISIBLE);
        // 耗时操作使用子线程
        new Thread(() -> {
            try {
                String path, result = null;
                if (requestCode == PDF_REQUEST_CODE) {
                    path = PickUtils.getTMPPath(getContext(), uri, ".pdf");
                    File file = new File(path);
                    result = convertService.pdf2Text(file);
                    file.delete();
                } else if (requestCode == PPT_REQUEST_CODE) {
                    path = PickUtils.getTMPPath(getContext(), uri, ".ppt");
                    File file = new File(path);
                    result = convertService.ppt2Text(file);
                    file.delete();
                }
                // result可能很大，导致跳转activity失败，因此写入临时文件，新activity也从临时文件内读取
                String tmpText = PickUtils.getTMPPath(getContext(), result);
                fragmentView.findViewById(R.id.progress_importing).setVisibility(View.INVISIBLE);
                jump2Editor(null, tmpText);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}