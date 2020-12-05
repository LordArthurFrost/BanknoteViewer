package edu.zntu.ukrainianbanknoteviewer.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.jetbrains.annotations.NotNull;

import edu.zntu.ukrainianbanknoteviewer.R;
import edu.zntu.ukrainianbanknoteviewer.managers.ImageManager;

public class FragmentInfo extends Fragment
{
    private View view;
    private ImageView imageView;

    public FragmentInfo()
    {
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (view == null)
        {
            this.view = inflater.inflate(R.layout.fragment_info, container, false);
            imageView = view.findViewById(R.id.Me);
            imageView.setImageDrawable(ImageManager.getImage("Emperor"));
        }
        return view;
    }
}