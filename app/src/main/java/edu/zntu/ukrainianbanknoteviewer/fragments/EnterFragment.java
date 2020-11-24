package edu.zntu.ukrainianbanknoteviewer.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.zntu.ukrainianbanknoteviewer.R;


public class EnterFragment extends Fragment
{

    public EnterFragment()
    {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v =  inflater.inflate(R.layout.fragment_enter, container, false);
        return v;
    }
}