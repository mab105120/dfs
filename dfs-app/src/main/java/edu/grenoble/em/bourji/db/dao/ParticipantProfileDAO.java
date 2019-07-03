package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.ParticipantProfiles;
import edu.grenoble.em.bourji.db.pojo.ParticipantProfile;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moe on 4/18/18.
 */
public class ParticipantProfileDAO extends AbstractDAO<ParticipantProfile> {

    public ParticipantProfileDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void add(String user, ParticipantProfiles profile) {
        persist(new ParticipantProfile(user, profile.name()));
    }

    public ParticipantProfile getParticipantProfile(String user) {
        Criteria cr = criteria();
        cr.add(Restrictions.eq("id", user));
        List profiles = cr.list();
        if(profiles.size() == 0)
            return null;
        else return (ParticipantProfile) profiles.get(0);
    }

    public List<ParticipantProfile> getParticipantProfiles(List<String> ids) {
        Criteria cr = criteria();
        cr.add(Restrictions.in("id", ids));
        List profiles = cr.list();
        List<ParticipantProfile> profilesList = new ArrayList<>();
        for(Object o: profiles) {
            ParticipantProfile profile = (ParticipantProfile) o;
            profilesList.add(profile);
        }
        return profilesList;
    }
}