package pt.oitoo.portooculto.engine.implementation.generic;

import pt.oitoo.portooculto.engine.implementation.callback.ListResponseCallback;
import pt.oitoo.portooculto.engine.implementation.callback.SingleResponseCallback;

public class GenericImplementation {

    private SingleResponseCallback singleResponseCallback;
    private ListResponseCallback listResponseCallback;

    public void setCallback(SingleResponseCallback responseCallback){
        this.singleResponseCallback = responseCallback;
    }

    public SingleResponseCallback getSingleResponseCallback() {
        return singleResponseCallback;
    }


    public ListResponseCallback getListResponseCallback() {
        return listResponseCallback;
    }

    public void setListResponseCallback(ListResponseCallback listResponseCallback) {
        this.listResponseCallback = listResponseCallback;
    }

}
