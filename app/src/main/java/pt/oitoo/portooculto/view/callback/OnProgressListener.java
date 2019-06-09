package pt.oitoo.portooculto.view.callback;

public interface OnProgressListener {

    void start();
    void onSuccess();
    void onFailure(int message);
}
