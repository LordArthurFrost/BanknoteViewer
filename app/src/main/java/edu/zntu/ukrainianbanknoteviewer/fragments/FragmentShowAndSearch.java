package edu.zntu.ukrainianbanknoteviewer.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.zntu.ukrainianbanknoteviewer.R;


public class FragmentShowAndSearch extends Fragment
{
    private FragmentShowBanknote fragmentShowBanknote;
    private View view;

    public FragmentShowAndSearch()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.view =  inflater.inflate(R.layout.fragment_show_and_search, container, false);
        return view;
    }
}
