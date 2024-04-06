package edu.whu.spacetime.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Objects;
import java.util.logging.Handler;
import edu.whu.spacetime.R;

public class ImportDialog extends Dialog implements View.OnClickListener {

    private View view;
    private Handler handler;
    public ImageButton import_pdf, import_ppt, import_audio;

    public interface IOnChooseFileListener{
        public void OnChooseFileListener(String type);
    }

    public IOnChooseFileListener onChooseFileListener;
    public void setOnChooseFileListener(IOnChooseFileListener listener) {
        this.onChooseFileListener = listener;
    }
    public ImportDialog(@NonNull Context context) {
        super(context);
        view = View.inflate(context, R.layout.import_dialog_layout, null);
        this.setContentView(view);
        this.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        Window window=this.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        this.handler = handler;
        initView();
    }

    private void initView() {
        import_pdf = view.findViewById(R.id.btn_import_pdf);
        import_ppt = view.findViewById(R.id.btn_import_ppt);
        import_audio = view.findViewById(R.id.btn_import_audio);

        import_pdf.setOnClickListener(this);
        import_ppt.setOnClickListener(this);
        import_audio.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_import_pdf) {
            onChooseFileListener.OnChooseFileListener("pdf");
            dismiss();
        }
        else if (id == R.id.btn_import_ppt) {
            onChooseFileListener.OnChooseFileListener("ppt");
            dismiss();
        }
        else if (id == R.id.btn_import_audio) {
            onChooseFileListener.OnChooseFileListener("audio");
            dismiss();
        }
    }


}
