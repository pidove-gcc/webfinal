/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.controller.web;

import com.ipn.mx.modelo.dao.UsuarioDAO;
import java.sql.SQLException;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author starl
 */
@Named
@RequestScoped
public class UserLoginView {
     
    private String username;
     
    private String password;
 
    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
 
    public String getPassword() { 
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
   
    public String login() throws SQLException {
        FacesMessage message = null;
        boolean loggedIn = false;
         UsuarioDAO dao = new UsuarioDAO();
         String consulta = dao.login(username, password);
        if(consulta != null ) {
            loggedIn = true;
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sesion Iniciada", "Iniciaste sesion como"+username));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario",username);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("correo",consulta);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setRedirect(true);
            return "/inicio?faces-redirect=true";
        } else {
            loggedIn = false;
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Datos incorrectos");
            
        }
         
        FacesContext.getCurrentInstance().addMessage(null, message);
        PrimeFaces.current().ajax().addCallbackParam("loggedIn", loggedIn);
        return "/login?faces-redirect=true";
    }   
}