package edu.whu.spacetime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.toast.XToast;

import edu.whu.spacetime.R;
import edu.whu.spacetime.SpacetimeApplication;
import edu.whu.spacetime.dao.UserDao;
import edu.whu.spacetime.domain.User;

public class LoginActivity extends AppCompatActivity {
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userDao = SpacetimeApplication.getIntance().getUserDatabase().getUserDao();

        // 登录按钮
        findViewById(R.id.btn_login).setOnClickListener(v -> this.login());
        // 注册按钮
        findViewById(R.id.btn_register).setOnClickListener(v -> register());
        // 直接进入按钮
        findViewById(R.id.btn_enter).setOnClickListener(v -> jump2Main());
    }

    private void login() {
        MaterialEditText editUsername = findViewById(R.id.edit_username);
        MaterialEditText editPassword = findViewById(R.id.edit_password);
        String inputUsername = editUsername.getEditValue();
        String inputPassword = editPassword.getEditValue();

        User user = userDao.getUserByName(inputUsername);
        if (!user.getPassword().equals(inputPassword)) {
            XToast.error(this, "用户名或密码错误!").show();
        } else {
            jump2Main();
        }
    }

    private void register() {
        MaterialEditText editUsername = findViewById(R.id.edit_username);
        MaterialEditText editPassword = findViewById(R.id.edit_password);
        String inputUsername = editUsername.getEditValue();
        String inputPassword = editPassword.getEditValue();

        User user = userDao.getUserByName(inputUsername);
        if (user != null) {
            XToast.error(this, "该用户名已被占用！").show();
            return;
        }
        User newUser = new User(inputUsername, inputPassword, "");
        userDao.insertUser(newUser);
        jump2Main();
    }

    private void jump2Main() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}