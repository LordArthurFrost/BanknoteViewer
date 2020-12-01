package edu.zntu.ukrainianbanknoteviewer.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.Map;

import edu.zntu.ukrainianbanknoteviewer.ConstantsBanknote;
import edu.zntu.ukrainianbanknoteviewer.MainActivity;
import edu.zntu.ukrainianbanknoteviewer.R;


public class FragmentFilterDialog extends DialogFragment implements View.OnClickListener
{
    private View view;
    private static String[] turnover, memorable, size;
    private static int sizePosition, turnoverPosition, memorablePosition;
    private EditText etDenomination, etPrintYear, etReleaseDate;
    private Spinner sMemorable, sTurnover, sSize;
    private Button btnCancel, btnSearch;
    private MainActivity mainActivity;
    private Map<Integer, String> searchMap, settingsMap;

    public FragmentFilterDialog()
    {
        //
    }

    public static Fragment newInstance(String denomination, String printYear, String releaseDate, String[] size, int memorablePosition, int sizePosition, int turnoverPosition)
    {
        FragmentFilterDialog fragmentFilterDialog = new FragmentFilterDialog();
        Bundle args = new Bundle();

        args.putString("denomination", denomination);
        args.putString("printYear", printYear);
        args.putString("releaseDate", releaseDate);

        args.putStringArray("size", size);

        args.putInt("memorablePosition", memorablePosition);
        args.putInt("sizePosition", sizePosition);
        args.putInt("turnoverPosition", turnoverPosition);

        fragmentFilterDialog.setArguments(args);

        return fragmentFilterDialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        getDialog().setTitle("Test");
        this.view = inflater.inflate(R.layout.fragment_filter_dialog, null);

        etDenomination = view.findViewById(R.id.etfilterDialogDenomination);
        etPrintYear = view.findViewById(R.id.etfilterDialogPrintYear);
        etReleaseDate = view.findViewById(R.id.etfilterDialogDate);

        sMemorable = view.findViewById(R.id.sfilterDialogMemorable);
        sTurnover = view.findViewById(R.id.sfilterDialogTurnover);
        sSize = view.findViewById(R.id.sfilterDialogSize);

        btnCancel = view.findViewById(R.id.btnfilterdialogdecline);
        btnSearch = view.findViewById(R.id.btnfilterdialoggo);

        turnover = new String[]{"", "У обігу", "Вийшла з обігу"};
        memorable = new String[]{"", "Пам'ятна", "Не пам'ятна"};
        size = getArguments().getStringArray("size");

        mainActivity = (MainActivity) getActivity();

        searchMap = new HashMap<>();
        settingsMap = new HashMap<>();

        etDenomination.setText(getArguments().getString("denomination"));
        etPrintYear.setText(getArguments().getString("printYear"));
        etReleaseDate.setText(getArguments().getString("releaseDate"));

        ArrayAdapter<String> memorableAdapter = new ArrayAdapter<>(getContext(), R.layout.filterdialogspinner, memorable);
        ArrayAdapter<String> turnoverAdapter = new ArrayAdapter<>(getContext(), R.layout.filterdialogspinner, turnover);
        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(getContext(), R.layout.filterdialogspinner, size);

        memorableAdapter.setDropDownViewResource(R.layout.filterdialogspinnerlist);
        turnoverAdapter.setDropDownViewResource(R.layout.filterdialogspinnerlist);
        sizeAdapter.setDropDownViewResource(R.layout.filterdialogspinnerlist);

        sMemorable.setAdapter(memorableAdapter);
        sTurnover.setAdapter(turnoverAdapter);
        sSize.setAdapter(sizeAdapter);

        sMemorable.setSelection(getArguments().getInt("memorablePosition"));
        sTurnover.setSelection(getArguments().getInt("turnoverPosition"));
        sSize.setSelection(getArguments().getInt("sizePosition"));

        sMemorable.setOnItemSelectedListener(onItemSelectedListener);
        sTurnover.setOnItemSelectedListener(onItemSelectedListener);
        sSize.setOnItemSelectedListener(onItemSelectedListener);

        btnSearch.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        return view;
    }

    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
        {
            switch (parent.getId())
            {
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
        etDenomination.setText("");
        etReleaseDate.setText("");
        etPrintYear.setText("");
        sMemorable.setSelection(0);
        sTurnover.setSelection(0);
        sSize.setSelection(0);
        settingsMap.clear();
    }

    public void setSearchFilters()
    {
        String[] crystalSize;
        String[] res = new String[]{etDenomination.getText().toString(), etPrintYear.getText().toString(), etReleaseDate.getText().toString()};
        for (int i = 0; i < 3; ++i)
        {
            if (!res[i].equals(""))
            {
                searchMap.putIfAbsent(i, res[i]);
                settingsMap.putIfAbsent(i, res[i]);
            }
        }

        settingsMap.putIfAbsent(ConstantsBanknote.MEMORABLEPOSITION, String.valueOf(memorablePosition));
        settingsMap.putIfAbsent(ConstantsBanknote.SIZEPOSITION, String.valueOf(sizePosition));
        settingsMap.putIfAbsent(ConstantsBanknote.TURNOVERPOSITION, String.valueOf(turnoverPosition));

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
                    searchMap.putIfAbsent(ConstantsBanknote.MEMORABLE, "");

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

            if (!size[sizePosition].equals(""))
            {
                crystalSize = (size[sizePosition]).split("[А-я]");
                crystalSize = crystalSize[0].split("\\s");
                searchMap.putIfAbsent(ConstantsBanknote.SIZEINFO, crystalSize[0]);
            }
        }


        this.dismiss();
        mainActivity.saveSetting(settingsMap);
        mainActivity.transferMapFilterDialogtoShow(searchMap);
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