package pt.oitoo.portooculto.engine.implementation.callback;

import retrofit2.Response;

public interface FailResponseCallback<T> {

    void onSuccess(Response<T> response);
    void onFailure(int message);
}
