package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.db.pojo.UserRecipOrientation;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Moe on 9/22/19.
 */
public class UserRecipOrientationDAO extends AbstractDAO<UserRecipOrientation> {
    public UserRecipOrientationDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void add(UserRecipOrientation userRecipOrientation) {
        persist(userRecipOrientation);
    }

    public void addAll(List<UserRecipOrientation> userRecipOrientationList) {
        userRecipOrientationList.stream().forEach(this::add);
    }

    public List<UserRecipOrientation> getUserRecipOrientation(String user) {
        Criteria cr = currentSession().createCriteria(UserRecipOrientation.class)
                .add(Restrictions.eq("user", user))
                .add(Restrictions.eq("submissionId", getLastSubmission(user)));
        return cr.list();
    }

    private int getLastSubmission(String user) {
        Criteria cr = currentSession().createCriteria(UserRecipOrientation.class);
        cr.add(Restrictions.eq("user", user));
        cr.setProjection(Projections.distinct(Projections.property("submissionId")));
        List<Integer> submissionIds = cr.list();
        submissionIds.sort(Comparator.naturalOrder());
        return submissionIds.isEmpty() ? 0 : submissionIds.get(submissionIds.size() - 1);
    }

    public int getNextSubmissionId(String user) {
        int nextSubmissionId = getLastSubmission(user);
        return nextSubmissionId == 0 ? 0 : nextSubmissionId + 1;
    }
}
