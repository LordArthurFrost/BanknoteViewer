package edu.zntu.ukrainianbanknoteviewer.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.zntu.ukrainianbanknoteviewer.R;
import edu.zntu.ukrainianbanknoteviewer.ShortBanknoteInfo;
import edu.zntu.ukrainianbanknoteviewer.managers.DataBaseManager;
import edu.zntu.ukrainianbanknoteviewer.managers.ShortBanknoteInfoAdapter;


public class FragmentShowAndSearch extends Fragment implements View.OnClickListener
{
    private FragmentShowBanknote fragmentShowBanknote;
    private View view;
    private List<ShortBanknoteInfo> shortBanknoteInfoList;
    Map<Integer, String> searchmap;
    Button btnFilters, btnSearch;
    Cursor cursor;
    SimpleCursorAdapter simpleCursorAdapter;
    AutoCompleteTextView autoCompleteTextView;
    ListView listView;
    ShortBanknoteInfoAdapter shortBanknoteInfoAdapter;

    public FragmentShowAndSearch()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.view = inflater.inflate(R.layout.fragment_show_and_search, container, false);
        shortBanknoteInfoList = new ArrayList<>();
        searchmap = new HashMap<>();
        btnFilters = view.findViewById(R.id.btnsearchfilter);
        btnSearch = view.findViewById(R.id.btnsearchstart);
        btnSearch.setOnClickListener(this);
        btnFilters.setOnClickListener(this);



        initialDBtest();
        DataBaseManager.fillShortBanknoteInfoList(shortBanknoteInfoList, searchmap, () -> requireActivity().runOnUiThread(() -> something()));
        ArrayList<String> data = new ArrayList<>();
        DataBaseManager.getAutocompleteEditText(data, () -> requireActivity().runOnUiThread(() -> setAutoCompleteView(data)));


        //DataBaseManager.fillShortBanknoteInfoList(shortBanknoteInfoList, searchmap, () -> requireActivity().runOnUiThread(() -> test()));


       /* DataBaseManager.fillShortBanknoteInfoList(shortBanknoteInfoList, searchmap, new Runnable()
        {
            @Override
            public void run()
            {
                requireActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        test();
                    }
                });
            }
        });*/

        return view;
    }

    public void something()
    {
        listView = view.findViewById(R.id.listshowandsearch);
        shortBanknoteInfoAdapter = new ShortBanknoteInfoAdapter(getContext(), R.layout.listview_banknote, shortBanknoteInfoList);
        listView.setAdapter(shortBanknoteInfoAdapter);
    }

    public void initialDBtest()
    {
        searchmap.put(0, "1");
    }

    //  shortBanknoteInfoAdapter = new ShortBanknoteInfoAdapter(getContext(), R.layout.listview_banknote, )


    //TODO
    public void setAutoCompleteView(ArrayList<String> data)
    {

        this.autoCompleteTextView = view.findViewById(R.id.autoCompleteEditText);
        autoCompleteTextView.setAdapter(new ArrayAdapter<>(getContext(), R.layout.custom_list_item, R.id.text_view_list_item, data));
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                   //
                }
                return false;
            }
        });

    }

    /*public void test()
    {
        cursor = DataBaseManager.check();
        String[] headers = new String[]{"denomination", "printYear"};
        simpleCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.listview_banknote, cursor, headers, new int[]{R.id.lvdenomination, R.id.lvprintyer}, 0);
        listView.setAdapter(simpleCursorAdapter);

    }*/


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnsearchstart:
                //DataBaseManager.check();
                break;

            case R.id.btnsearchfilter:
                break;
        }
    }
}
