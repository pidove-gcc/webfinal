/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mx.modelo.dao;

import com.ipn.mx.modelo.dto.UsuarioDTO;
import com.ipn.mx.utilerias.HibernateUtil;
import com.ipn.mx.utilerias.Utilerias;
import com.ipn.mx.web.bean.UsuarioMB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author starl
 */
public class UsuarioDAO {

    private Connection con;
    BasicDataSource basicDataSource = new BasicDataSource();

    public Connection conecta() throws SQLException {
        basicDataSource.setDriverClassName("org.postgresql.Driver");
        basicDataSource.setUsername("jgqsmfxyrhnuep");
        basicDataSource.setPassword("7cdf8ff522e0aaaee78f789a6a1ed49cc0456880bb3d83d53d1d01cfe0cb63e7");
        basicDataSource.setUrl("jdbc:postgres://jgqsmfxyrhnuep:7cdf8ff522e0aaaee78f789a6a1ed49cc0456880bb3d83d53d1d01cfe0cb63e7@ec2-54-161-208-31.compute-1.amazonaws.com:5432/d8jkca5obbq3ig");
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

    public void create(UsuarioDTO dto) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        String art = dto.getEntidad().getNombreUsuario();
        UsuarioMB mail = new UsuarioMB();
        String correo = mail.correo();
        Utilerias u = new Utilerias();
        try {
            transaction.begin();
            session.save(dto.getEntidad());
            transaction.commit();
            String asunto = "Registro de usuario";
            String texto = " Has registrado con exito al usuario: " + art;
            u.enviarEmail(correo, asunto, texto);
        } catch (HibernateException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();

            }
        }
    }

    public void update(UsuarioDTO dto) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        String art = dto.getEntidad().getNombreUsuario();
        UsuarioMB mail = new UsuarioMB();
        String correo = mail.correo();
        Utilerias u = new Utilerias();
        try {
            transaction.begin();
            session.update(dto.getEntidad());
            transaction.commit();
            String asunto = "Actualizacion de datos de usuario";
            String texto = " Se han guardado los cambios en el usuario: " + art;
            u.enviarEmail(correo, asunto, texto);
        } catch (HibernateException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();

            }
        }
    }

    public void delete(UsuarioDTO dto) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        String art = dto.getEntidad().getNombreUsuario();
        UsuarioMB mail = new UsuarioMB();
        String correo = mail.correo();
        Utilerias u = new Utilerias();
        try {
            transaction.begin();
            session.delete(dto.getEntidad());
            transaction.commit();
            String asunto = "Eliminacion de usuario";
            String texto = " Se ha eliminado con exito el usuario: " + art;
            u.enviarEmail(correo, asunto, texto);
        } catch (HibernateException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();

            }
        }
    }

    public UsuarioDTO load(UsuarioDTO dto) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        try {
            transaction.begin();
            dto.setEntidad(
                    session.get(dto.getEntidad().getClass(), dto.getEntidad().getIdUsuario()));
            transaction.commit();
        } catch (HibernateException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();

            }
        }
        return dto;
    }

    public List buscarTodos() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        List lista = null;
        try {
            transaction.begin();
            Query consulta = session.createQuery("from Usuario u order by u.idUsuario");
            lista = consulta.list();
            transaction.commit();
        } catch (HibernateException he) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        }
        return lista;
    }

    public String login(String user, String pass) throws SQLException {

        Connection cone = conecta();

        PreparedStatement ps = null;
        try {
            ps = cone.prepareStatement(
                    "SELECT nombreusuario, claveusuario,email FROM usuario WHERE nombreusuario= ? and claveusuario= ? ");
            ps.setString(1, user);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) // found
            {
                System.out.println(rs.getString("email"));
                String correo = rs.getString("email");
                return correo;
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "LoginDAO!",
                        "Wrong password message test!"));
                return null;
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Database Error",
                    "Unable to connect database"));
            System.out.println("Error in login() -->" + ex.getMessage());
            return null;
        } finally {

        }

    }
}
