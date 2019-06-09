package pt.oitoo.portooculto.engine;

import pt.oitoo.portooculto.model.Post;
import pt.oitoo.portooculto.model.Submission;

public interface SubmissionsDao {

    void uploadNewBuilding(Post post);
    void addDetailedInfo(Submission submission);
}
