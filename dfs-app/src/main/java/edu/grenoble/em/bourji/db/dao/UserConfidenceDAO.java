package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.db.pojo.UserConfidence;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Moe on 9/12/2017.
 */
public class UserConfidenceDAO extends AbstractDAO<UserConfidence> {
    public UserConfidenceDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void add(UserConfidence userConfidence) {
        persist(userConfidence);
    }

    public void addAll(List<UserConfidence> userConfidenceList) {
        userConfidenceList.stream().forEach(this::add);
    }

    public List<UserConfidence> getUserConfidence(String user) {
        Criteria cr = currentSession().createCriteria(UserConfidence.class)
                .add(Restrictions.eq("user", user))
                .add(Restrictions.eq("submissionId", getLastSubmission(user)));
        return cr.list();
    }

    private int getLastSubmission(String user) {
        Criteria cr = currentSession().createCriteria(UserConfidence.class);
        cr.add(Restrictions.eq("user", user));
        cr.setProjection(Projections.distinct(Projections.property("submissionId")));
        List<Integer> submissionIds = cr.list();
        submissionIds.sort(Comparator.naturalOrder());
        return submissionIds.isEmpty() ? 0 : submissionIds.get(submissionIds.size() - 1);
    }

    public int getNextSubmissionId(String user) {
        return getLastSubmission(user) + 1;
    }
}
