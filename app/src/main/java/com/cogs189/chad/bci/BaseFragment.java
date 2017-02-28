package com.cogs189.chad.bci;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cogs189.chad.bci.controllers.ControllerFactory;
import com.cogs189.chad.bci.controllers.navigation.Page;

/**
 * Created by Chad on 2/27/17.
 */

public abstract class BaseFragment extends Fragment implements ServiceContainer {

    protected Page page;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public ControllerFactory getControllerFactory() {
        return getActivity() != null ? ((BaseActivity) getActivity()).getControllerFactory() : null;
    }


    public Page getPage() {
        return page;
    }
}
