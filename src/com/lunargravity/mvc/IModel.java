package com.lunargravity.mvc;

import com.lunargravity.engine.scene.ISceneStateOwner;

public interface IModel extends ISceneStateOwner {
    String toJson();
    void fromJson(String json);
}
