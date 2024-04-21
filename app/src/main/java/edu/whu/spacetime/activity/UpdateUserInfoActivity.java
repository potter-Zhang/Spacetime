package edu.whu.spacetime.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;

import java.util.Objects;
import java.util.regex.Pattern;

import edu.whu.spacetime.R;
import edu.whu.spacetime.SpacetimeApplication;
import edu.whu.spacetime.dao.UserDao;
import edu.whu.spacetime.domain.User;

public class UpdateUserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton back_btn;
    private String property;
    TextView title;
    MaterialEditText user_property;
    Button save_btn;
    User user;
    UserDao userDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        // 获得UserAccountActivity传来的字符串作为类型
        Bundle myBundle = this.getIntent().getExtras();
        property = myBundle.getString("info");

        initView();
    }
    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.back_btn) finish();
        else if (v.getId() == R.id.save_btn)
        {
            String newValue = Objects.requireNonNull(user_property.getText()).toString();
            if (Objects.equals(property, "我的昵称") && !newValue.equals(user.getUsername()))
            {
                user.setUsername(newValue);
                userDao.updateUser(user);
                finish();
            }
            else if (Objects.equals(property, "手机") && !newValue.equals(user.getPhone()))
            {
                if (isValidPhoneNumber(newValue))
                {
                    user.setPhone(newValue);
                    userDao.updateUser(user);
                    finish();
                }
                else
                {
                    Toast.makeText(getBaseContext(), "请输入正确的手机号", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void initView()
    {
        user = SpacetimeApplication.getInstance().getCurrentUser();
        userDao = SpacetimeApplication.getInstance().getDatabase().getUserDao();
        back_btn = findViewById(R.id.back_btn);
        title = findViewById(R.id.title);
        title.setText(property);
        user_property = findViewById(R.id.user_property);
        save_btn = findViewById(R.id.save_btn);

        save_btn.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        if (Objects.equals(property, "我的昵称"))
        {
            user_property.setText(user.getUsername());
            user_property.setHint("用户名");
            user_property.setFloatingLabelText("用户名");

        }
        else if (Objects.equals(property, "手机"))
        {
            user_property.setText(user.getPhone());
            user_property.setHint("手机号");
            user_property.setFloatingLabelText("手机号");
        }
    }

    // 验证手机号
    public boolean isValidPhoneNumber(String phoneNumber) {
        if ((phoneNumber != null) && (!phoneNumber.isEmpty())) {
            return Pattern.matches("^1[3-9]\\d{9}$", phoneNumber);
        }
        return false;
    }

}