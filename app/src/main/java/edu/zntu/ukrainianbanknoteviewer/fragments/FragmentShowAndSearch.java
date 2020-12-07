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


    public FragmentShowAndSearch()
    {
    }


    public void setSettingsMap(Map<Integer, String> settingsMap)
    {
        this.settingsMap = settingsMap;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_show_and_search, container, false);
            shortBanknoteInfoList = new ArrayList<>();
            searchMap = new HashMap<>();
            settingsMap = new HashMap<>();
            btnFilters = view.findViewById(R.id.btnsearchfilter);
            btnFilters.setOnClickListener(this);
            ArrayList<String> data = new ArrayList<>();

            DataBaseManager.getAutocompleteEditText(data, () -> requireActivity().runOnUiThread(() -> setAutoCompleteView(data)));
        }
        return view;
    }


    public void clearInput()
    {
        autoCompleteTextView.setText("");
        settingsMap.clear();
        startSearch(searchMap);
    }


    public void startSearch(Map<Integer, String> searchMap)
    {
        autoCompleteTextView.setText("");
        this.searchMap = searchMap;
        DataBaseManager.fillShortBanknoteInfoList(shortBanknoteInfoList, searchMap, () -> requireActivity().runOnUiThread(() -> setBanknoteList()));
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
        autoCompleteTextView.setThreshold(2);

        autoCompleteTextView.setOnEditorActionListener((v, actionId, event) -> {
            String[] getSearch, denominationCheck;
            String result;
            StringBuilder calculateSearch = new StringBuilder();
            if ((actionId == EditorInfo.IME_ACTION_PREVIOUS))
            {
                result = autoCompleteTextView.getText().toString();

                if (result.equals("Egg") || result.equals("egg"))
                {
                    searchMap.put(ConstantsBanknote.IDINFO, ConstantsBanknote.EGG_ID);

                    startSearch(searchMap);

                    return true;
                }

                Log.d("FragmentShow", result);

                getSearch = result.split("\\s+([A-і])\\w+");

                denominationCheck = result.split("[^Г,К]");
                for (String s : denominationCheck)
                {
                    denominationCheck[0] += s;
                }

                if (!(denominationCheck.length == 0))
                {
                    if (denominationCheck[0].equals("Г") || denominationCheck[0].equals("г"))
                    {
                        searchMap.put(ConstantsBanknote.ISBANKNOTE, "1");
                    }
                    if (denominationCheck[0].equals("К") || denominationCheck[0].equals("к"))
                    {
                        searchMap.put(ConstantsBanknote.ISBANKNOTE, "0");
                    }
                }

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
                            if (getSearch[0].equals("1000"))
                            {
                                searchMap.put(ConstantsBanknote.DENOMINATION, getSearch[0]);
                                break;
                            }
                            searchMap.put(ConstantsBanknote.PRINTYEAR, getSearch[0]);
                        } else
                        {
                            searchMap.put(ConstantsBanknote.DENOMINATION, getSearch[0]);
                        }

                        break;
                    case 2:
                        if (getSearch[0].matches("\\d{4}"))
                        {
                            if (getSearch[0].equals("1000"))
                            {
                                searchMap.put(ConstantsBanknote.PRINTYEAR, getSearch[1]);
                                searchMap.put(ConstantsBanknote.DENOMINATION, getSearch[0]);
                                break;
                            }
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

        fragmentFilterDialog = (FragmentFilterDialog) FragmentFilterDialog.newInstance(settingsMap);

        if (v.getId() == R.id.btnsearchfilter)
        {
            fragmentFilterDialog.show(this.getChildFragmentManager(), "tag");


        }
    }
}
