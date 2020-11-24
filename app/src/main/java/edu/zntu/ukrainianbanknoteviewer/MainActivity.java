package edu.zntu.ukrainianbanknoteviewer;

import androidx.appcompat.app.AppCompatActivity;
import edu.zntu.ukrainianbanknoteviewer.fragments.*;
import edu.zntu.ukrainianbanknoteviewer.managers.*;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EnterFragment enterFragment = new EnterFragment();
        FragmentHelper.openFragment(enterFragment);

    }
}