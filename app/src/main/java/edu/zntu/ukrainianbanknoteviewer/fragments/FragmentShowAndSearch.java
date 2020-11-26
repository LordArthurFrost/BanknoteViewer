package edu.zntu.ukrainianbanknoteviewer.fragments;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;

import edu.zntu.ukrainianbanknoteviewer.MainActivity;
import edu.zntu.ukrainianbanknoteviewer.R;
import edu.zntu.ukrainianbanknoteviewer.managers.DataBaseManager;


public class FragmentShowAndSearch extends Fragment implements View.OnClickListener
{
    private FragmentShowBanknote fragmentShowBanknote;
    private View view;
    Button btnFilters, btnSearch;
    DataBaseManager dataBaseManager;
    Cursor cursor;
    SimpleCursorAdapter simpleCursorAdapter;
    ListView listView;

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
        listView = view.findViewById(R.id.listshowandsearch);
        btnSearch.setOnClickListener(this);
        btnFilters.setOnClickListener(this);
        return view;

    }

    public void test()
    {
        cursor = DataBaseManager.check();
        String[] headers = new String[]{"denomination", "printYear"};
        simpleCursorAdapter = new SimpleCursorAdapter(getContext(),android.R.layout.two_line_list_item, cursor, headers, new int[]{android.R.id.text1, android.R.id.text2},0);
        listView.setAdapter(simpleCursorAdapter);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnsearchstart:
                test();
                //DataBaseManager.check();
                break;

            case R.id.btnsearchfilter:
                break;
        }
    }
}
