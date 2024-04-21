package edu.whu.spacetime.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.SpacetimeApplication;
import edu.whu.spacetime.activity.HelloArActivity;
import edu.whu.spacetime.activity.MainActivity;
import edu.whu.spacetime.adapter.ARNoteListAdapter;
import edu.whu.spacetime.dao.ARNoteDao;
import edu.whu.spacetime.domain.ARNote;

public class StartARFragment extends Fragment {
    private View fragView;

    private RecyclerView arNoteListView;

    private ARNoteListAdapter adapter;

    public StartARFragment(){
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragView = inflater.inflate(R.layout.fragment_ar_start,container,false);
        setStartARButtonListener(fragView);

        arNoteListView = fragView.findViewById(R.id.list_ar_note);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        arNoteListView.setLayoutManager(layoutManager);
        setARNoteList();
        return fragView;
    }
    private void setStartARButtonListener(View view){
        view.findViewById(R.id.btn_ar_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HelloArActivity.class);
                startActivity(intent);
                //getActivity().finish();
            }
        });

//        view.findViewById(R.id.btn_screenshot).setOnClickListener(v -> {
//            getScreenshot();
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setARNoteList();
    }

    private void setARNoteList() {
        ARNoteDao arNoteDao = SpacetimeApplication.getInstance().getDatabase().getARNoteDao();
        List<ARNote> arNoteList = arNoteDao.queryARNotesByUserId(SpacetimeApplication.getInstance().getCurrentUser().getUserId());
        adapter = new ARNoteListAdapter(getContext(), arNoteList);
        arNoteListView.setAdapter(adapter);
        ImageView imgEmpty = fragView.findViewById(R.id.img_ar_list_empty);
        if (adapter.getItemCount() == 0 ) {
            imgEmpty.setVisibility(View.VISIBLE);
        } else {
            imgEmpty.setVisibility(View.GONE);
        }
        adapter.setOnSizeChangedListener(size -> {
            if (size == 0) {
                imgEmpty.setVisibility(View.VISIBLE);
            } else {
                imgEmpty.setVisibility(View.GONE);
            }
        });
    }

}
