package edu.whu.spacetime.widget;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.AttachPopupView;

import edu.whu.spacetime.R;

public class PopupMenu extends AttachPopupView {
    public PopupMenu(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.widget_popup_menu;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
    }
}
