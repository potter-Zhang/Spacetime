package edu.whu.spacetime.widget;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lxj.xpopup.core.BottomPopupView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.adapter.ModelListAdapter;
import edu.whu.spacetime.domain.ARModel;
import edu.whu.spacetime.service.ModelXmlParserService;

public class ModelChoosePopup extends BottomPopupView {
    private ModelListAdapter.OnModelChosenListener onModelChosenListener;

    private ModelListAdapter adapter;

    private RecyclerView modelListView;

    public ModelChoosePopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.widget_choose_model;
    }

    public void setOnModelChosenListener(ModelListAdapter.OnModelChosenListener onModelChosenListener) {
        this.onModelChosenListener = onModelChosenListener;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        modelListView = findViewById(R.id.list_model);
        // 横向RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        modelListView.setLayoutManager(linearLayoutManager);
        try {
            setModelList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setModelList() throws IOException {
        // 从xml文件中读取model信息
        InputStream xml= this.getClass().getClassLoader().getResourceAsStream("assets/models.xml");
        List<ARModel> modelList = ModelXmlParserService.getModels(xml);
        adapter = new ModelListAdapter(getContext(), modelList);
        modelListView.setAdapter(adapter);

        adapter.setOnModelChosenListener(arModel -> {
            if (this.onModelChosenListener != null) {
                onModelChosenListener.OnModelChosen(arModel);
            }
        });
    }
}
