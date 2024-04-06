package edu.whu.spacetime.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.whu.spacetime.R;
import edu.whu.spacetime.activity.HelloArActivity;
import edu.whu.spacetime.activity.MainActivity;

public class StartARFragment extends Fragment {
    public StartARFragment(){
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_ar_start,container,false);
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
    }
}
