package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.db.pojo.UserDemographic;
import edu.grenoble.em.bourji.db.pojo.UserSubmissionIdentifier;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Moe on 9/9/2017.
 */
public class UserDemographicDAO extends AbstractDAO<UserDemographic> {

    public UserDemographicDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void add(UserDemographic user) {
        persist(user);
    }

    public UserDemographic getUserDemographics(String userId) {
        int submissionId = getNextSubmissionId(userId) - 1;
        return get(new UserSubmissionIdentifier(userId, submissionId));
    }

    public int getNextSubmissionId(String user) {
        Criteria cr = currentSession().createCriteria(UserDemographic.class);
        cr.add(Restrictions.eq("user", user));
        cr.setProjection(Projections.distinct(Projections.property("submissionId")));
        List<Integer> submissionIds = cr.list();
        submissionIds.sort(Comparator.naturalOrder());
        return submissionIds.isEmpty() ? 0 : submissionIds.get(submissionIds.size() - 1) + 1;
    }
}
