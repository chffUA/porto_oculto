package pt.oitoo.portooculto.engine.implementation.callback;

import java.util.List;

public interface ListResponseCallback<T> {

    void onSuccess(List<T> list);
    void onFailure();
}
