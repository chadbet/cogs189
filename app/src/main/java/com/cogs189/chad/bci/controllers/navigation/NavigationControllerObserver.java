package com.cogs189.chad.bci.controllers.navigation;

/**
 * Created by Chad on 2/27/17.
 */

public interface NavigationControllerObserver {

    void onPageTransition(Page fromPage, Page toPage);

}
