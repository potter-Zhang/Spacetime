package edu.whu.spacetime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import edu.whu.spacetime.R;

public class UserAccountActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton back_btn;
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
    }

    void initView() {
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);
    }
}