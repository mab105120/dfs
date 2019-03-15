package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.db.pojo.AbsoluteEvaluation;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Moe on 4/11/18.
 */
public class AbsoluteEvaluationDao extends AbstractDAO<AbsoluteEvaluation> {

    public AbsoluteEvaluationDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void add(AbsoluteEvaluation e) {
        persist(e);
    }

    private int getLastSubmissionId(String user, String evaluationCode) {
        Criteria cr = currentSession().createCriteria(AbsoluteEvaluation.class);
        cr.add(Restrictions.eq("user", user));
        cr.add(Restrictions.eq("evaluationCode", evaluationCode));
        cr.setProjection(Projections.distinct(Projections.property("submissionId")));
        List<Integer> submissionIds = cr.list();
        submissionIds.sort(Comparator.naturalOrder());
        return submissionIds.isEmpty() ? 0 : submissionIds.get(submissionIds.size() - 1);
    }

    public int getNextSubmissionId(String user, String evaluationCode) {
        return getLastSubmissionId(user, evaluationCode) + 1;
    }

    public AbsoluteEvaluation getEvaluation(String userId, String evalCode) {
        Criteria cr = currentSession().createCriteria(AbsoluteEvaluation.class);
        cr.add(Restrictions.eq("user", userId))
                .add(Restrictions.eq("evaluationCode", evalCode))
                .add(Restrictions.eq("submissionId", getLastSubmissionId(userId, evalCode)));
        List<AbsoluteEvaluation> result = cr.list();
        if(result.isEmpty())
            throw new RuntimeException("Unable to find evaluation record for " + userId + ", eval code: " + evalCode);
        return result.get(0);
    }

}