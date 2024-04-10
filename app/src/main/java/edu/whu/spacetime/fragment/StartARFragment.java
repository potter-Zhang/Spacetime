package edu.whu.spacetime.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.whu.spacetime.R;
import edu.whu.spacetime.activity.HelloArActivity;
import edu.whu.spacetime.activity.MainActivity;

public class StartARFragment extends Fragment {
    private View fragView;

    public StartARFragment(){
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragView = inflater.inflate(R.layout.fragment_ar_start,container,false);
        setStartARButtonListener(fragView);
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
        return bitmap;
    }
}
