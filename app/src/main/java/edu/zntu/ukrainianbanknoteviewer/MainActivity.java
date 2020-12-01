package edu.zntu.ukrainianbanknoteviewer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import edu.zntu.ukrainianbanknoteviewer.fragments.*;
import edu.zntu.ukrainianbanknoteviewer.managers.*;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
    FragmentCatalogue fragmentCatalogue;
    FragmentShowAndSearch fragmentShowAndSearch;
    FragmentShowBanknote fragmentShowBanknote;
    FragmentInfo fragmentInfo;
    private FragmentManager fm;
    private DataBaseManager dataBaseManager;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //инциализация помошника по фрагментам
        fm = getSupportFragmentManager();
        FragmentHelper.init(fm, this);
        setNavigationBar();
        FragmentHelper.openFragment(fragmentCatalogue);
        dataBaseManager = new DataBaseManager(this);
    }


    public void transferMapFilterDialogtoShow(Map<Integer, String> map)
    {
        fragmentShowAndSearch.startSearch(map);
    }

    public void saveSetting(Map<Integer, String> map)
    {
        fragmentShowAndSearch.setSettingsMap(map);
    }


    public void setNavigationBar()
    {
        fragmentCatalogue = new FragmentCatalogue();
        fragmentInfo = new FragmentInfo();
        fragmentShowAndSearch = new FragmentShowAndSearch();
        fragmentShowBanknote = new FragmentShowBanknote();
        BottomNavigationView bottomNavigationView = findViewById(R.id.main_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId())
            {
                case R.id.nav_random:
                    //Функция для генерирования случайного запроса
                    // FragmentHelper.changeFragment(fragmentRandom);
                    break;
                case R.id.nav_search:
                    FragmentHelper.changeFragment(fragmentShowAndSearch);
                    break;
                case R.id.nav_catalogue:
                    FragmentHelper.changeFragment(fragmentCatalogue);
                    break;
                case R.id.nav_info:
                    FragmentHelper.changeFragment(fragmentInfo);
                    break;
            }
            return true;
        });
    }
}