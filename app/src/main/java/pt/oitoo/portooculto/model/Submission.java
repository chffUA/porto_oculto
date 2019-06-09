package pt.oitoo.portooculto.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

import pt.oitoo.portooculto.BR;

public class Submission extends BaseObservable implements Parcelable {

    private String uid;
    private String name;
    private String status;
    private GeoPoint geoLocation;
    private String addressString;
    private String ownership;
    private String description;
    private int year;
    private String wishes;
    private String type;
    private String uploaderName;
    private String uploaderEmail;
    private String uploaderUUID;
    private ArrayList<String> photos;
    private String buildingStatus;
    private boolean validatingFields;

    public Submission() {
    }

    public Submission( String uid, String addressString, String buildingStatus) {
        this.uid = uid;
        this.addressString = addressString;
        this.buildingStatus = buildingStatus;
    }

    public Submission(String uid, String name, String status, GeoPoint geoLocation, String addressString, String ownership, String description, int year, String wishes, String type, String uploaderName, String uploaderEmail, String uploaderUUID, ArrayList<String> photos, String buildingStatus, boolean validatingFields) {
        this.uid = uid;
        this.name = name;
        this.status = status;
        this.geoLocation = geoLocation;
        this.addressString = addressString;
        this.ownership = ownership;
        this.description = description;
        this.year = year;
        this.wishes = wishes;
        this.type = type;
        this.uploaderName = uploaderName;
        this.uploaderEmail = uploaderEmail;
        this.uploaderUUID = uploaderUUID;
        this.photos = photos;
        this.buildingStatus = buildingStatus;
        this.validatingFields = validatingFields;
    }

    protected Submission(Parcel in) {
        uid = in.readString();
        name = in.readString();
        status = in.readString();
        addressString = in.readString();
        ownership = in.readString();
        description = in.readString();
        year = in.readInt();
        wishes = in.readString();
        type = in.readString();
        uploaderName = in.readString();
        uploaderEmail = in.readString();
        uploaderUUID = in.readString();
        photos = in.createStringArrayList();
        buildingStatus = in.readString();
    }

    public static final Creator<Submission> CREATOR = new Creator<Submission>() {
        @Override
        public Submission createFromParcel(Parcel in) {
            return new Submission(in);
        }

        @Override
        public Submission[] newArray(int size) {
            return new Submission[size];
        }
    };

    @Bindable
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Bindable
    public String getName() {
        return name;
    }

    @Bindable
    public String getStatus() {
        return status;
    }

    @Bindable
    public String getOwnership() {
        return ownership;
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    @Bindable
    public int getYear() {
        return year;
    }

    @Bindable
    public String getWishes() {
        return wishes;
    }

    @Bindable
    public String getType() {
        return type;
    }

    @Bindable
    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    @Bindable
    public String getUploaderEmail() {
        return uploaderEmail;
    }

    public void setUploaderEmail(String uploaderEmail) {
        this.uploaderEmail = uploaderEmail;
    }

    @Bindable
    public String getUploaderUUID() {
        return uploaderUUID;
    }

    public void setUploaderUUID(String uploaderUUID) {
        this.uploaderUUID = uploaderUUID;
    }

    @Bindable
    public String getAddressString() {
        return addressString;
    }

    public void setAddressString(String addressString) {
        this.addressString = addressString;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOwnership(String ownership) {
        this.ownership = ownership;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setYear(int year) {
        this.year = year;
        notifyPropertyChanged(BR.year);
    }

    public void setWishes(String wishes) {
        this.wishes = wishes;
        notifyPropertyChanged(BR.wishes);
    }

    public void setType(String type) {
        this.type = type;
        notifyPropertyChanged(BR.type);
    }

    @Bindable
    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    @Bindable
    public GeoPoint getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoPoint geoLocation) {
        this.geoLocation = geoLocation;
    }

    @Bindable
    public String getBuildingStatus() {
        return buildingStatus;
    }

    public void setBuildingStatus(String buildingStatus) {
        this.buildingStatus = buildingStatus;
    }


    public boolean isValidatingFields() {
        return validatingFields;
    }

    public void setValidatingFields(boolean validatingFields) {
        this.validatingFields = validatingFields;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(name);
        dest.writeString(status);
        dest.writeString(addressString);
        dest.writeString(ownership);
        dest.writeString(description);
        dest.writeInt(year);
        dest.writeString(wishes);
        dest.writeString(type);
        dest.writeString(uploaderName);
        dest.writeString(uploaderEmail);
        dest.writeString(uploaderUUID);
        dest.writeStringList(photos);
        dest.writeString(buildingStatus);
    }
}
