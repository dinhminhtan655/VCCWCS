package com.wcs.vcc.main.doichieu.difference_check.current;

public interface IEventHandler<T> {
    void onAfterActualClick(T t);

    void onCurrentActualClick(T t);
}
