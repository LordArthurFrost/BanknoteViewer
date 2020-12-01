package edu.zntu.ukrainianbanknoteviewer.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

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
    private Map<Integer, String> searchmap, transfermap, settingsMap;
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
        this.view = inflater.inflate(R.layout.fragment_show_and_search, container, false);
        shortBanknoteInfoList = new ArrayList<>();
        searchmap = new HashMap<>();
        settingsMap = new HashMap<>();
        btnFilters = view.findViewById(R.id.btnsearchfilter);
        btnFilters.setOnClickListener(this);

        ArrayList<String> dbsize = new ArrayList<>();

        DataBaseManager.getSize(dbsize, () -> requireActivity().runOnUiThread(() -> {
            arrayListSizetoString(dbsize);

        }));


        ArrayList<String> data = new ArrayList<>();
        DataBaseManager.getAutocompleteEditText(data, () -> requireActivity().runOnUiThread(() -> setAutoCompleteView(data)));


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


    public void arrayListSizetoString(ArrayList<String> arrayList)
    {
        size = new String[arrayList.size() + 1];

        size[0] = "";
        for (int i = 0; i < arrayList.size(); ++i)
        {
            size[i + 1] = arrayList.get(i);
        }
    }


    public void startSearch(Map<Integer, String> searchmap)
    {
        this.searchmap = searchmap;
        DataBaseManager.fillShortBanknoteInfoList(shortBanknoteInfoList, searchmap, () -> requireActivity().runOnUiThread(() -> setBanknoteList()));
    }


    public void setBanknoteList()
    {
        transfermap = new HashMap<>();
        Log.d("FragmentSearch", "setBanknoteList()");
        listView = view.findViewById(R.id.listshowandsearch);
        shortBanknoteInfoAdapter = new ShortBanknoteInfoAdapter(getContext(), R.layout.listview_banknote, shortBanknoteInfoList);
        listView.setAdapter(shortBanknoteInfoAdapter);
        Log.d("FragmentSearch", "before onItemClickListener");
        onItemClickListener = new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                ShortBanknoteInfo selectedBanknote = (ShortBanknoteInfo) parent.getItemAtPosition(position);

                transfermap.put(ConstantsBanknote.IDINFO, selectedBanknote.getImageAbver());


                DataBaseManager.searchToShowTransfer(transfermap, () -> requireActivity().runOnUiThread(() -> fragmentShowBanknote.setAdditionalContent(transfermap)));


                Log.d("FragmentSearch", selectedBanknote.getDenomination() + " " + selectedBanknote.getPrintYear());

                fragmentShowBanknote = new FragmentShowBanknote();
                fragmentShowBanknote.setBanknoteInfo(selectedBanknote);
                DataBaseManager.searchToShowTransfer(transfermap, () -> requireActivity().runOnUiThread(() -> fragmentShowBanknote.setAdditionalContent(transfermap)));
                FragmentHelper.openFragment(fragmentShowBanknote);
            }
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
                            searchmap.put(ConstantsBanknote.PRINTYEAR, getSearch[0]);
                        } else
                        {
                            searchmap.put(ConstantsBanknote.DENOMINATION, getSearch[0]);
                        }

                        break;
                    case 2:
                        if (getSearch[0].matches("\\d{4}"))
                        {
                            searchmap.put(ConstantsBanknote.PRINTYEAR, getSearch[0]);
                            searchmap.put(ConstantsBanknote.DENOMINATION, getSearch[1]);
                        } else
                        {
                            searchmap.put(ConstantsBanknote.PRINTYEAR, getSearch[1]);
                            searchmap.put(ConstantsBanknote.DENOMINATION, getSearch[0]);
                        }
                        break;
                }

                startSearch(searchmap);

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
