package edu.zntu.ukrainianbanknoteviewer.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import edu.zntu.ukrainianbanknoteviewer.ConstantsBanknote;
import edu.zntu.ukrainianbanknoteviewer.R;
import edu.zntu.ukrainianbanknoteviewer.ShortBanknoteInfo;
import edu.zntu.ukrainianbanknoteviewer.managers.ImageManager;

public class FragmentShowBanknote extends Fragment
{
    private View view;
    private ProgressBar progressBar;
    private TextView tvDenomination, tvPrintYear, tvReleaseDate, tvSize, tvTurnover, tvDescription, tvExtra, tvProtection, tvMemorable;
    private ImageView ivBanknote;
    private String denomination, printYear, releaseDate, size = "Downloading Information...",
            turnover, descriptionAbver, descriptionRever, extra, protection, imageAbver, imageRever, memorable, isBanknote,
            printYearAdditional;

    public void setPrintYearAdditional()
    {
        if (isBanknote.equals("1"))
        {
            printYearAdditional = "Рік початку печаті: ";
        } else
        {
            printYearAdditional = "Рік початку карбування: ";
        }
    }

    //FragmentShowAndSearch
    public void setBanknoteInfo(@NotNull ShortBanknoteInfo shortBanknoteInfo)
    {
        this.isBanknote = shortBanknoteInfo.getIsBanknote();

        setPrintYearAdditional();

        this.denomination = shortBanknoteInfo.getDenomination();
        this.printYear = printYearAdditional + shortBanknoteInfo.getPrintYear() + " рік";
        this.releaseDate = "Дата випуску: " + shortBanknoteInfo.getReleaseDate();
        this.turnover = shortBanknoteInfo.getActive();
        this.imageAbver = shortBanknoteInfo.getImageAbver();
        this.imageRever = shortBanknoteInfo.getImageRever();
        this.memorable = shortBanknoteInfo.getMemorable();
    }


    //Random
    public void setBanknoteInfo(@org.jetbrains.annotations.NotNull Map<Integer, String> map)
    {
        this.isBanknote = map.get(ConstantsBanknote.ISBANKNOTE);

        setPrintYearAdditional();

        this.denomination = map.get(ConstantsBanknote.DENOMINATION);
        this.printYear = printYearAdditional + map.get(ConstantsBanknote.PRINTYEAR) + " рік";
        this.releaseDate = "Дата випуску: " + map.get(ConstantsBanknote.DATE);
        this.turnover = map.get(ConstantsBanknote.TURNOVER);
        this.imageAbver = map.get(ConstantsBanknote.IDINFO);
        this.imageRever = imageAbver + "1";
        this.memorable = map.get(ConstantsBanknote.MEMORABLE);
        this.descriptionAbver = map.get(ConstantsBanknote.DESCRIPTIONFRONT);
        this.descriptionRever = map.get(ConstantsBanknote.DESCRIPTIONBACK);
        this.protection = map.get(ConstantsBanknote.PROTECTIONINFO);
        this.extra = map.get(ConstantsBanknote.EXTRAINFOINFO);
        this.size = map.get(ConstantsBanknote.SIZEINFO) + " мм";
        this.isBanknote = map.get(ConstantsBanknote.ISBANKNOTE);
        setUIContent();
    }


    public FragmentShowBanknote()
    {
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
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


        //Checking photograph
        AtomicBoolean checkerAbver = new AtomicBoolean(true);
        ivBanknote.setOnClickListener(v -> {
            if (checkerAbver.get())
            {
                ivBanknote.setImageDrawable(ImageManager.getImage(imageRever));
                tvDescription.setText(descriptionRever);
                checkerAbver.set(false);
            } else
            {
                ivBanknote.setImageDrawable(ImageManager.getImage(imageAbver));
                tvDescription.setText(descriptionAbver);
                checkerAbver.set(true);
            }
        });

        return view;
    }


    public void setUIContent()
    {

        ivBanknote.setImageDrawable(ImageManager.getImage(imageAbver));
        tvDenomination.setText(denomination);
        tvPrintYear.setText(printYear);
        tvReleaseDate.setText(releaseDate);
        tvTurnover.setText(turnover);
        tvMemorable.setText(memorable);
        progressBar.setVisibility(View.GONE);
        tvDescription.setText(descriptionAbver);
        tvExtra.setText(extra);
        tvSize.setText(size);
        tvProtection.setText(protection);
    }


    public void setAdditionalContent(@NotNull Map<Integer, String> map)
    {
        this.descriptionAbver = map.get(ConstantsBanknote.DESCRIPTIONFRONT);
        this.descriptionRever = map.get(ConstantsBanknote.DESCRIPTIONBACK);
        this.protection = map.get(ConstantsBanknote.PROTECTIONINFO);
        this.extra = map.get(ConstantsBanknote.EXTRAINFOINFO);
        this.size = map.get(ConstantsBanknote.SIZEINFO) + " мм";
        setUIContent();
    }
}