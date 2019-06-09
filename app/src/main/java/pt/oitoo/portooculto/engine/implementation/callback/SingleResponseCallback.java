package pt.oitoo.portooculto.engine.implementation.callback;


public interface SingleResponseCallback<T> {

    void onSuccess(T t);
    void onFailure(int message);

}
