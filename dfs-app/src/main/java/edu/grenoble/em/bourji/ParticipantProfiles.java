package edu.grenoble.em.bourji;

import edu.grenoble.em.bourji.api.ParticipantProfile;

/**
 * Created by Moe on 4/18/18.
 */
public enum ParticipantProfiles {

    NFS(new ParticipantProfile(false, true, 2, 2, "high", 10, ExperimentMode.NFS)),
    NFS_T(new ParticipantProfile(false, false, true, 2, 2, "high", 10, ExperimentMode.NFS)),
    DFS_T(new ParticipantProfile(false, true, true, 2, 2, "high", 8, ExperimentMode.DFS)),
    DFS(new ParticipantProfile(false, true, 2, 2, "high", 8, ExperimentMode.DFS)),
    IFS(new ParticipantProfile(false, true, 2, 2, "high", 8, ExperimentMode.IFS));

    private ParticipantProfile profile;
    ParticipantProfiles(ParticipantProfile profile) {
        this.profile = profile;
    }

    public ParticipantProfile getProfile() {
        return this.profile;
    }
}