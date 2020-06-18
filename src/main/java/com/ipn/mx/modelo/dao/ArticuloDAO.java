/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mx.modelo.dao;

import com.ipn.mx.modelo.dto.ArticuloDTO;
import com.ipn.mx.utilerias.HibernateUtil;
import com.ipn.mx.utilerias.Utilerias;
import com.ipn.mx.web.bean.UsuarioMB;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author alfre
 */
public class ArticuloDAO {

    private Connection con;
    BasicDataSource basicDataSource = new BasicDataSource();

    public Connection conecta() throws SQLException {
        basicDataSource.setDriverClassName("org.postgresql.Driver");
        basicDataSource.setUsername("postgres");
        basicDataSource.setPassword("tacosdepastor89");
        basicDataSource.setUrl("jdbc:postgresql://localhost:5432/tienda");
        basicDataSource.setValidationQuery("select 1");
        con = null;
        try {
            DataSource dataSource = basicDataSource;
            con = dataSource.getConnection();
            System.out.println("Conexion establecida");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;

    }

    public void create(ArticuloDTO dto) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        String art = dto.getEntidad().getNombreArticulo();
        UsuarioMB mail = new UsuarioMB();
        String correo = mail.correo();
        Utilerias u = new Utilerias();
        try {
            transaction.begin();
            session.save(dto.getEntidad());
            transaction.commit();
            String asunto = "Creacion de articulo";
            String texto = " Has registrado con exito el articulo: " + art;
            u.enviarEmail(correo, asunto, texto);
        } catch (HibernateException he) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    public void update(ArticuloDTO dto) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        String art = dto.getEntidad().getNombreArticulo();
        UsuarioMB mail = new UsuarioMB();
        String correo = mail.correo();
        Utilerias u = new Utilerias();
        try {
            transaction.begin();
            session.update(dto.getEntidad());
            transaction.commit();
            String asunto = "Actualizacion de articulo";
            String texto = " Se han guardado los cambios con exito en el articulo: " + art;
            u.enviarEmail(correo, asunto, texto);
        } catch (HibernateException he) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    public void delete(ArticuloDTO dto) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        String art = dto.getEntidad().getNombreArticulo();
        UsuarioMB mail = new UsuarioMB();
        String correo = mail.correo();
        Utilerias u = new Utilerias();
        try {
            transaction.begin();
            session.delete(dto.getEntidad());
            transaction.commit();
            String asunto = "Eliminacion de articulo";
            String texto = " Se ha eliminado con exito el articulo: " + art;
             u.enviarEmail(correo, asunto, texto);
        } catch (HibernateException he) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    public ArticuloDTO load(ArticuloDTO dto) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        try {
            transaction.begin();
            dto.setEntidad(
                    session.get(dto.getEntidad().getClass(), dto.getEntidad().getClaveArticulo())
            );

            transaction.commit();
        } catch (HibernateException he) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        }
        return dto;
    }

    public List<ArticuloDTO> buscarTodos() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        List<ArticuloDTO> lista;
        org.hibernate.query.Query query = session.createQuery("from Articulo ar order by ar.claveArticulo");
        lista = query.list();
        transaction.commit();
        return lista;
    }

    public List<ArticuloDTO> graficar() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        List<ArticuloDTO> lista;
        org.hibernate.query.Query query = session.createQuery("from Articulo ar order by ar.claveArticulo");
        lista = query.list();
        transaction.commit();
        return lista;

    }
}
