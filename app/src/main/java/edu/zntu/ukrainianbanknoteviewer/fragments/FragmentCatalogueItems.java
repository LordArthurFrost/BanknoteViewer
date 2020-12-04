package edu.zntu.ukrainianbanknoteviewer.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.zntu.ukrainianbanknoteviewer.ConstantsBanknote;
import edu.zntu.ukrainianbanknoteviewer.R;
import edu.zntu.ukrainianbanknoteviewer.ShortBanknoteInfo;
import edu.zntu.ukrainianbanknoteviewer.managers.DataBaseManager;
import edu.zntu.ukrainianbanknoteviewer.managers.FragmentHelper;
import edu.zntu.ukrainianbanknoteviewer.managers.ShortBanknoteInfoAdapter;

public class FragmentCatalogueItems extends Fragment
{
    private View view;
    private ListView listView;
    private ShortBanknoteInfoAdapter shortBanknoteInfoAdapter;
    private AdapterView.OnItemClickListener onItemClickListener;
    private Map<Integer, String> searchMap;
    private FragmentShowBanknote fragmentShowBanknote;
    private ProgressBar progressBar;


    public FragmentCatalogueItems()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_catalogue_items, container, false);
            progressBar = view.findViewById(R.id.catalogueitemsprogressbar);
            listView = view.findViewById(R.id.lvcatalogueitems);

            fragmentShowBanknote = new FragmentShowBanknote();
            searchMap = new HashMap<>();
        }

        return view;
    }


    public void setContent(List<ShortBanknoteInfo> shortBanknoteInfoList)
    {
        progressBar.setVisibility(View.GONE);
        shortBanknoteInfoAdapter = new ShortBanknoteInfoAdapter(getContext(), R.layout.listview_banknote, shortBanknoteInfoList);
        listView.setAdapter(shortBanknoteInfoAdapter);
        onItemClickListener = (parent, view, position, id) -> {

            ShortBanknoteInfo selectedBanknote = (ShortBanknoteInfo) parent.getItemAtPosition(position);

            searchMap.put(ConstantsBanknote.IDINFO, selectedBanknote.getImageAbver());


            Log.d("FragmentSearch", selectedBanknote.getDenomination() + " " + selectedBanknote.getPrintYear());


            fragmentShowBanknote.setBanknoteInfo(selectedBanknote);

            DataBaseManager.searchToShowTransfer(searchMap, () -> requireActivity().runOnUiThread(() -> fragmentShowBanknote.setAdditionalContent(searchMap)));

            FragmentHelper.openFragment(fragmentShowBanknote);
        };
        listView.setOnItemClickListener(onItemClickListener);
    }
}