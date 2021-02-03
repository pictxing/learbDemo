package com.superfuns.healthcode.base;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();

}
