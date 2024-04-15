package edu.whu.spacetime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import edu.whu.spacetime.R;

public class UserAccountActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton back_btn;
    RelativeLayout user_name_btn, user_gender_btn, user_tele_btn, user_region_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        initView();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back_btn) finish();
        else if (id == R.id.user_name_rl)
        {
//            Intent intent = new Intent(this, UpdateUserInfoActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("info", "我的昵称");
//            intent.putExtras(bundle);
//            startActivity(intent);
        }
        else if (id == R.id.user_gender_rl)
        {

        }
        else if (id == R.id.user_tele_rl)
        {

        }
        else if (id == R.id.user_region_rl)
        {

        }
    }

    void initView() {
        back_btn = findViewById(R.id.back_btn);
        user_name_btn = findViewById(R.id.user_name_rl);
        user_gender_btn = findViewById(R.id.user_gender_rl);
        user_tele_btn = findViewById(R.id.user_tele_rl);
        user_region_btn = findViewById(R.id.user_region_rl);
        back_btn.setOnClickListener(this);
        user_name_btn.setOnClickListener(this);
        user_gender_btn.setOnClickListener(this);
        user_region_btn.setOnClickListener(this);
    }
}