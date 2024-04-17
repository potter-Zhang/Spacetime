package edu.whu.spacetime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.whu.spacetime.R;
import edu.whu.spacetime.SpacetimeApplication;
import edu.whu.spacetime.domain.User;

public class UserSettingActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton back_btn;
    private TextView user_name;
    private CircleImageView user_profile;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        initView();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back_btn) finish();
    }

    void initView() {
        user = SpacetimeApplication.getInstance().getCurrentUser();
        back_btn = findViewById(R.id.back_btn);
        user_name = findViewById(R.id.user_name);
        user_profile = findViewById(R.id.user_profile);
        byte[] profile_bytes = user.getAvatar();
        Bitmap profile_bitmap = BitmapFactory.decodeByteArray(profile_bytes, 0, profile_bytes.length);
        user_profile.setImageBitmap(profile_bitmap);
        user_name.setText(user.getUsername());
        back_btn.setOnClickListener(this);
    }
}