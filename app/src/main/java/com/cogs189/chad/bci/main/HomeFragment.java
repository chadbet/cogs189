package com.cogs189.chad.bci.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cogs189.chad.bci.BaseFragment;
import com.cogs189.chad.bci.R;
import com.cogs189.chad.bci.controllers.navigation.Page;
import com.cogs189.chad.bci.utils.ViewUtils;

/**
 * Created by Chad on 2/27/17.
 */

public class HomeFragment extends BaseFragment {

    public final static String TAG = HomeFragment.class.getName();

    private Button openTestPageButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        openTestPageButton = ViewUtils.getView(rootView, R.id.b_test );
        openTestPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getControllerFactory().getNavigationController().transitionToPage(getPage(), Page.MINDWAVE_STREAM);
            }
        });
        return rootView;
    }

    public static HomeFragment getInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public HomeFragment() {
        page = Page.MAIN_HOME;
    }
}
