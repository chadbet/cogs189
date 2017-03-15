package com.cogs189.chad.bci.controllers.navigation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Chad on 2/27/17.
 */

public class NavigationController {

    private Page currentPage = Page.NO_PAGE;

    private Set<NavigationControllerObserver> observers = new HashSet<>();

    private boolean transitioningPages = false;
    private Set<NavigationControllerObserver> removeList = new HashSet<>();
    private Set<NavigationControllerObserver> addList = new HashSet<>();

    public void addObserver(NavigationControllerObserver navigationControllerObserver) {
        if (transitioningPages) {
            addList.add(navigationControllerObserver);
        } else {
            observers.add(navigationControllerObserver);
        }
    }

    public void removeObserver(NavigationControllerObserver navigationControllerObserver) {
        if(transitioningPages) {
            removeList.add(navigationControllerObserver);
        } else {
            observers.remove(navigationControllerObserver);
        }
    }

    public void transitionToPage(Page fromPage, Page toPage) {
        currentPage = toPage;

        transitioningPages = true;

        for (NavigationControllerObserver observer : observers) {
            observer.onPageTransition(fromPage, toPage);
        }

        transitioningPages = false;
        removeSavedObservers();
    }

    private void removeSavedObservers() {
        for (Iterator<NavigationControllerObserver> iterator = removeList.iterator(); iterator.hasNext();) {
            NavigationControllerObserver observer = iterator.next();
            observers.remove(observer);
            iterator.remove();
        }
        for (Iterator<NavigationControllerObserver> iterator = addList.iterator(); iterator.hasNext();) {
            NavigationControllerObserver observer = iterator.next();
            observers.add(observer);
            iterator.remove();
        }
    }

    public Page getCurrentPage() {
        return currentPage;
    }

    public void overrideCurrentPage(Page backToPage) {
        currentPage = backToPage;
    }


}
