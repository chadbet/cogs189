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

public class TestFragment extends BaseFragment {

    public static final String TAG = TestFragment.class.getName();

    private Button toHomeBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fl_test, container, false);

        toHomeBtn = ViewUtils.getView(rootView, R.id.b_to_home);
        toHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getControllerFactory().getNavigationController().transitionToPage(getPage(), Page.MAIN_HOME);
            }
        });
        return rootView;
    }

    public static TestFragment getInstance() {
        TestFragment fragment = new TestFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }
}
