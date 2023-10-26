/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.database.DAO;

import com.mycompany.mavenproject1.database.model.Cliente;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Sebas
 */
public class ClienteDAO implements IClienteDAO {

    private SessionFactory sessionFactory;

    public ClienteDAO() {
        // Se inicializa la SessionFactory una vez y se reutiliza
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Cliente.class).buildSessionFactory();
    }

    @Override
    public void create(Cliente cliente) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            session.persist(cliente);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    @Override
    public List<Cliente> readAll() {
        Session session = sessionFactory.getCurrentSession();
        List<Cliente> clientes = new ArrayList<>();
        try {
            session.beginTransaction();
            clientes = session.createQuery("FROM Cliente", Cliente.class).getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
        return clientes;
    }

    @Override
    public Cliente readByRuc(String ruc) {
        Session session = sessionFactory.getCurrentSession();
        Cliente cliente = null;
        try {
            session.beginTransaction();
            cliente = session.createQuery("FROM Cliente WHERE ruc = :ruc", Cliente.class)
                    .setParameter("ruc", ruc)
                    .uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
        return cliente;
    }
}
