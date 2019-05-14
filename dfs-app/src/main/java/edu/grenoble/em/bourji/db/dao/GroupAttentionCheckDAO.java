package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.db.pojo.GroupAttentionCheck;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

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
}
