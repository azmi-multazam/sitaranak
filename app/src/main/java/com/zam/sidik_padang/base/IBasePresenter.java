package com.zam.sidik_padang.base;


public interface IBasePresenter<ViewT> {

    void onViewActive(ViewT view);

    void onViewInactive();
}
