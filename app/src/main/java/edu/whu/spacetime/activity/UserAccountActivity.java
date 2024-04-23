package edu.whu.spacetime.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopupext.listener.CityPickerListener;
import com.lxj.xpopupext.listener.TimePickerListener;
import com.lxj.xpopupext.popup.CityPickerPopup;
import com.lxj.xpopupext.popup.TimePickerPopup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.whu.spacetime.R;
import edu.whu.spacetime.SpacetimeApplication;
import edu.whu.spacetime.dao.UserDao;
import edu.whu.spacetime.domain.User;
import edu.whu.spacetime.widget.ImportDialog;

public class UserAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_PICK = 1;
    ImageButton back_btn;
    RelativeLayout user_name_btn, user_gender_btn, user_tele_btn, user_region_btn;
    LinearLayout user_exit_btn;
    TextView user_name_tv, user_tele_tv, user_region_tv;
    CircleImageView user_profile;
    Spinner user_gender;
    User user;
    UserDao userDao;
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
                    String region = province +" - " +city+" - " +area;
                    user.setRegion(region);
                    userDao.updateUser(user);
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
            SharedPreferences sharedPreferences= getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);;
            sharedPreferences.edit().remove("autoLogin").apply();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else if (id == R.id.user_profile)
        {
            openFolder();
        }
    }

    void initView() {
        user = SpacetimeApplication.getInstance().getCurrentUser();
        userDao = SpacetimeApplication.getInstance().getDatabase().getUserDao();
        back_btn = findViewById(R.id.back_btn);
        user_tele_tv = findViewById(R.id.user_tele);
        user_name_btn = findViewById(R.id.user_name_rl);
        user_gender_btn = findViewById(R.id.user_gender_rl);
        user_tele_btn = findViewById(R.id.user_tele_rl);
        user_region_btn = findViewById(R.id.user_region_rl);
        user_name_tv = findViewById(R.id.user_name);
        user_region_tv = findViewById(R.id.user_region);
        user_exit_btn = findViewById(R.id.user_exit_ll);
        user_profile = findViewById(R.id.user_profile);
        byte[] profile_bytes = user.getAvatar();
        if (profile_bytes == null) {
            user_profile.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_profile));
        }
        else
        {
            Bitmap profile_bitmap = BitmapFactory.decodeByteArray(profile_bytes, 0, profile_bytes.length);
            user_profile.setImageBitmap(profile_bitmap);
        }
        user_gender = findViewById(R.id.user_gender);
        if (user.isGender()){
            user_gender.setSelection(1);
        }
        else{
            user_gender.setSelection(0);
        }
        user_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                {
                    user.setGender(false);
                    userDao.updateUser(user);
                }
                else {
                    user.setGender(true);
                    userDao.updateUser(user);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        user_name_tv.setText(user.getUsername());
        user_region_tv.setText(user.getRegion());
        user_tele_tv.setText(user.getPhone());
        back_btn.setOnClickListener(this);
        user_name_btn.setOnClickListener(this);
        user_gender_btn.setOnClickListener(this);
        user_region_btn.setOnClickListener(this);
        user_tele_btn.setOnClickListener(this);
        user_exit_btn.setOnClickListener(this);
        user_profile.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = SpacetimeApplication.getInstance().getCurrentUser();
        user_tele_tv.setText(user.getPhone());
        user_name_tv.setText(user.getUsername());
    }

    void openFolder() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                Uri uri = resultData.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    // JPEG压缩
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] bytes = baos.toByteArray();
                    user.setAvatar(bytes);
                    userDao.updateUser(user);
                    user_profile.setImageBitmap(bitmap);
                    Toast.makeText(UserAccountActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}