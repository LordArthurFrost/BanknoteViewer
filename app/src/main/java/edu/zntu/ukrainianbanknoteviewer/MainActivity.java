package edu.zntu.ukrainianbanknoteviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import edu.zntu.ukrainianbanknoteviewer.fragments.*;
import edu.zntu.ukrainianbanknoteviewer.managers.*;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity
{
    private FragmentManager fm;
    FragmentEnter fragmentEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //инциализация помошника по фрагментам
        fm = getSupportFragmentManager();
        FragmentHelper.init(fm, this);


        fragmentEnter = new FragmentEnter();
        FragmentHelper.openFragment(fragmentEnter);

    }
}