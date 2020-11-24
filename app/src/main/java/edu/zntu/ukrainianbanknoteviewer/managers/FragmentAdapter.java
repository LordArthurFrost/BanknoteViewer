package edu.zntu.ukrainianbanknoteviewer.managers;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter
{
    //TODO?
    public FragmentAdapter(FragmentManager fragmentManager)
    {
        super(fragmentManager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        //return(Fragment.newInstance(position));
        return null; ////////////
    }

    @Override
    public int getCount()
    {
        return 4;
    }
}
