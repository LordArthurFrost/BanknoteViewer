package edu.zntu.ukrainianbanknoteviewer.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;

import edu.zntu.ukrainianbanknoteviewer.ConstantsBanknote;
import edu.zntu.ukrainianbanknoteviewer.R;
import edu.zntu.ukrainianbanknoteviewer.ShortBanknoteInfo;

public class FragmentShowBanknote extends Fragment
{
    private View view;


    TextView tvDenomination, tvPrintYear, tvReleaseDate, tvSize, tvTurnover, tvDescription, tvExtra, tvProtection;
    ImageView ivBanknote;
    String denomination, printYear, releaseDate, size, turnover, descriptionAbver, descriptionRever, extra, protection, imageAbver, imageRever;

    public void setBanknoteInfo(ShortBanknoteInfo shortBanknoteInfo, Map<Integer, String> map)
    {
        this.denomination = shortBanknoteInfo.getDenomination();
        int checkDenomination = Integer.parseInt(shortBanknoteInfo.getDenomination());
        setDenominationValue(checkDenomination);

        this.denomination = shortBanknoteInfo.getDenomination() + " Гривня";
        this.printYear = shortBanknoteInfo.getPrintYear() + " Рік";
        this.releaseDate = shortBanknoteInfo.getReleaseDate();
        this.turnover = shortBanknoteInfo.getActive();
        this.imageAbver = shortBanknoteInfo.getImageAbver();
        this.imageRever = shortBanknoteInfo.getImageRever();
        this.descriptionAbver = map.get(ConstantsBanknote.DESCRIPTIONFRONT);
        this.descriptionRever = map.get(ConstantsBanknote.DESCRIPTIONBACK);
        this.protection = map.get(ConstantsBanknote.PROTECTIONINFO);
        this.extra = map.get(ConstantsBanknote.EXTRAINFOINFO);
        this.size = map.get(ConstantsBanknote.SIZEINFO) + " мм";

    }

    public void setDenominationValue(int checkDenomination)
    {
        if (checkDenomination == 1)
        {
            this.denomination = denomination + " Гривня";
        }
        if (checkDenomination == 2)
        {
            this.denomination = denomination + " Гривні";
        }
        if (checkDenomination > 2)
        {
            this.denomination = denomination + " Гривень";
        }
    }

    public FragmentShowBanknote()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_show_banknote, container, false);
        tvDenomination = view.findViewById(R.id.tvshowdenomination);
        tvPrintYear = view.findViewById(R.id.tvshowprintyear);
        tvReleaseDate = view.findViewById(R.id.tvshowreleaseDate);
        tvSize = view.findViewById(R.id.tvshowsize);
        tvTurnover = view.findViewById(R.id.tvshowturnover);
        tvDescription = view.findViewById(R.id.tvshowdescription);
        tvExtra = view.findViewById(R.id.tvshowextra);
        tvProtection = view.findViewById(R.id.tvshowprotection);
        ivBanknote = view.findViewById(R.id.ivshowimage);
        setContent();
        ivBanknote.setOnClickListener(v -> {
            ivBanknote.setImageResource(R.drawable.ic_baseline_folder_24);
        });

        return view;
    }

    public void setContent()
    {
        tvDenomination.setText(denomination);
        tvPrintYear.setText(printYear);
        tvReleaseDate.setText(releaseDate);
        tvDescription.setText(descriptionAbver);
        tvTurnover.setText(turnover);
        tvExtra.setText(extra);
        ivBanknote.setImageResource(R.drawable.ic_baseline_apps_24);
        tvProtection.setText(protection);
    }
}