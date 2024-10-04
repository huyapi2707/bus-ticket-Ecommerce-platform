package org.huydd.bus_ticket_Ecommercial_platform.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Route;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RouteDAO {
    private final EntityManager entityManager;


    public Long countByFromSiteAndToSiteAndDepartDate(int fromSite,
                                                     int toSite,
                                                     Timestamp fromDate,
                                                     Timestamp toDate) {
        List<Object> params = new ArrayList<>();
        String sqlQuery = "SELECT count(r.id)\n" +
                "FROM Trip as t\n" +
                "JOIN Route as r ON t.route.id = r.id\n" +
                "JOIN Station as fst ON r.fromStation.id = fst.id\n" +
                "JOIN Station as tst ON r.toStation.id = tst.id\n" +
                "JOIN Site as fs ON fst.site.id = fs.id\n" +
                "JOIN Site as ts ON tst.site.id = ts.id\n" +
                "WHERE r.isActive = true\n";
        if (fromSite > 0) {
            sqlQuery += String.format("AND fs.id = ?%d\n", params.size() + 1);
            params.add(fromSite);
        }
        if (toSite > 0) {
            sqlQuery += String.format("AND ts.id = ?%d\n", params.size() + 1);
            params.add(toSite);
        }

        if (fromDate != null) {
            sqlQuery += String.format("AND t.departAt >= ?%d\n", params.size() + 1);
            params.add(fromDate);
        }
        if (toDate != null) {
            sqlQuery += String.format("AND t.departAt <= ?%d\n", params.size() + 1);
            params.add(toDate);
        }
        Query query = entityManager.createQuery(sqlQuery);
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i+ 1, params.get(i));
        }
        return (Long) query.getSingleResult();
    }

    public List<Route> findAllByFromSiteAndToSiteAndDepartDate(int fromSite,
                                                        int toSite,
                                                        Timestamp fromDate,
                                                        Timestamp toDate,
                                                        int page) {
        List<Object> params = new ArrayList<>();

        String sqlQuery = "SELECT r\n" +
                "FROM Trip as t\n" +
                "JOIN Route as r ON t.route.id = r.id\n" +
                "JOIN Station as fst ON r.fromStation.id = fst.id\n" +
                "JOIN Station as tst ON r.toStation.id = tst.id\n" +
                "JOIN Site as fs ON fst.site.id = fs.id\n" +
                "JOIN Site as ts ON tst.site.id = ts.id\n" +
                "WHERE r.isActive = true\n";
        if (fromSite > 0) {
            sqlQuery += String.format("AND fs.id = ?%d\n", params.size() + 1);
            params.add(fromSite);
        }
        if (toSite > 0) {
            sqlQuery += String.format("AND ts.id = ?%d\n", params.size() + 1);
            params.add(toSite);
        }

        if (fromDate != null) {
            sqlQuery += String.format("AND t.departAt >= ?%d\n", params.size() + 1);
            params.add(fromDate);
        }
        if (toDate != null) {
            sqlQuery += String.format("AND t.departAt <= ?%d\n", params.size() + 1);
            params.add(toDate);
        }

        Query query = entityManager.createQuery(sqlQuery);
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i+ 1, params.get(i));
        }
        if (page > 0) {
            query.setMaxResults(15);
            query.setFirstResult(page - 1);
        }
        return query.getResultList();
    }
}
