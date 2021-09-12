package com.lunargravity.mvc;

public interface IModel {
    String toJson();
    void fromJson(String json);
}
