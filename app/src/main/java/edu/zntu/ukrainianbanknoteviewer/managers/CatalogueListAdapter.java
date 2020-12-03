package edu.zntu.ukrainianbanknoteviewer.managers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import edu.zntu.ukrainianbanknoteviewer.R;
import edu.zntu.ukrainianbanknoteviewer.ShortBanknoteInfo;

public class CatalogueListAdapter extends ArrayAdapter<String>
{
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<String> arrayList;

    public CatalogueListAdapter(@NonNull Context context, int resource, ArrayList<String> arrayList)
    {
        super(context, resource, arrayList);
        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
        this.arrayList = arrayList;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = inflater.inflate(this.layout, parent, false);


        TextView denominationOrPrintYear = view.findViewById(R.id.lvcatalogueyear);

        String chosen = arrayList.get(position);
        denominationOrPrintYear.setText(chosen);

        return view;
    }
}
