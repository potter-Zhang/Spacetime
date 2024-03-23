package edu.whu.spacetime.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xuexiang.xui.widget.imageview.IconImageView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.whu.spacetime.R;
import edu.whu.spacetime.SpacetimeApplication;
import edu.whu.spacetime.activity.UserAccountActivity;
import edu.whu.spacetime.activity.UserCalendarActivity;
import edu.whu.spacetime.activity.UserSettingActivity;
import edu.whu.spacetime.dao.UserDao;
import edu.whu.spacetime.domain.User;
import edu.whu.spacetime.util.PickUtils;
import edu.whu.spacetime.util.ResultContract;
import edu.whu.spacetime.widget.ImportDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private CircleImageView user_profile;
    private RelativeLayout account_btn, user_setting_btn, user_collection_btn, user_update_btn, user_calendar_btn;
    private IconImageView setting_button;
    private AlertDialog import_popUp;
    private View importWindow;
    private Uri uri;


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

    private void initView() {
        user_profile = rootView.findViewById(R.id.user_profile);
        account_btn = rootView.findViewById(R.id.user_account_btn);
        setting_button = rootView.findViewById(R.id.user_setting);
        user_setting_btn = rootView.findViewById(R.id.user_setting_btn);
        user_collection_btn = rootView.findViewById(R.id.user_collection_btn);
        user_update_btn = rootView.findViewById(R.id.user_checkUpdate_btn);
        user_calendar_btn = rootView.findViewById(R.id.user_calendar_btn);

        user_profile.setOnClickListener(this);
        account_btn.setOnClickListener(this);
        setting_button.setOnClickListener(this);
        user_setting_btn.setOnClickListener(this);
        user_collection_btn.setOnClickListener(this);
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
        } else if (vId == R.id.user_collection_btn) {

        } else if (vId == R.id.user_checkUpdate_btn) {
            Toast.makeText(getContext(), "您已经是最新版本", Toast.LENGTH_SHORT).show();
        } else if (vId == R.id.user_calendar_btn) {
            Intent intent2 = new Intent(getContext(), UserCalendarActivity.class);
            startActivity(intent2);
        }
    }
}