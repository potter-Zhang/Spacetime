package edu.whu.spacetime.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xuexiang.xui.widget.imageview.IconImageView;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.whu.spacetime.R;
import edu.whu.spacetime.SpacetimeApplication;
import edu.whu.spacetime.activity.UserAccountActivity;
import edu.whu.spacetime.activity.UserCalendarActivity;
import edu.whu.spacetime.activity.UserSettingActivity;
import edu.whu.spacetime.dao.NoteDao;
import edu.whu.spacetime.dao.TodoDao;
import edu.whu.spacetime.domain.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private CircleImageView user_profile;
    private RelativeLayout account_btn, user_setting_btn, user_update_btn, user_calendar_btn;
    private IconImageView setting_button;
    private View importWindow;
    private TextView user_name, user_id, user_using_days, user_note_count, user_todo_count;
    private User user;
    private NoteDao noteDao;
    private TodoDao todoDao;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null){
            rootView = inflater.inflate(R.layout.fragment_user, container, false);
        }
        initView();
        return rootView;
    }

    private void initView(){
        noteDao = SpacetimeApplication.getInstance().getDatabase().getNoteDao();
        todoDao = SpacetimeApplication.getInstance().getDatabase().getTodoDao();
        // 初始化user以及个人信息
        user = SpacetimeApplication.getInstance().getCurrentUser();
        user_name = rootView.findViewById(R.id.user_name);
        user_id = rootView.findViewById(R.id.uid);
        user_name.setText(user.getUsername());
        String userid = String.valueOf(user.getUserId());
        user_id.setText(userid);

        user_profile = rootView.findViewById(R.id.user_profile);
        account_btn = rootView.findViewById(R.id.user_account_btn);
        setting_button = rootView.findViewById(R.id.user_setting);
        user_setting_btn = rootView.findViewById(R.id.user_setting_btn);
        user_update_btn = rootView.findViewById(R.id.user_checkUpdate_btn);
        user_calendar_btn = rootView.findViewById(R.id.user_calendar_btn);
        user_using_days = rootView.findViewById(R.id.user_using_days);
        user_note_count = rootView.findViewById(R.id.user_note_count);
        user_todo_count = rootView.findViewById(R.id.user_todo_count);

        byte[] profile_bytes = user.getAvatar();
        if (profile_bytes == null) {
            user_profile.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_profile));
        }
        else
        {
            Bitmap profile_bitmap = BitmapFactory.decodeByteArray(profile_bytes, 0, profile_bytes.length);
            user_profile.setImageBitmap(profile_bitmap);
        }
        user_using_days.setText(String.valueOf(getTimeGap(LocalDateTime.now() ,user.getCreateTime())));
        user_note_count.setText(String.valueOf(noteDao.queryByUserId(user.getUserId()).size()));
        user_todo_count.setText(String.valueOf(todoDao.getAllTodo(user.getUserId()).size()));
        user_profile.setOnClickListener(this);
        account_btn.setOnClickListener(this);
        setting_button.setOnClickListener(this);
        user_setting_btn.setOnClickListener(this);
        user_update_btn.setOnClickListener(this);
        user_calendar_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.user_account_btn || vId == R.id.user_profile) {
            Intent intent2 = new Intent(getContext(), UserAccountActivity.class);
            startActivity(intent2);
        } else if (vId == R.id.user_setting || vId == R.id.user_setting_btn) {
            Intent intent2 = new Intent(getContext(), UserSettingActivity.class);
            startActivity(intent2);
        } else if (vId == R.id.user_checkUpdate_btn) {
            Toast.makeText(getContext(), "您已经是最新版本", Toast.LENGTH_SHORT).show();
        } else if (vId == R.id.user_calendar_btn) {
            Intent intent2 = new Intent(getContext(), UserCalendarActivity.class);
            startActivity(intent2);
        }
    }

    public int getTimeGap(LocalDateTime now, LocalDateTime createDay)
    {
        int[] d = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int now_year = now.getYear();
        int now_month = now.getMonth().getValue();
        int now_day = now.getDayOfMonth();
        int createDay_year = createDay.getYear();
        int createDay_month = createDay.getMonth().getValue();
        int createDay_day = createDay.getDayOfMonth();

        // 得到两个日期相对于0000.0.0的日期差
        long now_count = now_day, createDay_count = createDay_day;
        for (int i = 0; i <= now_year; i ++ )
        {
            if ((i % 4 == 0 && i % 100 != 0) || i % 400 == 0)
            {
                now_count += 366;
                if (i == now_year) d[2] = 29;
            }
            else
            {
                now_count += 365;
                if (i == now_year) d[2] = 28;
            }
        }
        for (int i = 1; i <= now_month; i ++ )
            now_day += d[i];
        for (int i = 0; i <= createDay_year; i ++ )
        {
            if ((i % 4 == 0 && i % 100 != 0) || i % 400 == 0)
            {
                createDay_count += 366;
                if (i == createDay_year) d[2] = 29;
            }
            else
            {
                createDay_count += 365;
                if (i == createDay_year) d[2] = 28;
            }
        }
        for (int i = 1; i <= createDay_month; i ++ )
            createDay_day += d[i];
        return (int) (now_count - createDay_count + 1);
    }
    @Override
    public void onResume() {
        super.onResume();
        byte[] profile_bytes = user.getAvatar();
        if (profile_bytes == null) {
            user_profile.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_profile));
        }
        else
        {
            Bitmap profile_bitmap = BitmapFactory.decodeByteArray(profile_bytes, 0, profile_bytes.length);
            user_profile.setImageBitmap(profile_bitmap);
        }
        user_using_days.setText(String.valueOf(getTimeGap(LocalDateTime.now() ,user.getCreateTime())));
        user_note_count.setText(String.valueOf(noteDao.queryByUserId(user.getUserId()).size()));
        user_todo_count.setText(String.valueOf(todoDao.getAllTodo(user.getUserId()).size()));
    }
}