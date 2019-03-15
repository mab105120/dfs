package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.db.pojo.Activity;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

/**
 * Created by Moe on 9/24/17.
 */
public class ActivityDAO extends AbstractDAO<Activity> {
    public ActivityDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void add(Activity activity) {
        persist(activity);
    }
}
