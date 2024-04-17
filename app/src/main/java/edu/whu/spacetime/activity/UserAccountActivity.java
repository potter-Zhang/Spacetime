package edu.whu.spacetime.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopupext.listener.CityPickerListener;
import com.lxj.xpopupext.listener.TimePickerListener;
import com.lxj.xpopupext.popup.CityPickerPopup;
import com.lxj.xpopupext.popup.TimePickerPopup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import edu.whu.spacetime.R;
import edu.whu.spacetime.SpacetimeApplication;
import edu.whu.spacetime.domain.User;
import edu.whu.spacetime.widget.ImportDialog;

public class UserAccountActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton back_btn;
    RelativeLayout user_name_btn, user_gender_btn, user_tele_btn, user_region_btn;
    LinearLayout user_exit_btn;
    TextView user_name_tv, user_tele_tv, user_region_tv;
    User user;
    static String tele, region;
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
            Intent intent = new Intent(this, UpdateUserInfoActivity.class);
            Bundle bundle = new Bundle();
            // 传递修改的属性类型
            bundle.putString("info", "我的昵称");
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else if (id == R.id.user_tele_rl)
        {
            Intent intent = new Intent(this, UpdateUserInfoActivity.class);
            Bundle bundle = new Bundle();
            // 传递修改的属性类型
            bundle.putString("info", "手机");
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else if (id == R.id.user_region_rl)
        {
            CityPickerPopup popup = new CityPickerPopup(UserAccountActivity.this);
            popup.setCityPickerListener(new CityPickerListener() {
                @Override
                public void onCityConfirm(String province, String city, String area, View v) {
                    Log.e("tag", province +" - " +city+" - " +area);
                    Toast.makeText(UserAccountActivity.this, province +" - " +city+" - " +area, Toast.LENGTH_SHORT).show();
                    region = province +" - " +city+" - " +area;
                    user_region_tv.setText(region);
                }
                @Override
                public void onCityChange(String province, String city, String area) {

                }

                @Override
                public void onCancel() {

                }
            });
            new XPopup.Builder(UserAccountActivity.this)
                    .asCustom(popup)
                    .show();
        }
        else if (id == R.id.user_exit_ll)
        {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    void initView() {
        user = SpacetimeApplication.getInstance().getCurrentUser();
        back_btn = findViewById(R.id.back_btn);
        user_tele_tv = findViewById(R.id.user_tele);
        user_name_btn = findViewById(R.id.user_name_rl);
        user_gender_btn = findViewById(R.id.user_gender_rl);
        user_tele_btn = findViewById(R.id.user_tele_rl);
        user_region_btn = findViewById(R.id.user_region_rl);
        user_name_tv = findViewById(R.id.user_name);
        user_region_tv = findViewById(R.id.user_region);
        user_exit_btn = findViewById(R.id.user_exit_ll);
        user_name_tv.setText(user.getUsername());
        tele = (String) user_tele_tv.getText();
        region = (String) user_region_tv.getText();
        back_btn.setOnClickListener(this);
        user_name_btn.setOnClickListener(this);
        user_gender_btn.setOnClickListener(this);
        user_region_btn.setOnClickListener(this);
        user_tele_btn.setOnClickListener(this);
        user_exit_btn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = SpacetimeApplication.getInstance().getCurrentUser();
        user_tele_tv.setText(tele);
        user_name_tv.setText(user.getUsername());
        user_region_tv.setText(region);
    }
}