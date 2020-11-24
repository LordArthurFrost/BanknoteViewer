package edu.zntu.ukrainianbanknoteviewer.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.zntu.ukrainianbanknoteviewer.R;


public class FragmentShowAndSearch extends Fragment implements View.OnClickListener
{
    private FragmentShowBanknote fragmentShowBanknote;
    private View view;
    Button btnFilters, btnSearch;

    public FragmentShowAndSearch()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.view = inflater.inflate(R.layout.fragment_show_and_search, container, false);
        btnFilters = view.findViewById(R.id.btnsearchfilter);
        btnSearch = view.findViewById(R.id.btnsearchstart);
        btnSearch.setOnClickListener(this);
        btnFilters.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnsearchstart:
                break;

            case R.id.btnsearchfilter:
                break;
        }
    }
}
