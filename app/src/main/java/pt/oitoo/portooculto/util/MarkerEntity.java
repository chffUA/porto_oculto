package pt.oitoo.portooculto.util;

import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import pt.oitoo.portooculto.model.Submission;

public class MarkerEntity {

    private MarkerOptions markerOptions;
    private List<String> photoIds;
    private String pointId;
    private Submission submission;

    public MarkerEntity(MarkerOptions markerOptions, List<String> photoIds, String pointId, Submission submission) {
        this.markerOptions = markerOptions;
        this.photoIds = photoIds;
        this.pointId = pointId;
        this.submission = submission;
    }

    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }

    public List<String> getPhotoIds() {
        return photoIds;
    }

    public String getPointId() {
        return pointId;
    }

    public Submission getSubmission() {
        return submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }
}
