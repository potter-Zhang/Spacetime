package edu.whu.spacetime.adapter;

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

import java.time.LocalDateTime;
import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.domain.ARNote;

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

    private Context context;
    private List<ARNote> arNoteList;

    public ARNoteListAdapter(Context context, List<ARNote> arNoteList) {
        this.arNoteList = arNoteList;
        this.context = context;
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
        byte[] imgBytes = arNote.getImg();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
        holder.mTVTitle.setText(arNote.getTitle());
        holder.mARImg.setImageBitmap(bitmap);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = (int) (450 + Math.random() * 300); // 设置item的高度不同从而实现瀑布式布局
        holder.itemView.setLayoutParams(layoutParams);
        LocalDateTime createTime = arNote.getCreateTime();
        int year = createTime.getYear();
        StringBuilder timeDisplay = new StringBuilder();
        if (LocalDateTime.now().getYear() - year >= 1) {
            // 超过一年才显示年份
            timeDisplay.append(year).append("年");
        }
        timeDisplay.append(createTime.getMonthValue()).append("月").append(createTime.getDayOfMonth()).append("日");
        holder.mTVTime.setText(timeDisplay.toString());
    }

    @Override
    public int getItemCount() {
        return arNoteList.size();
    }
}
