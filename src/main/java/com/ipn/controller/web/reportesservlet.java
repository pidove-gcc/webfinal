/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.controller.web;

import com.ipn.mx.modelo.dao.UsuarioDAO;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 *
 * @author alfre
 */
@WebServlet(name = "reportesservlet", urlPatterns = {"/reportesservlet"})
public class reportesservlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, JRException {
        String accion = request.getParameter("accion");
        if(accion.equals("reporteentrada")){
            reporteEntrada(request, response);
        }else{
            if(accion.equals("reportesalida")){
                reporteSalida(request, response);
            }else{
                if(accion.equals("reporteusuarios")){
                    reporteUsuarios(request, response);
                }
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(reportesservlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(reportesservlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(reportesservlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(reportesservlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void reporteEntrada(HttpServletRequest request, HttpServletResponse response) throws SQLException, JRException{
        UsuarioDAO dao = new UsuarioDAO();
        try {
            try (ServletOutputStream sos = response.getOutputStream()) {
                File reporte = new File(getServletConfig().getServletContext().getRealPath("/reportes/ReporteEntrada.jasper"));
                byte[] bytes;
                bytes = JasperRunManager.runReportToPdf(reporte.getPath(),null,dao.conecta());
                response.setContentType("application/pdf");
                response.setContentLength(bytes.length);
                sos.write(bytes, 0, bytes.length);
                sos.flush();
               
            }
        } catch (IOException e) {
        }
    }

    private void reporteSalida(HttpServletRequest request, HttpServletResponse response) throws SQLException, JRException {
        UsuarioDAO dao = new UsuarioDAO();
        try {
            try (ServletOutputStream sos = response.getOutputStream()) {
                File reporte = new File(getServletConfig().getServletContext().getRealPath("/reportes/ReporteSalida.jasper"));
                byte[] bytes;
                bytes = JasperRunManager.runReportToPdf(reporte.getPath(),null,dao.conecta());
                response.setContentType("application/pdf");
                response.setContentLength(bytes.length);
                sos.write(bytes, 0, bytes.length);
                sos.flush(); 
               
            }
        } catch (IOException e) {
        }
    }

    private void reporteUsuarios(HttpServletRequest request, HttpServletResponse response) throws SQLException, JRException {
        UsuarioDAO dao = new UsuarioDAO();
        try {
            try (ServletOutputStream sos = response.getOutputStream()) {
                File reporte = new File(getServletConfig().getServletContext().getRealPath("/reportes/ListaUsuarios.jasper"));
                byte[] bytes;
                bytes = JasperRunManager.runReportToPdf(reporte.getPath(),null,dao.conecta());
                response.setContentType("application/pdf");
                response.setContentLength(bytes.length);
                sos.write(bytes, 0, bytes.length);
                sos.flush();
               
            }
        } catch (IOException e) {
        }
    }
}
