package edu.whu.spacetime.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.lxj.xpopup.XPopup;
import com.xuexiang.xui.widget.toast.XToast;

import java.time.LocalDateTime;

import edu.whu.spacetime.R;
import edu.whu.spacetime.SpacetimeApplication;
import edu.whu.spacetime.dao.NoteDao;
import edu.whu.spacetime.domain.Note;
import edu.whu.spacetime.jp.wasabeef.richeditor.RichEditor;
import edu.whu.spacetime.widget.AIChatPopup;
import edu.whu.spacetime.widget.ConfirmDialog;

import java.util.regex.*;


public class EditorActivity extends AppCompatActivity {

    private RichEditor mEditor;

    private AIChatPopup chatPopup;

    private TextView mPreview;

    private TextView wordCount;

    private EditText editNoteTitle;

    private NoteDao noteDao;

    private Note note;

    /**
     * 当前笔记所处的笔记本，用于将笔记保存到指定的笔记本
     */
    private int notebookId;

    /**
     * 用于匹配html标签，统计字数时先去除标签
     */
    private static final String HTMLREGEX = "<[^>]+>";

    private Pattern htmlPattern;

    /**
     * 保存笔记
     */
    private void saveNote() {
        String content = mEditor.getHtml();
        content = (content == null) ? "" : content;
        String title = editNoteTitle.getText().toString();
        String plainText = htmlPattern.matcher(content).replaceAll("");
        ConfirmDialog saveDialog;
        if (note == null) {
            note = new Note();
            note.setContent(content);
            note.setPlainText(plainText);
            note.setTitle(title);
            note.setUserId(SpacetimeApplication.getInstance().getCurrentUser().getUserId());
            note.setCreateTime(LocalDateTime.now());
            note.setNotebookId(notebookId);
            saveDialog = new ConfirmDialog(this, getColor(R.color.dark_yellow), "新建笔记", "新建");
            saveDialog.setOnConfirmListener(() -> {
                noteDao.insertNote(note);
                finish();
            });
            saveDialog.setCancelListener(() -> finish());
            new XPopup.Builder(this).isDestroyOnDismiss(true).asCustom(saveDialog).show();
        } else {
            note.setContent(content);
            note.setPlainText(plainText);
            note.setTitle(title);
            saveDialog = new ConfirmDialog(this, getColor(R.color.dark_yellow), "保存笔记", "保存");
            saveDialog.setOnConfirmListener(() -> {
                noteDao.updateNote(note);
                finish();
            });
            saveDialog.setCancelListener(() -> finish());
            new XPopup.Builder(this).isDestroyOnDismiss(true).asCustom(saveDialog).show();
        }
    }

    @Override
    public void onBackPressed() {
        saveNote();
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        noteDao = SpacetimeApplication.getInstance().getDatabase().getNoteDao();
        htmlPattern = Pattern.compile(HTMLREGEX);
        editNoteTitle = findViewById(R.id.edit_note_title);
        mPreview = (TextView) findViewById(R.id.preview);
        wordCount = findViewById(R.id.tv_word_count);

        mEditor = (RichEditor) findViewById(R.id.editor);
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(16);
        mEditor.setEditorFontColor(Color.BLACK);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("请输入文本...");
        mEditor.setOnTextChangeListener(text -> {
            mPreview.setText(text);
            Matcher matcher = htmlPattern.matcher(text);
            String plainText = matcher.replaceAll("");
            wordCount.setText(plainText.length() + "字");
        });

        Bundle bundle = getIntent().getExtras();
        note = (Note) bundle.getSerializable("note");
        notebookId = bundle.getInt("notebookId");
        String notebookName = bundle.getString("notebookName");
        TextView tvCurrentNotebook = findViewById(R.id.tv_current_notebook);
        tvCurrentNotebook.setText(notebookName);
        // 打开笔记时传入的Note不为空，新建笔记时则传入null
        if (note != null) {
            mEditor.setHtml(note.getContent());
            mPreview.setText(note.getContent());
            editNoteTitle.setText(note.getTitle());
            wordCount.setText(note.getPlainText().length() + "字");
        } else {
            String content = bundle.getString("content");
            if (content != null) {
                mEditor.setHtml(content);
                mPreview.setText(content);
                wordCount.setText(content.length() + "字");
            }
        }
        bindBtnFunction();
    }

