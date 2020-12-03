package edu.zntu.ukrainianbanknoteviewer.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.zntu.ukrainianbanknoteviewer.ConstantsBanknote;
import edu.zntu.ukrainianbanknoteviewer.R;
import edu.zntu.ukrainianbanknoteviewer.ShortBanknoteInfo;
import edu.zntu.ukrainianbanknoteviewer.managers.CatalogueListAdapter;
import edu.zntu.ukrainianbanknoteviewer.managers.DataBaseManager;
import edu.zntu.ukrainianbanknoteviewer.managers.FragmentHelper;


public class FragmentCatalogue extends Fragment implements View.OnClickListener
{
    private View view;
    private Boolean isDenomination = true;
    private ListView listView;
    private Button switchButton;
    private AdapterView.OnItemClickListener onItemClickListener;
    private FragmentCatalogueItems fragmentCatalogueItems;
    private String[] denominationYear;
    private ArrayList<String> catalogueList;
    private Map<Integer, String> searchMap;
    private List<ShortBanknoteInfo> shortBanknoteInfoList;

    public FragmentCatalogue()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (view == null)
        {
            this.view = inflater.inflate(R.layout.fragment_catalogue, container, false);
            listView = view.findViewById(R.id.lvcatalogue);
            switchButton = view.findViewById(R.id.btncatalogueswitch);

            shortBanknoteInfoList = new ArrayList<>();
            catalogueList = new ArrayList<>();
            searchMap = new HashMap<>();
            denominationYear = new String[]{"Номінал", "Рік друку"};


            switchButton.setText(denominationYear[1]);
            switchButton.setOnClickListener(this);

            catalogueListManager();

            fragmentCatalogueItems = new FragmentCatalogueItems();
        }

        return view;
    }


    public void setCatalogueList(ArrayList<String> catalogueList)
    {
        CatalogueListAdapter catalogueListAdapter = new CatalogueListAdapter(getContext(), R.layout.listview_catalogue, catalogueList);
        listView.setAdapter(catalogueListAdapter);

        onItemClickListener = (parent, view, position, id) -> {

            String selected = (String) parent.getItemAtPosition(position);
            Log.d("Catalogue", selected);

            if (isDenomination)
            {
                searchMap.put(ConstantsBanknote.DENOMINATION, selected);
            } else
            {
                searchMap.put(ConstantsBanknote.PRINTYEAR, selected);
            }

            DataBaseManager.fillShortBanknoteInfoList(shortBanknoteInfoList, searchMap, () -> requireActivity().runOnUiThread(() -> fragmentCatalogueItems.setContent(shortBanknoteInfoList)));
            FragmentHelper.openFragment(fragmentCatalogueItems);
        };

        listView.setOnItemClickListener(onItemClickListener);
    }


    public void catalogueListManager()
    {
        try
        {
            DataBaseManager.getDenominationOrPrintYear(catalogueList, isDenomination, () -> requireActivity().runOnUiThread(() -> setCatalogueList(catalogueList)));
            catalogueList.clear();
        } catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.btncatalogueswitch)
        {
            if (isDenomination)
            {
                switchButton.setText(denominationYear[0]);
            } else
            {
                switchButton.setText(denominationYear[1]);
            }
            isDenomination = !isDenomination;
            catalogueListManager();
        }
    }
}