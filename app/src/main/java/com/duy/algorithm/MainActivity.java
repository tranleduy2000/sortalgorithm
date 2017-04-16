package com.duy.algorithm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import static com.duy.algorithm.algorithms.AlgorithmThread.ALGORITHM_NAME.BUBBLE_SORT;
import static com.duy.algorithm.algorithms.AlgorithmThread.ALGORITHM_NAME.COCKTAIL_SORT;
import static com.duy.algorithm.algorithms.AlgorithmThread.ALGORITHM_NAME.INSERTION_SORT;
import static com.duy.algorithm.algorithms.AlgorithmThread.ALGORITHM_NAME.MERGE_SORT;
import static com.duy.algorithm.algorithms.AlgorithmThread.ALGORITHM_NAME.QUICK_SORT;
import static com.duy.algorithm.algorithms.AlgorithmThread.ALGORITHM_NAME.SELECTION_SORT;
import static com.duy.algorithm.algorithms.AlgorithmThread.ALGORITHM_NAME.SHELL_SORT;

/**
 * Created by DUy on 02-Feb-17.
 */

public class MainActivity extends AbstractAppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

//        logAdapter = new LogAdapter(this);
//        mLogger = (RecyclerView) findViewById(R.id.rc_logger);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setStackFromEnd(true);
//        mLogger.setHasFixedSize(true);
//        mLogger.setAdapter(logAdapter);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }
        if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        SortAlgorithmFragment algorithmFragment = null;
        SortSpeedTestFragmentSort sortSpeedTestFragment = null;
        setTitle(item.getTitle().toString());
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_bubble_sort:
                algorithmFragment = SortAlgorithmFragment.newInstance(BUBBLE_SORT);
                commit(algorithmFragment);
                break;
            case R.id.nav_insert_sort:
                algorithmFragment = SortAlgorithmFragment.newInstance(INSERTION_SORT);
                commit(algorithmFragment);
                break;
            case R.id.nav_merge_sort:
                algorithmFragment = SortAlgorithmFragment.newInstance(MERGE_SORT);
                commit(algorithmFragment);
                break;
            case R.id.nav_quick_sort:
                algorithmFragment = SortAlgorithmFragment.newInstance(QUICK_SORT);
                commit(algorithmFragment);
                break;
            case R.id.nav_select_sort:
                algorithmFragment = SortAlgorithmFragment.newInstance(SELECTION_SORT);
                commit(algorithmFragment);
                break;
            case R.id.nav_shell_sort:
                algorithmFragment = SortAlgorithmFragment.newInstance(SHELL_SORT);
                commit(algorithmFragment);
                break;
            case R.id.nav_sort_test:
                sortSpeedTestFragment = SortSpeedTestFragmentSort.newInstance(SHELL_SORT);
                commit(sortSpeedTestFragment);
                break;
            case R.id.nav_setting:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            case R.id.nav_info:
                startActivity(new Intent(getApplicationContext(), AppAboutActivity.class));
                break;
            case R.id.nav_cocktail_sort:
                algorithmFragment = SortAlgorithmFragment.newInstance(COCKTAIL_SORT);
                commit(algorithmFragment);
                break;
        }
        drawerLayout.closeDrawers();
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void commit(Fragment algorithmFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, algorithmFragment);
        fragmentTransaction.commit();
    }

    public void openDrawer(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
//        if (view != null) view.setVisibility(View.GONE);
    }


    public void showLog(String log) {
//        logAdapter.showLog(log);
//        mLogger.scrollToPosition(logAdapter.getItemCount() - 1);
    }

    public void clearLog() {
//        logAdapter.clear();
    }

    public void showLog(String message, int[] array) {
    }
}
