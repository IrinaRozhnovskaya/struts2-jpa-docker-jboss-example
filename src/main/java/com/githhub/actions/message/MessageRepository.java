package com.githhub.actions.message;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

import static com.githhub.actions.message.Message.FIND_ALL;
import static java.util.Collections.unmodifiableList;

@ApplicationScoped
public class MessageRepository {

    public static final Integer PAGE_SIZE = 20;

    @PersistenceContext
    EntityManager em;

    public void save(final Message message) {
        em.persist(message);
    }

    public List<Message> findAllOrderByCreatedAtDesc() {
        TypedQuery<Message> query = em.createNamedQuery(FIND_ALL, Message.class)
                .setMaxResults(PAGE_SIZE);
        List<Message> resultList = query.getResultList();
        return unmodifiableList(resultList);

    }
}
