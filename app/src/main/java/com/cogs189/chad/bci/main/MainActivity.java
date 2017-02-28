package com.cogs189.chad.bci.main;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.cogs189.chad.bci.BaseActivity;
import com.cogs189.chad.bci.R;
import com.cogs189.chad.bci.controllers.navigation.NavigationControllerObserver;
import com.cogs189.chad.bci.controllers.navigation.Page;

public class MainActivity extends BaseActivity implements NavigationControllerObserver {

    private static final Page START_PAGE = Page.MAIN_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getControllerFactory().getNavigationController().addObserver(this);
        openStartFragment();
    }

    @Override
    protected void onStop() {
        if (getControllerFactory() != null) {
            getControllerFactory().getNavigationController().removeObserver(this);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void openStartFragment() {
        Page currentPage = getControllerFactory().getNavigationController().getCurrentPage();
        getControllerFactory().getNavigationController().transitionToPage(currentPage, START_PAGE);
    }

    @Override
    public void onPageTransition(Page fromPage, Page toPage) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch(toPage) {
            case MAIN_HOME:
                fragmentTransaction.replace(R.id.activity_main, HomeFragment.getInstance(), HomeFragment.TAG);
                break;
            case MAIN_TEST:
                fragmentTransaction.replace(R.id.activity_main, TestFragment.getInstance(), TestFragment.TAG);
                break;
        }

        fragmentTransaction.addToBackStack(toPage.name());
        fragmentTransaction.commit();
    }
}
