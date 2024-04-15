package edu.whu.spacetime.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.domain.ARModel;

public class ModelListAdapter extends RecyclerView.Adapter<ModelListAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgModel;

        public TextView tvModelName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgModel = itemView.findViewById(R.id.img_model);
            tvModelName = itemView.findViewById(R.id.tv_model_name);
        }
    }

    public interface OnModelChosenListener {
        void OnModelChosen(ARModel arModel);
    }

    private Context context;

    private List<ARModel> arModelList;

    private OnModelChosenListener onModelChosenListener;

    public ModelListAdapter(Context context, List<ARModel> arModelList) {
        this.context = context;
        this.arModelList = arModelList;
    }

    public void setOnModelChosenListener(OnModelChosenListener onModelChosenListener) {
        this.onModelChosenListener = onModelChosenListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_model_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ARModel arModel = arModelList.get(position);
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(arModel.getPreviewImgPath(), "drawable", context.getPackageName());
        holder.imgModel.setImageResource(resourceId);
        holder.tvModelName.setText(arModel.getName());
        holder.itemView.setOnClickListener(v -> {
            if (this.onModelChosenListener != null) {
                onModelChosenListener.OnModelChosen(arModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arModelList.size();
    }
}
