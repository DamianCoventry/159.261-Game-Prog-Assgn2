package com.lunargravity.world.model;

import com.lunargravity.engine.timeouts.TimeoutManager;
import com.lunargravity.mvc.IModel;

public interface IWorldModel extends IModel {
    void removeTimeouts(TimeoutManager timeoutManager);
}
