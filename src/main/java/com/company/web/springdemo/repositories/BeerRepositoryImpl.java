package com.company.web.springdemo.repositories;

import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.FilterOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BeerRepositoryImpl implements BeerRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public BeerRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Beer> get(FilterOptions filterOptions) {
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filterOptions.getName().ifPresent(value -> {
                filters.add("name like :name");
                params.put("name", String.format("%%%s%%", value));
            });

            filterOptions.getMinAbv().ifPresent(value -> {
                filters.add("abv >= :minAbv");
                params.put("minAbv", value);
            });

            filterOptions.getMaxAbv().ifPresent(value -> {
                filters.add("abv <= :maxAbv");
                params.put("maxAbv", value);
            });

            filterOptions.getStyleId().ifPresent(value -> {
                filters.add("style.id = :styleId");
                params.put("styleId", value);
            });

            StringBuilder queryString = new StringBuilder("from Beer");
            if (!filters.isEmpty()) {
                queryString
                        .append(" where ")
                        .append(String.join(" and ", filters));
            }
            queryString.append(generateOrderBy(filterOptions));

            Query<Beer> query = session.createQuery(queryString.toString(), Beer.class);
            query.setProperties(params);
            return query.list();
        }
    }

    @Override
    public Beer get(int id) {
        try (Session session = sessionFactory.openSession()) {
            Beer beer = session.get(Beer.class, id);
            if (beer == null) {
                throw new EntityNotFoundException("Beer", id);
            }
            return beer;
        }
    }

    @Override
    public Beer get(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Beer> query = session.createQuery("from Beer where name = :name", Beer.class);
            query.setParameter("name", name);

            List<Beer> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("Beer", "name", name);
            }

            return result.get(0);
        }
    }

    @Override
    public void create(Beer beer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(beer);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Beer beer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(beer);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Beer beerToDelete = get(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(beerToDelete);
            session.getTransaction().commit();
        }
    }

    private String generateOrderBy(FilterOptions filterOptions) {
        if (filterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = "";
        switch (filterOptions.getSortBy().get()) {
            case "name":
                orderBy = "name";
                break;
            case "abv":
                orderBy = "abv";
                break;
            case "style":
                orderBy = "style.name";
                break;
            default:
                return "";
        }

        orderBy = String.format(" order by %s", orderBy);

        if (filterOptions.getSortOrder().isPresent() && filterOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }

}
