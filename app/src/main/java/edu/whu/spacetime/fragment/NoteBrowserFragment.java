package edu.whu.spacetime.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.adapter.NoteListAdapter;
import edu.whu.spacetime.domain.Note;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteBrowserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteBrowserFragment extends Fragment {
    private static final String ARG_NOTEBOOK = "notebookId";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NoteBrowserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment NoteBrowserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoteBrowserFragment newInstance(int param1) {
        NoteBrowserFragment fragment = new NoteBrowserFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NOTEBOOK, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_NOTEBOOK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_note_browser, container, false);
        ListView noteListView = fragmentView.findViewById(R.id.list_note);
        List<Note> noteList = new ArrayList<>();
        noteList.add(new Note("测试1", 0, 0, "测试内容", LocalDateTime.now()));
        noteList.add(new Note("测试2", 0, 0, "测试内容", LocalDateTime.now()));
        NoteListAdapter listAdapter = new NoteListAdapter(getContext(), R.layout.item_note_list, noteList);
        noteListView.setAdapter(listAdapter);
        return fragmentView;
    }
}