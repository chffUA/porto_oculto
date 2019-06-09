package pt.oitoo.portooculto.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.location.Location;

import pt.oitoo.portooculto.BR;

public class Post extends BaseObservable {

   private Bitmap cameraBitmap;
   private Location location;
   private String pointId;
   private String address;
   private String buildingStatus;
   private boolean validatingFields;

    public Post(Bitmap cameraBitmap, boolean validatingFields) {
        this.cameraBitmap = cameraBitmap;
    }

    @Bindable
    public Bitmap getCameraBitmap() {
        return cameraBitmap;
    }

    public void setCameraBitmap(Bitmap cameraBitmap) {
        this.cameraBitmap = cameraBitmap;
        notifyPropertyChanged(BR.cameraBitmap);

    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public String getPointId(){
        return pointId;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    @Bindable
    public boolean isValidatingFields() {
        return validatingFields;
    }

    public void setValidatingFields(boolean validatingFields) {
        this.validatingFields = validatingFields;
        notifyPropertyChanged(BR.validatingFields);
    }

    @Bindable
    public String getBuildingStatus() {
        return buildingStatus;
    }

    public void setBuildingStatus(String buildingStatus) {
        this.buildingStatus = buildingStatus;
        notifyPropertyChanged(BR.buildingStatus);
    }


}
