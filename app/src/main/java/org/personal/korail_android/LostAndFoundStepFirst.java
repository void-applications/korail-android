package org.personal.korail_android;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LostAndFoundStepFirst extends Fragment {

    private String title;
    private int page;

    public static LostAndFoundStepFirst newInstance(int page, String title) {

        LostAndFoundStepFirst stepFirst = new LostAndFoundStepFirst();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        stepFirst.setArguments(args);
        return stepFirst;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lost_and_found_step_first, container, false);
//        EditText tvLabel = (EditText) view.findViewById(R.id.editText);
//        tvLabel.setText(page + " -- " + title);
        return view;
    }



}
