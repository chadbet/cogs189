package com.cogs189.chad.bci.controllers;

import com.cogs189.chad.bci.controllers.navigation.NavigationController;

/**
 * Created by Chad on 2/27/17.
 */

public class ControllerFactory {

    private static ControllerFactory controllerFactory;

    private boolean isTornDown = false;

    private NavigationController navigationController;

    private ControllerFactory() {}

    public static ControllerFactory getInstance() {
        if (controllerFactory == null) {
            controllerFactory = new ControllerFactory();
        }
        return controllerFactory;
    }

    public void reset() {
        navigationController = null;
    }

    public void tearDown() {
        reset();
        controllerFactory = null;
        isTornDown = true;
    }

    public boolean isTornDown() {
        return isTornDown;
    }

    public NavigationController getNavigationController() {
        if (navigationController == null) {
            navigationController = new NavigationController();
        }
        return navigationController;
    }
}
