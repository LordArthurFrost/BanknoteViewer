package edu.zntu.ukrainianbanknoteviewer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import edu.zntu.ukrainianbanknoteviewer.R;
import edu.zntu.ukrainianbanknoteviewer.managers.FragmentHelper;


public class FragmentEnter extends Fragment implements View.OnClickListener
{
    Button btnStart, btnInfo, btnExit;
    View view;
    FragmentInfo fragmentInfo;
    FragmentShowAndSearch fragmentShowAndSearch;


    public FragmentEnter()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.view = inflater.inflate(R.layout.fragment_enter, container, false);
        btnStart = view.findViewById(R.id.btnmainstart);
        btnInfo = view.findViewById(R.id.btnmaininfo);
        btnExit = view.findViewById(R.id.btnmainexit);
        btnStart.setOnClickListener(this);
        btnInfo.setOnClickListener(this);
        btnExit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnmainstart:
                fragmentShowAndSearch = new FragmentShowAndSearch();
                FragmentHelper.openFragment(fragmentShowAndSearch);
                break;
            case R.id.btnmaininfo:
                fragmentInfo = new FragmentInfo();
                FragmentHelper.openFragment(fragmentInfo);
                break;
            case R.id.btnmainexit:
                getActivity().finish();
                break;
        }
    }
}