    /**
     * 绑定编辑器按钮的功能
     */
    private void bindBtnFunction() {
        findViewById(R.id.action_undo).setOnClickListener(v -> mEditor.undo());

        findViewById(R.id.action_redo).setOnClickListener(v -> mEditor.redo());

        findViewById(R.id.action_bold).setOnClickListener(v -> mEditor.setBold());

        findViewById(R.id.action_italic).setOnClickListener(v -> mEditor.setItalic());

        findViewById(R.id.action_subscript).setOnClickListener(v -> mEditor.setSubscript());

        findViewById(R.id.action_superscript).setOnClickListener(v -> mEditor.setSuperscript());

        findViewById(R.id.action_strikethrough).setOnClickListener(v -> mEditor.setStrikeThrough());

        findViewById(R.id.action_underline).setOnClickListener(v -> mEditor.setUnderline());

        findViewById(R.id.action_heading1).setOnClickListener(v -> mEditor.setHeading(1));

        findViewById(R.id.action_heading2).setOnClickListener(v -> mEditor.setHeading(2));

        findViewById(R.id.action_heading3).setOnClickListener(v -> mEditor.setHeading(3));

        findViewById(R.id.action_heading4).setOnClickListener(v -> mEditor.setHeading(4));

        findViewById(R.id.action_heading5).setOnClickListener(v -> mEditor.setHeading(5));

        findViewById(R.id.action_heading6).setOnClickListener(v -> mEditor.setHeading(6));

        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(v -> mEditor.setIndent());

        findViewById(R.id.action_outdent).setOnClickListener(v -> mEditor.setOutdent());

        findViewById(R.id.action_align_left).setOnClickListener(v -> mEditor.setAlignLeft());

        findViewById(R.id.action_align_center).setOnClickListener(v -> mEditor.setAlignCenter());

        findViewById(R.id.action_align_right).setOnClickListener(v -> mEditor.setAlignRight());

        findViewById(R.id.action_blockquote).setOnClickListener(v -> mEditor.setBlockquote());

        findViewById(R.id.action_insert_bullets).setOnClickListener(v -> mEditor.setBullets());

        findViewById(R.id.action_insert_numbers).setOnClickListener(v -> mEditor.setNumbers());

        findViewById(R.id.action_insert_image).setOnClickListener(v -> mEditor.insertImage("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg",
                "dachshund", 320));

        findViewById(R.id.action_insert_youtube).setOnClickListener(v -> mEditor.insertYoutubeVideo("https://www.youtube.com/embed/pS5peqApgUA"));

        findViewById(R.id.action_insert_audio).setOnClickListener(v -> mEditor.insertAudio("https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_5MG.mp3"));

        findViewById(R.id.action_insert_video).setOnClickListener(v -> mEditor.insertVideo("https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/1080/Big_Buck_Bunny_1080_10s_10MB.mp4", 360));

        findViewById(R.id.action_insert_link).setOnClickListener(v -> mEditor.insertLink("https://github.com/wasabeef", "wasabeef"));
        findViewById(R.id.action_insert_checkbox).setOnClickListener(v -> mEditor.insertTodo());

        findViewById(R.id.save_btn).setOnClickListener(v -> {
            saveNote();
        });

        findViewById(R.id.return_btn).setOnClickListener(v -> {
            saveNote();
        });

        ImageButton btnAIChat = findViewById(R.id.btn_ai_chat);
        if (note == null) {
            // 新建笔记时不能使用ai对话
            btnAIChat.setVisibility(View.GONE);
        } else {
            btnAIChat.setOnClickListener(v -> {
                chatPopup = (chatPopup == null) ? new AIChatPopup(this, note.getPlainText()) : chatPopup;
                new XPopup.Builder(this)
                        .isDestroyOnDismiss(false)
                        .autoFocusEditText(false)
                        .asCustom(chatPopup)
                        .show();
            });
        }
    }

}