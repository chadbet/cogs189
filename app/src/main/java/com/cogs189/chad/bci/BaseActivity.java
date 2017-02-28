package com.cogs189.chad.bci;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cogs189.chad.bci.controllers.ControllerFactory;

/**
 * Created by Chad on 2/27/17.
 */

public class BaseActivity extends AppCompatActivity implements ServiceContainer {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public ControllerFactory getControllerFactory() {
        return BCIApp.getInstance().getControllerFactory();

    }

}
