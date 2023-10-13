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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

/**
 *
 * @author Sebas
 */
public class MaterialDAO implements IMaterialDAO {

    private SessionFactory sessionFactory;

    public MaterialDAO() {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Material.class).buildSessionFactory();
    }

    @Override
    public Boolean create(Material material) {
        String codigo = material.getCodigo();
        var succes = false;

        Material existingMaterial = readByCodigo(codigo, material.getCliente().getRuc());
        if (existingMaterial != null) {
            return false;
        } else {
            Session session = sessionFactory.openSession();
            Transaction transaction = null;

            try {
                transaction = session.beginTransaction();
                session.persist(material);
                transaction.commit();
                succes = true;
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                    succes = false;
                }
                e.printStackTrace();
            } finally {
                session.close();
            }
        }

        return succes;
    }

    @Override
    public List<Material> readAll() {
        Session session = sessionFactory.getCurrentSession();
        List<Material> materiales = new ArrayList<>();
        try {
            session.beginTransaction();
            materiales = session.createQuery("SELECT m FROM Material m", Material.class).getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
        return materiales;
    }

    // Otros métodos según tus necesidades...
    @Override
    public Boolean delete(String subpartida, String ruc) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            Material reporte = readByCodigo(subpartida, ruc);
            if (reporte != null) {
                session.delete(reporte);
                transaction.commit();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public Material readByCodigo(String codigo, String ruc) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Material material = null;

        try {
            transaction = session.beginTransaction();
            material = session.createQuery("SELECT m FROM Material m WHERE m.codigo = :codigo AND m.cliente.ruc=:ruc", Material.class)
                    .setParameter("codigo", codigo)
                    .setParameter("ruc", ruc)
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return material;
    }

    public Boolean update(Material material) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        var success = false;
        try {
            transaction = session.beginTransaction();
            Material existingMaterial = readByCodigo(material.getCodigo(), material.getCliente().getRuc());

            if (existingMaterial != null) {
                // Comprobar si los nuevos valores son nulos antes de asignarlos
                if (material.getSubpartida() != null) {
                    existingMaterial.setSubpartida(material.getSubpartida());
                }
                if (material.getDescripcion() != null) {
                    existingMaterial.setDescripcion(material.getDescripcion());
                }
                if (material.getTipoUnidad() != null) {
                    existingMaterial.setTipoUnidad(material.getTipoUnidad());
                }
                if (material.getSaldoInsumo() != null) {
                    existingMaterial.setSaldoInsumo(material.getSaldoInsumo());
                }
                if (material.getPorcentajeMerma() != null) {
                    existingMaterial.setPorcentajeMerma(material.getPorcentajeMerma());
                }
                if (material.getCoeficienteConsumo() != null) {
                    existingMaterial.setCoeficienteConsumo(material.getCoeficienteConsumo());
                }
                if (material.getPorcentajeDesperdicio() != null) {
                    existingMaterial.setPorcentajeDesperdicio(material.getPorcentajeDesperdicio());
                }
                if (material.getCalculaDesperdicio() != null) {
                    existingMaterial.setCalculaDesperdicio(material.getCalculaDesperdicio());
                }
                if (material.getAplicaFormula() != null) {
                    existingMaterial.setAplicaFormula(material.getAplicaFormula());
                }
                if (material.getCliente() != null) {
                    existingMaterial.setCliente(material.getCliente());
                }

                // ... Actualizar otros atributos según sea necesario ...
                session.update(existingMaterial); // Actualizar el material en la base de datos
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

    @Override
    public Boolean deleteAll() {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery("DELETE FROM Material");
            int deletedCount = query.executeUpdate();
            transaction.commit();
            return deletedCount > 0;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
}
