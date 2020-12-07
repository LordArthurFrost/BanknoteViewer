package edu.zntu.ukrainianbanknoteviewer.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

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
    private int isBanknote = 1;
    private ListView listView;
    private Button btnSwitchDenomination, btnSwitchType;
    private AdapterView.OnItemClickListener onItemClickListener;
    private FragmentCatalogueItems fragmentCatalogueItems;
    private String[] denominationYear, type;
    private ArrayList<String> catalogueList;
    private Map<Integer, String> searchMap;
    private List<ShortBanknoteInfo> shortBanknoteInfoList;

    public FragmentCatalogue()
    {
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (view == null)
        {
            this.view = inflater.inflate(R.layout.fragment_catalogue, container, false);

            listView = view.findViewById(R.id.lvcatalogue);
            btnSwitchDenomination = view.findViewById(R.id.btncataloguedenominyearswitch);
            btnSwitchType = view.findViewById(R.id.btncatalogueswitchtype);

            shortBanknoteInfoList = new ArrayList<>();
            catalogueList = new ArrayList<>();
            searchMap = new HashMap<>();
            denominationYear = new String[]{"Номінал", "Рік створення"};
            type = new String[]{"Банкнота", "Монета"};


            btnSwitchDenomination.setText(denominationYear[1]);
            btnSwitchType.setText(type[1]);
            btnSwitchDenomination.setOnClickListener(this);
            btnSwitchType.setOnClickListener(this);

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

            String[] result, denominationCheck;
            String selected = (String) parent.getItemAtPosition(position);

            result = selected.split("\\s[A-і]");
            if (isBanknote == 0)
            {
                denominationCheck = selected.split("[^Г,К]");
                for (String s : denominationCheck)
                {
                    denominationCheck[0] += s;
                }

                if (isDenomination && denominationCheck[0].equals("Г"))
                {
                    result[0] = String.valueOf(Integer.parseInt(result[0]) * 100);
                }
            }


            Log.d("Catalogue", selected);

            searchMap.put(ConstantsBanknote.ISBANKNOTE, String.valueOf(isBanknote));

            if (isDenomination)
            {
                searchMap.put(ConstantsBanknote.DENOMINATION, result[0]);
            } else
            {
                searchMap.put(ConstantsBanknote.PRINTYEAR, result[0]);
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
            DataBaseManager.getDenominationOrPrintYear(catalogueList, isDenomination, isBanknote, () -> requireActivity().runOnUiThread(() -> setCatalogueList(catalogueList)));
            catalogueList.clear();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btncatalogueswitchtype:
                if (isBanknote == 1)
                {
                    btnSwitchType.setText(type[0]);
                    isBanknote = 0;
                } else
                {
                    btnSwitchType.setText(type[1]);
                    isBanknote = 1;
                }
                catalogueListManager();
                break;
            case R.id.btncataloguedenominyearswitch:
                if (isDenomination)
                {
                    btnSwitchDenomination.setText(denominationYear[0]);
                } else
                {
                    btnSwitchDenomination.setText(denominationYear[1]);
                }
                isDenomination = !isDenomination;
                catalogueListManager();
                break;
        }
    }
}