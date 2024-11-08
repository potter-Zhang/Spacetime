package edu.whu.spacetime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.toast.XToast;

import java.time.LocalDateTime;
import java.util.Collections;

import edu.whu.spacetime.R;
import edu.whu.spacetime.SpacetimeApplication;
import edu.whu.spacetime.dao.NoteDao;
import edu.whu.spacetime.dao.NotebookDao;
import edu.whu.spacetime.dao.TodoDao;
import edu.whu.spacetime.dao.UserDao;
import edu.whu.spacetime.domain.Note;
import edu.whu.spacetime.domain.Notebook;
import edu.whu.spacetime.domain.Todo;
import edu.whu.spacetime.domain.User;

public class LoginActivity extends AppCompatActivity {
    private boolean isHide = false;  //输入框密码是否是隐藏，默认为false
    private UserDao userDao;
    private MaterialEditText mEditTextUserName;
    private MaterialEditText mEditTextUserPasswd;
    private CheckBox checkBox_remember;
    private CheckBox checkBox_auto;
    private ImageButton preview;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preview = findViewById(R.id.preview);

        userDao = SpacetimeApplication.getInstance().getDatabase().getUserDao();

        // 登录按钮
        findViewById(R.id.btn_login).setOnClickListener(v -> this.login());
        // 注册按钮
        findViewById(R.id.btn_register).setOnClickListener(v -> register());
        // 直接进入按钮
        findViewById(R.id.btn_enter).setOnClickListener(v -> direct());
        // 输入光标消失
        mEditTextUserName = findViewById(R.id.edit_username);
        mEditTextUserPasswd = findViewById(R.id.edit_password);
        final String getHint = mEditTextUserName.getHint().toString();

        //自动登录和记住密码
        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        checkBox_remember = findViewById(R.id.ckBox_rememberPasswd);
        checkBox_auto = findViewById(R.id.ckBox_autoLogin);
        // 检查是否存在保存的用户名和密码
        if (sharedPreferences.contains("username") && sharedPreferences.contains("password")) {
            String savedUsername = sharedPreferences.getString("username", "");
            String savedPassword = sharedPreferences.getString("password", "");
            mEditTextUserName.setText(savedUsername);
            mEditTextUserPasswd.setText(savedPassword);
            checkBox_remember.setChecked(true);

            // 检查是否选择了自动登录
            if (sharedPreferences.getBoolean("autoLogin", false)) {
                checkBox_auto.setChecked(true);
                login();
            }
        }
        mEditTextUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditTextUserName.setFocusable(true);
                mEditTextUserName.setFocusableInTouchMode(true);
                mEditTextUserName.requestFocus();
                mEditTextUserName.findFocus();
            }
        });
        mEditTextUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mEditTextUserName.setHint("");
                }else {
                    mEditTextUserName.setHint(getHint);
                }
            }
        });

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowPassword();
            }
        });
    }

    private void login() {
        MaterialEditText editUsername = findViewById(R.id.edit_username);
        MaterialEditText editPassword = findViewById(R.id.edit_password);
        String inputUsername = editUsername.getEditValue();
        String inputPassword = editPassword.getEditValue();

        if(inputUsername.isEmpty()||inputPassword.isEmpty()){
            XToast.error(this, "用户名或密码不能为空!").show();
            return;
        }
        User user = userDao.getUserByName(inputUsername);
        if (user == null || !user.getPassword().equals(inputPassword)) {
            XToast.error(this, "用户名或密码错误!").show();
        } else {
            if(checkBox_remember.isChecked()){
                sharedPreferences.edit().putString("username", inputUsername).putString("password",inputPassword).apply();
                if(checkBox_auto.isChecked()){
                    sharedPreferences.edit().putBoolean("autoLogin", true).apply();
                }else{
                    sharedPreferences.edit().remove("autoLogin").apply();
                }
            }else if(checkBox_auto.isChecked()){
                sharedPreferences.edit().putString("username", inputUsername).putString("password",inputPassword).apply();
                sharedPreferences.edit().putBoolean("autoLogin", true).apply();
            }else{
                sharedPreferences.edit().remove("username").remove("password").apply();
            }
            SpacetimeApplication.getInstance().setCurrentUser(user);
            jump2Main();
        }
    }

    private void register() {
        MaterialEditText editUsername = findViewById(R.id.edit_username);
        MaterialEditText editPassword = findViewById(R.id.edit_password);
        String inputUsername = editUsername.getEditValue();
        String inputPassword = editPassword.getEditValue();

        if (inputUsername.length() == 0 || inputPassword.length() == 0) {
            XToast.error(this, "用户名或密码不能为空！").show();
            return;
        }

        if (userDao.getUserByName(inputUsername) != null) {
            XToast.error(this, "该用户名已被占用！").show();
            return;
        }
        // 创建新用户
        User newUser = new User();
        newUser.setUsername(inputUsername);
        newUser.setPassword(inputPassword);
        newUser.setGender(true);
        newUser.setCreateTime(LocalDateTime.now());
        Long userId = userDao.insertUser(newUser);
        newUser.setUserId(userId.intValue());
        SpacetimeApplication.getInstance().setCurrentUser(newUser);

        // 给新用户创建一个新笔记本
        NotebookDao notebookDao = SpacetimeApplication.getInstance().getDatabase().getNotebookDao();
        Notebook defaultNotebook = new Notebook("默认笔记本", newUser.getUserId());
        Long notebookId = notebookDao.insertNotebook(defaultNotebook).get(0);
        // 添加一条引导note
        NoteDao noteDao = SpacetimeApplication.getInstance().getDatabase().getNoteDao();
        Note guidanceNote = new Note("长按可删除", newUser.getUserId(), notebookId.intValue(), "", LocalDateTime.now());
        noteDao.insertNote(guidanceNote);
        // 添加一条引导todo
        TodoDao todoDao = SpacetimeApplication.getInstance().getDatabase().getTodoDao();
        Todo todo = new Todo(newUser.getUserId(), "左划可删除", "", LocalDateTime.now(), false);
        todoDao.insertTodo(todo);
        jump2Main();
    }

    private void direct() {
        User user = userDao.getSingleUser();
        if (user == null) return;
        SpacetimeApplication.getInstance().setCurrentUser(user);
        jump2Main();
    }

    private void jump2Main() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void isShowPassword() {
        MaterialEditText editPassword = findViewById(R.id.edit_password);
        if(isHide == false) {
            //表示显示密码的“眼睛”图标
            preview.setImageResource(R.drawable.preview_open);
            //密文
            HideReturnsTransformationMethod method1 = HideReturnsTransformationMethod.getInstance();

            editPassword.setTransformationMethod(method1);
            isHide = true;
        } else {
            //表示隐藏密码的“闭眼”图标
            preview.setImageResource(R.drawable.preview_close);
            //密文
            TransformationMethod method2 = PasswordTransformationMethod.getInstance();
            editPassword.setTransformationMethod(method2);
            isHide = false;
        }
        //重置光标位置
        int index = editPassword.getText().toString().length();
        editPassword.setSelection(index) ;
    }
}