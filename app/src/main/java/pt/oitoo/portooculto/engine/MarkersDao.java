package pt.oitoo.portooculto.engine;

public interface MarkersDao {

    void retrieveMarkers();
    void retrieveMarkersByUser(String uid);
}
