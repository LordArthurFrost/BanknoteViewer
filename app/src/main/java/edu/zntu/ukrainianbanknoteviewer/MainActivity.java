package edu.zntu.ukrainianbanknoteviewer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

import edu.zntu.ukrainianbanknoteviewer.fragments.FragmentCatalogue;
import edu.zntu.ukrainianbanknoteviewer.fragments.FragmentInfo;
import edu.zntu.ukrainianbanknoteviewer.fragments.FragmentShowAndSearch;
import edu.zntu.ukrainianbanknoteviewer.fragments.FragmentShowBanknote;
import edu.zntu.ukrainianbanknoteviewer.managers.DataBaseManager;
import edu.zntu.ukrainianbanknoteviewer.managers.FragmentHelper;

import static java.lang.Thread.sleep;

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
        Map <Integer, String> randomMap = new HashMap<>();
        fragmentCatalogue = new FragmentCatalogue();
        fragmentInfo = new FragmentInfo();
        fragmentShowAndSearch = new FragmentShowAndSearch();
        BottomNavigationView bottomNavigationView = findViewById(R.id.main_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId())
            {
                case R.id.nav_random:

                    fragmentShowBanknote = new FragmentShowBanknote();
                    DataBaseManager.getAllInformation(randomMap, () -> runOnUiThread(() -> fragmentShowBanknote.setBanknoteInfo(randomMap)));
                    randomMap.clear();
                    FragmentHelper.openFragment(fragmentShowBanknote);
                    break;
                case R.id.nav_search:

                    FragmentHelper.changeFragment(fragmentShowAndSearch);
                    break;
                case R.id.nav_catalogue:
                    //FragmentHelper.changeFragment(fragmentCatalogue);
                    break;
                case R.id.nav_info:
                    FragmentHelper.changeFragment(fragmentInfo);
                    break;
            }
            return true;
        });
    }
}