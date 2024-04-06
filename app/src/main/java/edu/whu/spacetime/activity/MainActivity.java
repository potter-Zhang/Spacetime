package edu.whu.spacetime.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import edu.whu.spacetime.R;
import edu.whu.spacetime.SpacetimeApplication;
import edu.whu.spacetime.adapter.MyFragmentPagerAdapter;
import edu.whu.spacetime.dao.NotebookDao;
import edu.whu.spacetime.domain.Notebook;
import edu.whu.spacetime.fragment.HelloArFragment;
import edu.whu.spacetime.fragment.NoteBrowserFragment;
import edu.whu.spacetime.fragment.TodoBrowserFragment;
import edu.whu.spacetime.fragment.UserFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView nav;
    private ViewPager2 viewpager;

    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNavigation();
        initViewPager();
    }

    @Override
    public void onBackPressed() {
        boolean exitTrigger = false;
        for (Fragment fragment : fragments) {
            if (fragment instanceof NoteBrowserFragment) {
                exitTrigger = ((NoteBrowserFragment) fragment).onBackPressed();
            }
        }
        if (!exitTrigger) {
            super.onBackPressed();
        }
    }

    private void initViewPager() {
        viewpager = findViewById(R.id.id_viewpager);
        fragments = new ArrayList<>();
        // 在这里添加fragment
        fragments.add(initNoteFragment());
        fragments.add(new TodoBrowserFragment());
        fragments.add(UserFragment.newInstance());
        fragments.add(new HelloArFragment());
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
                    case 3:
                        nav.setSelectedItemId(R.id.navigation_item4);
                    default:
                        break;
                }
            }

        });
        //设置底部导航栏字体颜色
        //状态
        int[][] states = new int[2][];
        //按下
        states[0] = new int[] {android.R.attr.state_checked};
        //默认
        states[1] = new int[] {-android.R.attr.state_checked};

        //状态对应颜色值（按下，默认）
        int[] colors = new int[] {Color.parseColor("#09bb07"),Color.GRAY};
        ColorStateList colorList = new ColorStateList(states, colors);
        nav.setItemTextColor(colorList);
        viewpager.setUserInputEnabled(false);
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
                else if (id == R.id.navigation_item4) {
                    viewpager.setCurrentItem(3);
                    return true;
                }
                return false;
            }
        });
    }

    private NoteBrowserFragment initNoteFragment() {
        NotebookDao notebookDao = SpacetimeApplication.getInstance().getDatabase().getNotebookDao();
        // 取出第一个默认笔记本
        Notebook notebook = notebookDao.getNotebooksByUserId(SpacetimeApplication.getInstance().getCurrentUser().getUserId()).get(0);
        return NoteBrowserFragment.newInstance(notebook);
    }

}