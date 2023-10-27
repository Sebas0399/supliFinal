/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.database.DAO;

import com.mycompany.mavenproject1.database.model.Reporte;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Sebas
 */
@Transactional

public class ReporteDAO implements IReporteDAO {

    private SessionFactory sessionFactory;

    public ReporteDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Reporte reporte) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.persist(reporte);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAll() {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        try {
            Query query = session.createQuery("DELETE FROM Reporte");
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Reporte read() {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        try {
            Query<Reporte> query = session.createQuery("FROM Reporte", Reporte.class);
            Reporte reporte = query.uniqueResult();
            transaction.commit();
            return reporte;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
            return null; // O lanza una excepción apropiada en tu aplicación
        }
    }

}
