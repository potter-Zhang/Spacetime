package edu.whu.spacetime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.ImageButton;

import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.toast.XToast;

import java.time.LocalDateTime;

import edu.whu.spacetime.R;
import edu.whu.spacetime.SpacetimeApplication;
import edu.whu.spacetime.dao.NotebookDao;
import edu.whu.spacetime.dao.UserDao;
import edu.whu.spacetime.domain.Notebook;
import edu.whu.spacetime.domain.User;

public class LoginActivity extends AppCompatActivity {
    private boolean isHide = false;  //输入框密码是否是隐藏，默认为false
    private UserDao userDao;
    private MaterialEditText mEditTextUserName;
    private ImageButton preview;
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
        final String getHint = mEditTextUserName.getHint().toString();
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

        User user = userDao.getUserByName(inputUsername);
        if (user == null || !user.getPassword().equals(inputPassword)) {
            XToast.error(this, "用户名或密码错误!").show();
        } else {
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
        // 读取一次user获取数据库自动生成的userId
        newUser.setUserId(userId.intValue());
        SpacetimeApplication.getInstance().setCurrentUser(newUser);

        // 给新用户创建一个新笔记本
        NotebookDao notebookDao = SpacetimeApplication.getInstance().getDatabase().getNotebookDao();
        Notebook defaultNotebook = new Notebook("默认笔记本", newUser.getUserId());
        notebookDao.insertNotebook(defaultNotebook);
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