package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.db.pojo.AttentionCheckStatus;
import edu.grenoble.em.bourji.db.pojo.GroupAttentionCheck;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

import static edu.grenoble.em.bourji.db.pojo.AttentionCheckStatus.FAIL;
import static edu.grenoble.em.bourji.db.pojo.AttentionCheckStatus.PASS;
import static edu.grenoble.em.bourji.db.pojo.AttentionCheckStatus.PENDING;

/**
 * Created by Moe on 5/6/19.
 */
public class GroupAttentionCheckDAO extends AbstractDAO<GroupAttentionCheck> {

    public GroupAttentionCheckDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void add(GroupAttentionCheck record) {
        persist(record);
    }

    public AttentionCheckStatus getStatus(String user) {
        Criteria cr = criteria();
        cr.add(Restrictions.eq("id", user));
        List result = cr.list();
        if (result.isEmpty()) return PENDING;
        GroupAttentionCheck record = (GroupAttentionCheck) result.get(0);
        return record.isPass() ? PASS : FAIL;
    }
}
