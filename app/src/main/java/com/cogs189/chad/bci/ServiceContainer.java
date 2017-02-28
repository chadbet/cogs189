package com.cogs189.chad.bci;

import com.cogs189.chad.bci.controllers.ControllerFactory;

/**
 * Created by Chad on 2/27/17.
 */

public interface ServiceContainer {

    ControllerFactory getControllerFactory();

}
