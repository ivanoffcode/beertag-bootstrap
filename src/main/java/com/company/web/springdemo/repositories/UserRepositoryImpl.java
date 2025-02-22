package com.company.web.springdemo.repositories;

import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> get() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User", User.class);
            return query.list();
        }
    }

    @Override
    public User get(int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User", id);
            }
            return user;
        }
    }

    @Override
    public User get(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where username = :username", User.class);
            query.setParameter("username", username);

            List<User> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("User", "username", username);
            }

            return result.get(0);
        }
    }

    @Override
    public void create(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
    }

}
