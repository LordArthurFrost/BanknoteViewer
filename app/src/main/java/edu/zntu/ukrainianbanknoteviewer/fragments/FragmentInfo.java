package edu.zntu.ukrainianbanknoteviewer.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.zntu.ukrainianbanknoteviewer.R;

public class FragmentInfo extends Fragment
{
    //private int pageNumber;
    private View view;


   /* public static FragmentInfo newInstance(int page)
    {
        FragmentInfo fragment = new FragmentInfo();
        Bundle args = new Bundle();
        args.putInt("num", page);
        fragment.setArguments(args);
        return fragment;
    }*/

    public FragmentInfo()
    {
    }

   /* @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments() != null ? getArguments().getInt("num") : 1;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.view = inflater.inflate(R.layout.fragment_info, container, false);
        return view;
    }
}