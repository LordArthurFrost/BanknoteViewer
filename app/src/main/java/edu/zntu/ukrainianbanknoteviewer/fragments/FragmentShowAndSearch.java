package edu.zntu.ukrainianbanknoteviewer.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.zntu.ukrainianbanknoteviewer.ConstantsBanknote;
import edu.zntu.ukrainianbanknoteviewer.R;
import edu.zntu.ukrainianbanknoteviewer.ShortBanknoteInfo;
import edu.zntu.ukrainianbanknoteviewer.managers.DataBaseManager;
import edu.zntu.ukrainianbanknoteviewer.managers.FragmentHelper;
import edu.zntu.ukrainianbanknoteviewer.managers.ShortBanknoteInfoAdapter;


public class FragmentShowAndSearch extends Fragment implements View.OnClickListener
{
    private FragmentShowBanknote fragmentShowBanknote;
    private View view;
    private List<ShortBanknoteInfo> shortBanknoteInfoList;
    private Map<Integer, String> searchMap, transferMap, settingsMap;
    private Button btnFilters;
    private AutoCompleteTextView autoCompleteTextView;
    private ListView listView;
    private FragmentFilterDialog fragmentFilterDialog;
    private ShortBanknoteInfoAdapter shortBanknoteInfoAdapter;
    private AdapterView.OnItemClickListener onItemClickListener;
    private static String[] size;


    public FragmentShowAndSearch()
    {
        // Required empty public constructor
    }


    public void setSettingsMap(Map<Integer, String> settingsMap)
    {
        this.settingsMap = settingsMap;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if(view == null)
        {
            view = inflater.inflate(R.layout.fragment_show_and_search, container, false);
            shortBanknoteInfoList = new ArrayList<>();
            searchMap = new HashMap<>();
            settingsMap = new HashMap<>();
            btnFilters = view.findViewById(R.id.btnsearchfilter);
            btnFilters.setOnClickListener(this);
            ArrayList<String> data = new ArrayList<>();
            ArrayList<String> dbsize = new ArrayList<>();

            try
            {
                DataBaseManager.getSize(dbsize, () -> requireActivity().runOnUiThread(() -> arrayListSizetoString(dbsize)));

                DataBaseManager.getAutocompleteEditText(data, () -> requireActivity().runOnUiThread(() -> setAutoCompleteView(data)));

            } catch (Exception e)
            {
                e.printStackTrace();
            }

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

        }
        return view;
    }


    public void arrayListSizetoString(ArrayList<String> arrayList)
    {
        size = new String[arrayList.size() + 1];

        size[0] = "";
        for (int i = 0; i < arrayList.size(); ++i)
        {
            size[i + 1] = arrayList.get(i);
        }
    }


    public void clearInput()
    {
        autoCompleteTextView.setText("");
        settingsMap.clear();
        startSearch(searchMap);
    }


    public void startSearch(Map<Integer, String> searchmap)
    {
        autoCompleteTextView.setText("");
        this.searchMap = searchmap;
        DataBaseManager.fillShortBanknoteInfoList(shortBanknoteInfoList, searchmap, () -> requireActivity().runOnUiThread(() -> setBanknoteList()));
    }


    public void setBanknoteList()
    {
        transferMap = new HashMap<>();
        Log.d("FragmentSearch", "setBanknoteList()");
        listView = view.findViewById(R.id.listshowandsearch);
        shortBanknoteInfoAdapter = new ShortBanknoteInfoAdapter(getContext(), R.layout.listview_banknote, shortBanknoteInfoList);
        listView.setAdapter(shortBanknoteInfoAdapter);
        Log.d("FragmentSearch", "before onItemClickListener");
        onItemClickListener = (parent, view, position, id) -> {

            ShortBanknoteInfo selectedBanknote = (ShortBanknoteInfo) parent.getItemAtPosition(position);

            transferMap.put(ConstantsBanknote.IDINFO, selectedBanknote.getImageAbver());


            Log.d("FragmentSearch", selectedBanknote.getDenomination() + " " + selectedBanknote.getPrintYear());

            fragmentShowBanknote = new FragmentShowBanknote();
            fragmentShowBanknote.setBanknoteInfo(selectedBanknote);

            DataBaseManager.searchToShowTransfer(transferMap, () -> requireActivity().runOnUiThread(() -> fragmentShowBanknote.setAdditionalContent(transferMap)));

            FragmentHelper.openFragment(fragmentShowBanknote);
        };
        listView.setOnItemClickListener(onItemClickListener);
    }


    public void setAutoCompleteView(ArrayList<String> data)
    {

        this.autoCompleteTextView = view.findViewById(R.id.autoCompleteEditText);
        autoCompleteTextView.setAdapter(new ArrayAdapter<>(getContext(), R.layout.custom_list_item, R.id.text_view_list_item, data));
        autoCompleteTextView.setThreshold(1);

        autoCompleteTextView.setOnEditorActionListener((v, actionId, event) -> {
            String[] getSearch;
            String result;
            StringBuilder calculateSearch = new StringBuilder();
            if ((actionId == EditorInfo.IME_ACTION_PREVIOUS))
            {
                result = autoCompleteTextView.getText().toString();
                Log.d("FragmentShow", result);

                getSearch = result.split("\\s+([A-і])\\w+");
                for (String string : getSearch)
                {
                    calculateSearch.append(string);
                }
                getSearch = calculateSearch.toString().split("\\s");

                switch (getSearch.length)
                {
                    case 1:
                        if (getSearch[0].matches("\\d{4}"))
                        {
                            searchMap.put(ConstantsBanknote.PRINTYEAR, getSearch[0]);
                        } else
                        {
                            searchMap.put(ConstantsBanknote.DENOMINATION, getSearch[0]);
                        }

                        break;
                    case 2:
                        if (getSearch[0].matches("\\d{4}"))
                        {
                            searchMap.put(ConstantsBanknote.PRINTYEAR, getSearch[0]);
                            searchMap.put(ConstantsBanknote.DENOMINATION, getSearch[1]);
                        } else
                        {
                            searchMap.put(ConstantsBanknote.PRINTYEAR, getSearch[1]);
                            searchMap.put(ConstantsBanknote.DENOMINATION, getSearch[0]);
                        }
                        break;
                }

                startSearch(searchMap);

                return true;
            }
            return false;
        });

    }


    @Override
    public void onClick(View v)
    {

        fragmentFilterDialog = (FragmentFilterDialog) FragmentFilterDialog.newInstance(settingsMap.getOrDefault(ConstantsBanknote.DENOMINATION, ""), settingsMap.getOrDefault(ConstantsBanknote.PRINTYEAR, ""), settingsMap.getOrDefault(ConstantsBanknote.DATE, ""), size, Integer.parseInt(settingsMap.getOrDefault(ConstantsBanknote.MEMORABLEPOSITION, "0")), Integer.parseInt(settingsMap.getOrDefault(ConstantsBanknote.SIZEPOSITION, "0")), Integer.parseInt(settingsMap.getOrDefault(ConstantsBanknote.TURNOVERPOSITION, "0")));

        if (v.getId() == R.id.btnsearchfilter)
        {
            fragmentFilterDialog.show(this.getChildFragmentManager(), "tag");


        }
    }
}
