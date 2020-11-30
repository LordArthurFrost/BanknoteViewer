package edu.zntu.ukrainianbanknoteviewer.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import edu.zntu.ukrainianbanknoteviewer.ConstantsBanknote;
import edu.zntu.ukrainianbanknoteviewer.R;
import edu.zntu.ukrainianbanknoteviewer.ShortBanknoteInfo;

public class FragmentShowBanknote extends Fragment
{
    private View view;
    private ProgressBar progressBar;
    private TextView tvDenomination, tvPrintYear, tvReleaseDate, tvSize, tvTurnover, tvDescription, tvExtra, tvProtection, tvMemorable;
    private ImageView ivBanknote;
    private String denomination, printYear, releaseDate, size = "Downloading Information...", turnover, descriptionAbver, descriptionRever, extra, protection, imageAbver, imageRever, memorable;

    public void setBanknoteInfo(ShortBanknoteInfo shortBanknoteInfo)
    {
        this.denomination = shortBanknoteInfo.getDenomination();
        int checkDenomination = Integer.parseInt(denomination);
        setDenominationValue(checkDenomination);

        this.printYear = "Рік початку печаті: " + shortBanknoteInfo.getPrintYear() + " рік";
        this.releaseDate = "Дата випуску: " + shortBanknoteInfo.getReleaseDate();
        this.turnover = shortBanknoteInfo.getActive();
        this.imageAbver = shortBanknoteInfo.getImageAbver();
        this.imageRever = shortBanknoteInfo.getImageRever();
        this.memorable = shortBanknoteInfo.getMemorable();
    }

    public void setDenominationValue(int checkDenomination)
    {
        switch (checkDenomination)
        {
            case 1:
                this.denomination = denomination + " Гривня";
                break;

            case 2:
                this.denomination = denomination + " Гривні";
                break;

            default:
                this.denomination = denomination + " Гривень";
                break;
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
        tvTurnover = view.findViewById(R.id.tvshowturnover);
        ivBanknote = view.findViewById(R.id.ivshowimage);
        tvMemorable = view.findViewById(R.id.tvshowmenorable);

        progressBar = view.findViewById(R.id.showProgressBar);

        tvSize = view.findViewById(R.id.tvshowsize);
        tvDescription = view.findViewById(R.id.tvshowdescription);
        tvExtra = view.findViewById(R.id.tvshowextra);
        tvProtection = view.findViewById(R.id.tvshowprotection);

        setPrimeContent();

        AtomicBoolean checkerAbver = new AtomicBoolean(true);
        ivBanknote.setOnClickListener(v -> {
            if (checkerAbver.get())
            {
                ivBanknote.setImageResource(R.drawable.ic_baseline_folder_24);
                tvDescription.setText(descriptionRever);
                checkerAbver.set(false);
            } else
            {
                ivBanknote.setImageResource(R.drawable.ic_baseline_apps_24);
                tvDescription.setText(descriptionAbver);
                checkerAbver.set(true);
            }
        });

        return view;
    }

    public void setPrimeContent()
    {
        tvDenomination.setText(denomination);
        tvPrintYear.setText(printYear);
        tvReleaseDate.setText(releaseDate);
        tvTurnover.setText(turnover);
        ivBanknote.setImageResource(R.drawable.ic_baseline_apps_24);
        tvMemorable.setText(memorable);
    }

    public void setAdditionalContent(Map<Integer, String> map)
    {
        this.descriptionAbver = map.get(ConstantsBanknote.DESCRIPTIONFRONT);
        this.descriptionRever = map.get(ConstantsBanknote.DESCRIPTIONBACK);
        this.protection = map.get(ConstantsBanknote.PROTECTIONINFO);
        this.extra = map.get(ConstantsBanknote.EXTRAINFOINFO);
        this.size = map.get(ConstantsBanknote.SIZEINFO) + " мм";

        progressBar.setVisibility(View.GONE);

        tvDescription.setText(descriptionAbver);
        tvExtra.setText(extra);
        tvSize.setText(size);
        tvProtection.setText(protection);
    }
}