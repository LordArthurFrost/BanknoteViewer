package edu.zntu.ukrainianbanknoteviewer.managers;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import edu.zntu.ukrainianbanknoteviewer.R;


public class FragmentHelper
{
    private static FragmentManager fragmentManager;
    private static Activity activity;

    public static void init(FragmentManager fm, Activity act)
    {
        fragmentManager = fm;
        activity = act;
    }

    //метод открытия фрагмента
    public static void openFragment(Fragment fragmentOpen)
    {
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.main_fragment, fragmentOpen)
                .addToBackStack(null)
                .commit();
    }




    public static void changeFragment(Fragment fragment)
    {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.main_fragment, fragment)
                .commit();
    }

}

