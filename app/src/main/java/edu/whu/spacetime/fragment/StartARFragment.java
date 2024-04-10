package edu.whu.spacetime.fragment;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.io.ByteArrayOutputStream;
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

        view.findViewById(R.id.btn_screenshot).setOnClickListener(v -> {
            getScreenshot();
        });
    }

    private void setARNoteList() {
        ARNoteDao arNoteDao = SpacetimeApplication.getInstance().getDatabase().getARNoteDao();
        List<ARNote> arNoteList = arNoteDao.queryARNotesByUserId(SpacetimeApplication.getInstance().getCurrentUser().getUserId());
        ARNoteListAdapter adapter = new ARNoteListAdapter(getContext(), arNoteList);
        arNoteListView.setAdapter(adapter);
    }


    /**
     * 截屏并显示动画
     * @return 截屏图片BitMap
     */
    public Bitmap getScreenshot() {
        View view = getActivity().getWindow().getDecorView();
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        ImageView img = fragView.findViewById(R.id.img_screenshot);
        img.setImageBitmap(bitmap);
        // 截图缩放移动到左下角
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(img, "translationX", 0f,-60f,-120f,-180f,-240f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(img, "translationY", 0f,120f,240f,360f,480f);
        ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(img, "scaleX", 1f, 0.4f);
        ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(img, "scaleY", 1f, 0.4f);
        AnimatorSet moveAnimatorSet = new AnimatorSet();
        moveAnimatorSet.playTogether(animatorX, animatorY, animatorScaleX, animatorScaleY);
        moveAnimatorSet.setDuration(1000);
        // 移动到左下角后向左移动并消失
        ObjectAnimator animatorDisappearAlpha = ObjectAnimator.ofFloat(img, "alpha", 1f, 1f, 1f, 0.5f, 0f);
        animatorDisappearAlpha.setDuration(1500);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(moveAnimatorSet, animatorDisappearAlpha);
        animatorSet.start();
        // JPEG压缩
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] bytes = baos.toByteArray();
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        //保存到数据库
        ARNote arNote = new ARNote();
        arNote.setTitle("测试");
        arNote.setImg(bytes);
        arNote.setUserId(SpacetimeApplication.getInstance().getCurrentUser().getUserId());
        ARNoteDao arNoteDao = SpacetimeApplication.getInstance().getDatabase().getARNoteDao();
        arNoteDao.insertARNotes(arNote);
        return bitmap;
    }
}
