package com.cogs189.chad.bci;

import android.app.Application;

import com.cogs189.chad.bci.controllers.ControllerFactory;

/**
 * Created by Chad on 2/27/17.
 */

public class BCIApp extends Application implements ServiceContainer {

    public static final String TAG = BCIApp.class.getName();

    private static BCIApp bciApp;
    private boolean tornDown;

    private ControllerFactory controllerFactory;

    public static BCIApp getInstance() {
        if (bciApp == null) {
            throw new RuntimeException("App not initialized");
        }
        if (bciApp.isTornDown()) {
            bciApp.init();
        }
        return bciApp;
    }

    public boolean isTornDown() {
        return tornDown;
    }

    private void init() {
        bciApp = this;

        controllerFactory = ControllerFactory.getInstance();

        tornDown = false;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public ControllerFactory getControllerFactory() {
        return controllerFactory;
    }
}
