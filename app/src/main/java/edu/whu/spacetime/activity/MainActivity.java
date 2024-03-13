package edu.whu.spacetime.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import edu.whu.spacetime.MyFragmentPagerAdapter;
import edu.whu.spacetime.R;
import edu.whu.spacetime.fragment.UserFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView nav;
    private ViewPager2 viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNavigation();
        initViewPager();
    }

    private void initViewPager() {
        viewpager = findViewById(R.id.id_viewpager);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(UserFragment.newInstance());
        fragments.add(UserFragment.newInstance());
        fragments.add(UserFragment.newInstance());
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(
                getSupportFragmentManager(), getLifecycle(), fragments);
        viewpager.setAdapter(myFragmentPagerAdapter);

        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position)
                {
                    case 0:
                        nav.setSelectedItemId(R.id.navigation_item1);
                        break;
                    case 1:
                        nav.setSelectedItemId(R.id.navigation_item2);
                        break;
                    case 2:
                        nav.setSelectedItemId(R.id.navigation_item3);
                        break;
                    default:
                        break;
                }
            }

        });
    }

    // 初始化navigationview，把每个item绑定到一个fragment上
    private void initNavigation() {
        nav = findViewById(R.id.nav_view);
        nav.setItemIconTintList(null);
        // navigation绑定到viewpager上
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.navigation_item1){
                    viewpager.setCurrentItem(0);
                    return true;
                }
                else if(id == R.id.navigation_item2){
                    viewpager.setCurrentItem(1);
                    return true;
                }
                else if(id == R.id.navigation_item3){
                    viewpager.setCurrentItem(2);
                    return true;
                }
                return false;
            }
        });
    }
}