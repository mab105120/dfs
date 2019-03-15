package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.db.pojo.UserExperience;
import edu.grenoble.em.bourji.db.pojo.UserSubmissionIdentifier;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Moe on 9/11/17.
 */
public class UserExperienceDAO extends AbstractDAO<UserExperience> {

    public UserExperienceDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void add(UserExperience user) {
        persist(user);
    }

    public UserExperience getUserExperience(String userId) {
        return get(new UserSubmissionIdentifier(userId, getLatestSubmissionId(userId)));
    }

    private int getLatestSubmissionId(String user) {
        return getNextSubmissionId(user) - 1;
    }

    public int getNextSubmissionId(String user) {
        Criteria cr = currentSession().createCriteria(UserExperience.class);
        cr.add(Restrictions.eq("user", user));
        cr.setProjection(Projections.distinct(Projections.property("submissionId")));
        List<Integer> submissionId = cr.list();
        submissionId.sort(Comparator.naturalOrder());
        return submissionId.isEmpty() ? 0 : submissionId.get(submissionId.size() - 1) + 1;
    }
}
