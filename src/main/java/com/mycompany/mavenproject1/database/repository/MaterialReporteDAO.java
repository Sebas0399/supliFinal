/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.database.repository;

import com.mycompany.mavenproject1.database.model.Cliente;
import com.mycompany.mavenproject1.database.model.Material;
import com.mycompany.mavenproject1.database.model.MaterialReporte;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Sebas
 */
@Transactional
public class MaterialReporteDAO implements IMaterialReporteDAO {

    private SessionFactory sessionFactory;

    public MaterialReporteDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Boolean create(MaterialReporte material) {
        String codigo = material.getCodigo();
        MaterialReporte existingMaterial = readByCodigo(codigo);

        if (existingMaterial != null) {
            return false; // El material ya existe
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(material);
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<MaterialReporte> readAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT m FROM MaterialReporte m", MaterialReporte.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Boolean delete(String codigo) {
        MaterialReporte reporte = readByCodigo(codigo);

        if (reporte != null) {
            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                session.delete(reporte);
                transaction.commit();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false; // No se encontró el material
        }
    }

    @Override
    public MaterialReporte readByCodigo(String codigo) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT m FROM MaterialReporte m WHERE m.codigo = :codigo", MaterialReporte.class)
                    .setParameter("codigo", codigo)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean update(MaterialReporte material, String codigo) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        var success = false;
        try {
            transaction = session.beginTransaction();
            MaterialReporte existingMaterial = readByCodigo(codigo);

            if (existingMaterial != null) {
                System.out.println("Existe el material");

                // Comprobar si los nuevos valores son nulos antes de asignarlos
                if (material.getDescripcion() != null) {
                    existingMaterial.setDescripcion(material.getDescripcion());
                }
                if (material.getCodigo() != null) {
                    existingMaterial.setCodigo(material.getCodigo());
                }

                if (material.getCliente() != null) {
                    existingMaterial.setCliente(material.getCliente());
                }

                session.update(existingMaterial);
            }
            transaction.commit();
            success = true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                success = false;
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return success;
    }
}
