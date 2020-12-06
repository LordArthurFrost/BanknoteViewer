package edu.zntu.ukrainianbanknoteviewer;

import android.os.Bundle;
import android.view.ViewGroup;

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
import edu.zntu.ukrainianbanknoteviewer.managers.ImageManager;

public class MainActivity extends AppCompatActivity
{
    private FragmentCatalogue fragmentCatalogue;
    private FragmentShowAndSearch fragmentShowAndSearch;
    private FragmentShowBanknote fragmentShowBanknote;
    private FragmentInfo fragmentInfo;
    private ImageManager imageManager;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentCatalogue = new FragmentCatalogue();
        fragmentInfo = new FragmentInfo();
        fragmentShowAndSearch = new FragmentShowAndSearch();
        fragmentShowBanknote = new FragmentShowBanknote();

        FragmentManager fm = getSupportFragmentManager();
        FragmentHelper.init(fm, this);

        FragmentHelper.changeFragment(fragmentCatalogue);

        setNavigationBar();
        FragmentHelper.openFragment(fragmentCatalogue);
        DataBaseManager dataBaseManager = new DataBaseManager(this);
        imageManager = new ImageManager(this);
    }


    public void transferMapFilterDialogToShow(Map<Integer, String> map)
    {
        fragmentShowAndSearch.startSearch(map);
    }

    public void saveSetting(Map<Integer, String> map)
    {
        fragmentShowAndSearch.setSettingsMap(map);
    }

    public void clearInput()
    {
        fragmentShowAndSearch.clearInput();
    }


    public void setNavigationBar()
    {
        Map<Integer, String> randomMap = new HashMap<>();
        BottomNavigationView bottomNavigationView = findViewById(R.id.main_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId())
            {
                case R.id.nav_random:
                    DataBaseManager.getAllInformation(randomMap, () -> runOnUiThread(() -> fragmentShowBanknote.setBanknoteInfo(randomMap)));
                    randomMap.clear();
                    FragmentHelper.changeFragment(fragmentShowBanknote);
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