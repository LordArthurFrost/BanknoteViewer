package edu.zntu.ukrainianbanknoteviewer.managers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import edu.zntu.ukrainianbanknoteviewer.R;
import edu.zntu.ukrainianbanknoteviewer.ShortBanknoteInfo;

public class ShortBanknoteInfoAdapter extends ArrayAdapter<ShortBanknoteInfo>
{
    private LayoutInflater inflater;
    private int layout;
    private List<ShortBanknoteInfo> banknotes;

    public ShortBanknoteInfoAdapter(@NonNull Context context, int resource, List<ShortBanknoteInfo> banknotes)
    {
        super(context, resource, banknotes);
        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
        this.banknotes = banknotes;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = inflater.inflate(this.layout, parent, false);

        ImageView banknoteImage = view.findViewById(R.id.lviv);
        TextView denomination = view.findViewById(R.id.lvdenomination);
        TextView printYear = view.findViewById(R.id.lvprintyer);
        TextView releasedate = view.findViewById(R.id.lvreleasedate);
        TextView memorable = view.findViewById(R.id.lvmemorable);
        TextView active = view.findViewById(R.id.lvactive);

        ShortBanknoteInfo banknote = banknotes.get(position);
        banknoteImage.setImageResource(R.drawable.ic_baseline_folder_24);
        denomination.setText(banknote.getDenomination());
        printYear.setText(banknote.getPrintYear());
        releasedate.setText(banknote.getReleaseDate());
        memorable.setText(banknote.getMemorable());
        active.setText(banknote.getActive());

        return view;
    }
}
