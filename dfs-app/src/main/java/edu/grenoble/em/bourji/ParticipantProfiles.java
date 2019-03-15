package edu.grenoble.em.bourji;

import edu.grenoble.em.bourji.api.ParticipantProfile;

/**
 * Created by Moe on 4/18/18.
 */
public enum ParticipantProfiles {

    NFS(new ParticipantProfile(false, false, 4, 2, "high", 20, ExperimentMode.DFS)),
    DFS(new ParticipantProfile(false, false, 4, 2, "high", 20, ExperimentMode.DFS)),
    IFS(new ParticipantProfile(false, false, 4, 2, "high", 20, ExperimentMode.DFS));

    private ParticipantProfile profile;
    ParticipantProfiles(ParticipantProfile profile) {
        this.profile = profile;
    }

    public ParticipantProfile getProfile() {
        return this.profile;
    }
}