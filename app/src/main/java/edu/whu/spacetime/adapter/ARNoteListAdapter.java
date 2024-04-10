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

import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.domain.ARNote;

public class ARNoteListAdapter extends RecyclerView.Adapter<ARNoteListAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mARImg;
        public TextView mTVTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mARImg = itemView.findViewById(R.id.img_arnote);
            mTVTitle = itemView.findViewById(R.id.tv_arnote_title);
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
        holder.mTVTitle.setText(arNote.getTitle());
        byte[] imgBytes = arNote.getImg();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
        holder.mARImg.setImageBitmap(bitmap);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = (int) (450 + Math.random() * 300); // 设置item的高度，可以根据需要自行调整
        holder.itemView.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return arNoteList.size();
    }
}
