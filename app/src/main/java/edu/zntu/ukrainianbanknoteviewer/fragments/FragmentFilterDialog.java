package edu.zntu.ukrainianbanknoteviewer.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.zntu.ukrainianbanknoteviewer.ConstantsBanknote;
import edu.zntu.ukrainianbanknoteviewer.MainActivity;
import edu.zntu.ukrainianbanknoteviewer.R;
import edu.zntu.ukrainianbanknoteviewer.managers.DataBaseManager;


public class FragmentFilterDialog extends DialogFragment implements View.OnClickListener
{
    private View view;
    private static String[] turnover, memorable, size, isBanknote;
    private static int sizePosition, turnoverPosition, memorablePosition, isBanknotePosition;
    private EditText etDenomination, etPrintYear, etReleaseDate;
    private Spinner sMemorable, sTurnover, sSize, sType;
    private Button btnCancel, btnSearch;
    private static final String BANKNOTE = "1", COIN = "0", NOTHING = "";
    private MainActivity mainActivity;
    private Map<Integer, String> searchMap, settingsMap;
    private TextView tvisHryvya;
    private CheckBox cbisHryvnya;
    ArrayList<String> dbSize;

    public FragmentFilterDialog()
    {
    }

    public static Fragment newInstance(Map<Integer, String> settingsMap)
    {
        FragmentFilterDialog fragmentFilterDialog = new FragmentFilterDialog();
        Bundle args = new Bundle();

        args.putString("denomination", settingsMap.getOrDefault(ConstantsBanknote.DENOMINATION, NOTHING));
        args.putString("printYear", settingsMap.getOrDefault(ConstantsBanknote.PRINTYEAR, NOTHING));
        args.putString("releaseDate", settingsMap.getOrDefault(ConstantsBanknote.DATE, NOTHING));

        args.putInt("memorablePosition", Integer.parseInt(settingsMap.getOrDefault(ConstantsBanknote.MEMORABLEPOSITION, "0")));
        args.putInt("sizePosition", Integer.parseInt(settingsMap.getOrDefault(ConstantsBanknote.SIZEPOSITION, "0")));
        args.putInt("turnoverPosition", Integer.parseInt(settingsMap.getOrDefault(ConstantsBanknote.TURNOVERPOSITION, "0")));
        args.putInt("isBanknotePosition", Integer.parseInt(settingsMap.getOrDefault(ConstantsBanknote.ISBANKNOTEPOSITION, "0")));

        args.putBoolean("isHryvnya", Boolean.valueOf(settingsMap.getOrDefault(ConstantsBanknote.ISHRYVNYA, "false")));

        fragmentFilterDialog.setArguments(args);

        return fragmentFilterDialog;
    }

    public void arrayListSizeToString(ArrayList<String> arrayList)
    {
        size = new String[arrayList.size() + 1];

        size[0] = NOTHING;
        for (int i = 0; i < arrayList.size(); ++i)
        {
            size[i + 1] = arrayList.get(i);
        }

        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(getContext(), R.layout.filterdialogspinner, size);
        sizeAdapter.setDropDownViewResource(R.layout.filterdialogspinnerlist);
        sSize.setAdapter(sizeAdapter);
        sSize.setSelection(getArguments().getInt("sizePosition"));
        sSize.setClickable(true);
    }


