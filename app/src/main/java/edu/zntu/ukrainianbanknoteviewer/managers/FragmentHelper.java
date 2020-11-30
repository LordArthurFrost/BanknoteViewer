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
    public static void openFragment(Fragment fragment)
    {
         /*   fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.main_fragment_container, fragment)
                    .addToBackStack(String.valueOf(activity.findViewById(R.id.nestedScrollView).getScrollY()))
                    .commit();
            activity.findViewById(R.id.nestedScrollView).scrollTo(0, 0);*/
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }


    //TODO?
        /*
        public static void hideFragment(Fragment fragment)
        {
            fragmentManager.beginTransaction()
                    .hide(fragment)
                    .commit();
        }
        */

    public static void changeFragment(Fragment fragment)
    {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

          /*  fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.main_fragment_container, fragment)
                    .commit();*/

        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment, fragment)
                .commit();
    }

}

