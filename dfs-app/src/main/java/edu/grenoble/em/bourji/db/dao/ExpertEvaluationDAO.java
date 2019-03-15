package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.db.pojo.ExpertEvaluation;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Moe on 5/7/18.
 */
public class ExpertEvaluationDAO extends AbstractDAO<ExpertEvaluation>{
    public ExpertEvaluationDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public ExpertEvaluation get(String usertId, String evalCode) {
        Criteria cr = criteria().add(Restrictions.eq("id", usertId))
                .add(Restrictions.eq("evalCode", evalCode));
        List result = cr.list();
        if(result.isEmpty()) return null;
        return (ExpertEvaluation) result.get(0);
    }

    public void add(ExpertEvaluation evaluation) {
        persist(evaluation);
    }
}
