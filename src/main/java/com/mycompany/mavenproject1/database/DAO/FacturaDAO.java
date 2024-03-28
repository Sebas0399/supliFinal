/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.database.DAO;

import com.mycompany.mavenproject1.database.model.Factura;
import com.mycompany.mavenproject1.database.model.MaterialReporte;
import java.time.LocalDate;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author Administrator
 */
public class FacturaDAO implements IFacturaDAO {

    private SessionFactory sessionFactory;

    public FacturaDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Boolean create(Factura factura) {
        String numero = factura.getNroFactura();
        String ruc = factura.getRucCliente();
        Factura existingFactura = readByNumeroAndRuc(numero, ruc);

        if (existingFactura != null) {
            return false; // El material ya existe
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(factura);
            transaction.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void delete(String numero, String rucCliente) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Factura readByNumeroAndRuc(String numero, String rucCliente) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT f FROM Factura f WHERE f.numero = :numero AND f.rucCliente=:rucCliente", Factura.class)
                    .setParameter("numero", numero)
                    .setParameter("rucCliente", rucCliente)
                    .uniqueResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Factura> filterByFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT f FROM Factura f WHERE f.fecha BETWEEN :fechaInicio AND :fechaFin", Factura.class)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

}
