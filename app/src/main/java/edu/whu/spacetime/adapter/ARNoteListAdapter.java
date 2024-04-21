package edu.whu.spacetime.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lxj.xpopup.XPopup;
import com.xuexiang.xui.widget.imageview.photoview.PhotoView;
import com.xuexiang.xui.widget.imageview.photoview.PhotoViewAttacher;

import java.time.LocalDateTime;
import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.SpacetimeApplication;
import edu.whu.spacetime.dao.ARNoteDao;
import edu.whu.spacetime.domain.ARNote;
import edu.whu.spacetime.widget.ARNotePopup;

public class ARNoteListAdapter extends RecyclerView.Adapter<ARNoteListAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mARImg;
        public TextView mTVTitle;

        public TextView mTVTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mARImg = itemView.findViewById(R.id.img_arnote);
            mTVTitle = itemView.findViewById(R.id.tv_arnote_title);
            mTVTime = itemView.findViewById(R.id.tv_arnote_time);
        }
    }

    /**
     * 笔记数量变化监听器
     */
    public interface OnSizeChangedListener {
        void onSizeChanged(int size);
    }

    private Context context;
    private List<ARNote> arNoteList;

    private ARNoteDao arNoteDao;

    private OnSizeChangedListener onSizeChangedListener;

    public ARNoteListAdapter(Context context, List<ARNote> arNoteList) {
        this.arNoteList = arNoteList;
        this.context = context;
        this.arNoteDao = SpacetimeApplication.getInstance().getDatabase().getARNoteDao();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_arnote_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ARNote arNote = this.arNoteList.get(position);
        // 将Blob显示到ImageView上
        byte[] imgBytes = arNote.getImg();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
        holder.mARImg.setImageBitmap(bitmap);
        holder.mTVTitle.setText(arNote.getTitle());

        // 设置item的高度不同从而实现瀑布式布局
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = (int) (450 + Math.random() * 300);
        holder.itemView.setLayoutParams(layoutParams);

        // 设置显示时间
        LocalDateTime createTime = arNote.getCreateTime();
        int year = createTime.getYear();
        StringBuilder timeDisplay = new StringBuilder();
        if (LocalDateTime.now().getYear() - year >= 1) {
            // 超过一年才显示年份
            timeDisplay.append(year).append("年");
        }
        timeDisplay.append(createTime.getMonthValue()).append("月").append(createTime.getDayOfMonth()).append("日");
        holder.mTVTime.setText(timeDisplay.toString());

        // 长按弹出编辑菜单
        holder.itemView.setOnLongClickListener(v -> {
            ARNotePopup popup = new ARNotePopup(v.getContext());
            // 删除事件
            popup.setOnDeleteListener(() -> {
                this.remove(arNote);
                popup.dismiss();
            });
            // 重命名事件
            popup.setOnEditConfirmListener(text -> {
                this.rename(arNote, text);
                popup.dismiss();
            });
            new XPopup.Builder(v.getContext())
                    .isDestroyOnDismiss(true)
                    .atView(holder.mARImg)
                    .asCustom(popup)
                    .show();
            return true;
        });

        //点击放大图片
        holder.itemView.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(v.getContext());
            PhotoView photoView = new PhotoView(v.getContext());
            photoView.setImageBitmap(bitmap);
            dialog.setContentView(photoView);
            //将dialog周围的白块设置为透明
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();
            // 单击退出
            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    dialog.cancel();
                }

                @Override
                public void onOutsidePhotoTap() {
                    dialog.cancel();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return arNoteList.size();
    }

    public void setOnSizeChangedListener(OnSizeChangedListener onSizeChangedListener) {
        this.onSizeChangedListener = onSizeChangedListener;
    }

    /**
     * 从Adapter中移除对应的ARNote
     */
    public void remove(ARNote arNote) {
        int position = this.arNoteList.indexOf(arNote);
        this.arNoteList.remove(position);
        this.notifyItemRemoved(position);
        this.arNoteDao.deleteARNotes(arNote);
        if (this.onSizeChangedListener != null) {
            onSizeChangedListener.onSizeChanged(getItemCount());
        }
    }

    public void rename(ARNote arNote, String newTitle) {
        int position = this.arNoteList.indexOf(arNote);
        arNote.setTitle(newTitle);
        this.notifyItemChanged(position);
        this.arNoteDao.updateARNotes(arNote);
    }

    /**
     * 添加ARNote
     */
    public void add(ARNote arNote) {
        this.arNoteList.add(arNote);
        this.notifyItemInserted(this.arNoteList.size() - 1);
        if (this.onSizeChangedListener != null) {
            onSizeChangedListener.onSizeChanged(getItemCount());
        }
    }
}
