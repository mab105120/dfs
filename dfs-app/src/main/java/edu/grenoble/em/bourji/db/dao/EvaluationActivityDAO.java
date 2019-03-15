package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.db.pojo.EvaluationActivity;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

/**
 * Created by Moe on 9/28/17.
 */
public class EvaluationActivityDAO extends AbstractDAO<EvaluationActivity> {

    public EvaluationActivityDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void add(EvaluationActivity activity) {
        persist(activity);
    }
}
