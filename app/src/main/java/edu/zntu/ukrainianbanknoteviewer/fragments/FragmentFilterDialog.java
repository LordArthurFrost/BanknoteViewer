package edu.zntu.ukrainianbanknoteviewer.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.zntu.ukrainianbanknoteviewer.R;


public class FragmentFilterDialog extends DialogFragment
{
    private View view;

    public FragmentFilterDialog()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        this.view = inflater.inflate(R.layout.fragment_filter_dialog, null);

        return view;
    }
}