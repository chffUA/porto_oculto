package pt.oitoo.portooculto.engine;

import pt.oitoo.portooculto.model.Submission;

public interface AdminDao {

    void listSubsmissions();
    void approveSubsmission(Submission building, String approval);
}