    public void setSize(String type)
    {
        DataBaseManager.getSize(dbSize, type, () -> requireActivity().runOnUiThread(() -> arrayListSizeToString(dbSize)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.view = inflater.inflate(R.layout.fragment_filter_dialog, null);

        tvisHryvya = view.findViewById(R.id.tvfilteddialoghryvna);
        cbisHryvnya = view.findViewById(R.id.cbfilterDialogisHryvna);

        setHryvnyaVisibility(false);

        etDenomination = view.findViewById(R.id.etfilterDialogDenomination);
        etPrintYear = view.findViewById(R.id.etfilterDialogPrintYear);
        etReleaseDate = view.findViewById(R.id.etfilterDialogDate);

        sType = view.findViewById(R.id.sfilterDialogType);
        sMemorable = view.findViewById(R.id.sfilterDialogMemorable);
        sTurnover = view.findViewById(R.id.sfilterDialogTurnover);
        sSize = view.findViewById(R.id.sfilterDialogSize);

        sSize.setClickable(false);

        btnCancel = view.findViewById(R.id.btnfilterdialogdecline);
        btnSearch = view.findViewById(R.id.btnfilterdialoggo);

        isBanknote = new String[]{NOTHING, "Банкнота", "Монета"};
        turnover = new String[]{NOTHING, "У обігу", "Вийшла з обігу"};
        memorable = new String[]{NOTHING, "Пам'ятна", "Не пам'ятна"};

        dbSize = new ArrayList<>();
        setSize(isBanknote[0]);


        mainActivity = (MainActivity) getActivity();

        searchMap = new HashMap<>();
        settingsMap = new HashMap<>();

        etDenomination.setText(getArguments().getString("denomination"));
        etPrintYear.setText(getArguments().getString("printYear"));
        etReleaseDate.setText(getArguments().getString("releaseDate"));

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getContext(), R.layout.filterdialogspinner, isBanknote);
        ArrayAdapter<String> memorableAdapter = new ArrayAdapter<>(getContext(), R.layout.filterdialogspinner, memorable);
        ArrayAdapter<String> turnoverAdapter = new ArrayAdapter<>(getContext(), R.layout.filterdialogspinner, turnover);

        typeAdapter.setDropDownViewResource(R.layout.filterdialogspinnerlist);
        memorableAdapter.setDropDownViewResource(R.layout.filterdialogspinnerlist);
        turnoverAdapter.setDropDownViewResource(R.layout.filterdialogspinnerlist);

        sType.setAdapter(typeAdapter);
        sMemorable.setAdapter(memorableAdapter);
        sTurnover.setAdapter(turnoverAdapter);

        int i = getArguments().getInt("isBanknotePosition");
        sType.setSelection(getArguments().getInt("isBanknotePosition"));
        sMemorable.setSelection(getArguments().getInt("memorablePosition"));
        sTurnover.setSelection(getArguments().getInt("turnoverPosition"));

        sType.setOnItemSelectedListener(onItemSelectedListener);
        sMemorable.setOnItemSelectedListener(onItemSelectedListener);
        sTurnover.setOnItemSelectedListener(onItemSelectedListener);
        sSize.setOnItemSelectedListener(onItemSelectedListener);

        btnSearch.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        return view;
    }

    public void setHryvnyaVisibility(Boolean bool)
    {

        if (bool)
        {
            tvisHryvya.setVisibility(View.VISIBLE);
            cbisHryvnya.setVisibility(View.VISIBLE);
        } else
        {
            tvisHryvya.setVisibility(View.GONE);
            cbisHryvnya.setVisibility(View.GONE);
        }
    }


    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
        {
            Boolean visibility = false;
            switch (parent.getId())
            {
                case R.id.sfilterDialogType:
                    sSize.setClickable(false);
                    sSize.setSelection(0);
                    if (!isBanknote[position].equals(isBanknote[0]))
                    {
                        if (isBanknote[position].equals(isBanknote[1]))
                        {
                            setSize(BANKNOTE);
                        }
                        if (isBanknote[position].equals(isBanknote[2]))
                        {
                            setSize(COIN);
                            visibility = true;
                            cbisHryvnya.setChecked(getArguments().getBoolean("isHryvnya"));
                        }
                    } else
                    {
                        setSize(NOTHING);
                    }
                    isBanknotePosition = position;
                    setHryvnyaVisibility(visibility);
                    Log.d("FragmentFilterDialog", "isBanknotePosition: " + position);
                    break;
                case R.id.sfilterDialogSize:
                    sizePosition = position;
                    Log.d("FragmentFilterDialog", "Size: " + position);
                    break;
                case R.id.sfilterDialogMemorable:
                    memorablePosition = position;
                    Log.d("FragmentFilterDialog", "Memorable: " + position);
                    break;
                case R.id.sfilterDialogTurnover:
                    turnoverPosition = position;
                    Log.d("FragmentFilterDialog", "Turnover: " + position);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent)
        {

        }
    };


    public void clearFilter()
    {
        getArguments().clear();
        etDenomination.setText(NOTHING);
        etReleaseDate.setText(NOTHING);
        etPrintYear.setText(NOTHING);
        sType.setSelection(0);
        cbisHryvnya.setChecked(false);
        sMemorable.setSelection(0);
        sTurnover.setSelection(0);
        sSize.setSelection(0);
        settingsMap.clear();
        mainActivity.clearInput();
    }


    public void setSearchFilters()
    {
        String[] crystalSize;
        String[] res = new String[]{etDenomination.getText().toString(), etPrintYear.getText().toString(), etReleaseDate.getText().toString()};

        settingsMap.putIfAbsent(ConstantsBanknote.MEMORABLEPOSITION, String.valueOf(memorablePosition));
        settingsMap.putIfAbsent(ConstantsBanknote.SIZEPOSITION, String.valueOf(sizePosition));
        settingsMap.putIfAbsent(ConstantsBanknote.TURNOVERPOSITION, String.valueOf(turnoverPosition));
        settingsMap.putIfAbsent(ConstantsBanknote.ISBANKNOTEPOSITION, String.valueOf(isBanknotePosition));
        settingsMap.putIfAbsent(ConstantsBanknote.ISHRYVNYA, String.valueOf(cbisHryvnya.isChecked()));

        try
        {
            if (!isBanknote[isBanknotePosition].equals(isBanknote[0]))
            {
                if (isBanknote[isBanknotePosition].equals(isBanknote[1]))
                {
                    searchMap.putIfAbsent(ConstantsBanknote.ISBANKNOTE, BANKNOTE);
                }
                if (isBanknote[isBanknotePosition].equals(isBanknote[2]))
                {
                    searchMap.putIfAbsent(ConstantsBanknote.ISBANKNOTE, COIN);
                    if (cbisHryvnya.isChecked())
                    {
                        if (res[0] != null && !res[0].equals(NOTHING) && cbisHryvnya.isChecked())
                        {
                            settingsMap.putIfAbsent(ConstantsBanknote.DENOMINATION, res[0]);
                            res[0] = String.valueOf(Integer.parseInt(res[0]) * 100);
                        }
                    }
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        if (cbisHryvnya.isChecked() && isBanknote[isBanknotePosition].equals(isBanknote[2]))
        {
            searchMap.putIfAbsent(ConstantsBanknote.ISHRYVNYA, "1");

        }

        if (!cbisHryvnya.isChecked() && isBanknote[isBanknotePosition].equals(isBanknote[2]))
        {
            searchMap.putIfAbsent(ConstantsBanknote.ISHRYVNYA, "0");
        }

        for (int i = 0; i < 3; ++i)
        {
            if (!res[i].equals(NOTHING))
            {
                searchMap.putIfAbsent(i, res[i]);
                settingsMap.putIfAbsent(i, res[i]);
            }
        }


        try
        {
            if (!memorable[memorablePosition].equals(memorable[0]))
            {
                if (memorable[memorablePosition].equals(memorable[1]))
                {
                    searchMap.putIfAbsent(ConstantsBanknote.MEMORABLE, "Not");
                }
                if (memorable[memorablePosition].equals(memorable[2]))
                {
                    searchMap.putIfAbsent(ConstantsBanknote.MEMORABLE, NOTHING);

                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            if (!turnover[turnoverPosition].equals(turnover[0]))
            {
                if (turnover[turnoverPosition].equals(turnover[1]))
                {
                    searchMap.putIfAbsent(ConstantsBanknote.TURNOVER, "1");
                }
                if (turnover[turnoverPosition].equals(turnover[2]))
                {
                    searchMap.putIfAbsent(ConstantsBanknote.TURNOVER, "0");
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        if (!(size == null))
        {

            if (!size[sizePosition].equals(NOTHING))
            {
                crystalSize = (size[sizePosition]).split("[А-я]");
                crystalSize = crystalSize[0].split("\\s");
                searchMap.putIfAbsent(ConstantsBanknote.SIZEINFO, crystalSize[0]);
            }
        }


        this.dismiss();
        mainActivity.saveSetting(settingsMap);
        mainActivity.transferMapFilterDialogToShow(searchMap);
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnfilterdialoggo:
                setSearchFilters();
                break;
            case R.id.btnfilterdialogdecline:
                clearFilter();
                break;
        }
    }

}