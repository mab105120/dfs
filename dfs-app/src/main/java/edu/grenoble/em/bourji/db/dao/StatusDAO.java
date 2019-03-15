package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.api.ProgressStatus;
import edu.grenoble.em.bourji.db.pojo.Status;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Moe on 9/23/2017.
 */
public class StatusDAO extends AbstractDAO<Status> {

    public StatusDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public ProgressStatus getCurrentStatus(String user) {
        Criteria cr = currentSession().createCriteria(Status.class)
                .add(Restrictions.eq("user", user))
                .addOrder(Order.desc("time"));
        List status = list(cr);
        return status.size() == 0 ? ProgressStatus.NOT_STARTED : ProgressStatus.valueOf((String) status.get(0));
    }

    public List<ProgressStatus> getProgress(String user) {
        Criteria cr = currentSession().createCriteria(Status.class)
                .add(Restrictions.eq("user", user));
        cr.setProjection(Projections.distinct(Projections.property("status")));
        List<String> distinctStatuses = cr.list();
        List<ProgressStatus> progressStatuses = distinctStatuses.stream().map(ProgressStatus::valueOf).collect(Collectors.toList());
        progressStatuses.sort(Comparator.naturalOrder());
        return progressStatuses;
    }

    public void add(Status s) {
        persist(s);
    }

    public boolean stepCompleted(String user, ProgressStatus status) {
        Criteria cr = currentSession().createCriteria(Status.class)
                .add(Restrictions.eq("user", user))
                .add(Restrictions.eq("status", status.name()));
        return !list(cr).isEmpty();
    }
}
