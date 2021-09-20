package com.lunargravity.mvc;

import com.lunargravity.engine.scene.ISceneStateOwner;

public interface IModel extends ISceneStateOwner {
    String modelToJson();
    void modelFromJson(String json);
}
