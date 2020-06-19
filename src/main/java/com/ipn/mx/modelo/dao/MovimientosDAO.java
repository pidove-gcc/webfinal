/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mx.modelo.dao;

import com.ipn.mx.modelo.dto.ArticuloDTO;
import com.ipn.mx.modelo.dto.MovimientosDTO;
import com.ipn.mx.utilerias.HibernateUtil;
import com.ipn.mx.utilerias.Utilerias;
import com.ipn.mx.web.bean.UsuarioMB;
import static java.lang.System.exit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author alfre
 */
public class MovimientosDAO {

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

    public void create(MovimientosDTO dto) throws SQLException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        int clave = dto.getEntidad().getClaveArticulo();
        Long ide = new Long(clave);
        String art = dto.getEntidad().getTipoMov();
        int can = dto.getEntidad().getCantidad();
         int salida= ci(ide, can, art);
         if (salida<0) {
             
              FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error ",
                    "No se puede realizar la operacion"));
              return;
            
        }
         actart(ide, salida);
        if (art.equals("S")) {
            art = "Salida";

        } else {
            art = "Entrada";
        }
        UsuarioMB mail = new UsuarioMB();
        String correo = mail.correo();
        Utilerias u = new Utilerias();
        try {
            transaction.begin();
            session.save(dto.getEntidad());
            transaction.commit();
            String asunto = "Registro de movimiento";
            String texto = " Has registrado un movimiento de : " + art + " en el sistema";
            u.enviarEmail(correo, asunto, texto);
        } catch (HibernateException he) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    public void update(MovimientosDTO dto) throws SQLException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
         int clave = dto.getEntidad().getClaveArticulo();
        Long id = new Long(clave);
        String art = dto.getEntidad().getTipoMov();
        Long ide = dto.getEntidad().getIdMovimiento();
        int can = dto.getEntidad().getCantidad();
        int salida= ci(id, can, art);
        if (salida<0) {
             
              FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error ",
                    "No se puede realizar la operacion"));
              return;
            
        }
         actart(id, salida);
        if (art.equals("S")) {
            art = "Salida";
        } else {
            art = "Entrada";
        }
        UsuarioMB mail = new UsuarioMB();
        String correo = mail.correo();
        Utilerias u = new Utilerias();
        try {
            transaction.begin();
            session.update(dto.getEntidad());
            transaction.commit();
            String asunto = "Actualizacion de movimiento";
            String texto = " Se ha actualizado el  movimiento con el id : " + ide + " a uno de tipo: " + art;
            u.enviarEmail(correo, asunto, texto);
        } catch (HibernateException he) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    public void delete(MovimientosDTO dto) throws SQLException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        int clave = dto.getEntidad().getClaveArticulo();
        Long id = new Long(clave);
        String art = dto.getEntidad().getTipoMov();
        if (art.equals("E")) {
            art="S";
        }
        else if (art.equals("S")) {
            art="E";
        }
        Long ide = dto.getEntidad().getIdMovimiento();
         int can = dto.getEntidad().getCantidad();
        int salida= ci(id, can, art);
        if (salida<0) {
             
              FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error ",
                    "No se puede realizar la operacion"));
              return;
            
        }
         actart(id, salida);
        UsuarioMB mail = new UsuarioMB();
        String correo = mail.correo();
        Utilerias u = new Utilerias();
        try {
            transaction.begin();
            session.delete(dto.getEntidad());
            transaction.commit();
            String asunto = "Eliminacion de movimiento";
            String texto = " Se ha eliminado el movimiento con el id : " + ide;
            u.enviarEmail(correo, asunto, texto);
        } catch (HibernateException he) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    public MovimientosDTO load(MovimientosDTO dto) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        try {
            transaction.begin();
            dto.setEntidad(
                    session.get(dto.getEntidad().getClass(), dto.getEntidad().getIdMovimiento())
            );

            transaction.commit();
        } catch (HibernateException he) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        }
        return dto;
    }

    public List<MovimientosDTO> buscarTodos() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        List<MovimientosDTO> lista;
        org.hibernate.query.Query query = session.createQuery("from Movimientos mov order by mov.idMovimiento");
        lista = query.list();
        transaction.commit();
        return lista;
    }

    public List<ArticuloDTO> getproductos() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        List<ArticuloDTO> lista;
        org.hibernate.query.Query query = session.createQuery("from Articulo ar order by ar.claveArticulo  ");
        lista = query.list();
        transaction.commit();
        return lista;
    }

    public int ci(Long ide, int cant, String tm) throws SQLException {

        Connection cone = conecta();
        String can = null;
        int num;

        PreparedStatement ps = null;
        try {
            ps = cone.prepareStatement(
                    "SELECT existencia FROM articulo WHERE clavearticulo= ? ");
            ps.setLong(1, ide);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) // found
            {
                System.out.println(rs.getString("existencia"));
                can = rs.getString("existencia");

            }
            num = Integer.parseInt(can);
            if (tm.equals("E")) {

                return num + cant;

            } else {
                return num - cant;
            }

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Database Error",
                    "Unable to connect database"));
            System.out.println("Error in login() -->" + ex.getMessage());

        } finally {
            
            if(cone != null) cone.close();  

        }

        return 0;

    }

    public void actart(Long ide, int cant) throws SQLException {

        Connection cone = conecta();

        PreparedStatement ps = null;
        try {
            ps = cone.prepareStatement(
                    "update articulo set existencia=? WHERE clavearticulo= ? ");
            ps.setInt(1, cant);
            ps.setLong(2, ide);

            ResultSet rs = ps.executeQuery();
            
        } catch (Exception ex) {
            System.out.println("Error in login() -->" + ex.getMessage());

        } finally {
            
            if(cone != null) cone.close(); 

        }

    }
    
    public static void main(String[] args) throws SQLException {
        MovimientosDAO dao = new MovimientosDAO();
        Long ide = Long.parseLong("6");
       int salida= dao.ci(ide, 10, "E");
        if (salida<0) {
            
            System.out.println("No se puede realizar la operacion");
            
        }
        else{
            dao.actart(ide, salida);
            System.out.println("El stock queda de: " + salida);
            
        }
       
        
    }
}
