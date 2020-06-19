/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mx.web.bean;

import com.ipn.mx.modelo.dao.UsuarioDAO;
import com.ipn.mx.modelo.dto.UsuarioDTO;
import static com.ipn.mx.web.bean.BaseBean.ACC_ACTUALIZAR;
import static com.ipn.mx.web.bean.BaseBean.ACC_CREAR;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author alfre
 */
@ManagedBean(name = "usuarioMB")
//@Named(value = "usuarioMB")
@SessionScoped
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioMB extends BaseBean implements Serializable {
    private UsuarioDAO dao = new UsuarioDAO();
    private UsuarioDTO dto;
    private List<UsuarioDTO> listaDeUsuarios;
     private String username;
    private String password;
    
    @PostConstruct
    public void init(){
        listaDeUsuarios = new ArrayList<>();
        listaDeUsuarios = dao.buscarTodos();
    }
    
    public String ini() throws IOException{
        
return "/inicio?faces-redirect=true";
    }
    
    public String prepareAdd(){
        dto = new UsuarioDTO();
        setAccion(ACC_CREAR);
        return "/usuario/usuarioForm?faces-redirect=true";
    }
    
    public String prepareUpdate(){
        setAccion(ACC_ACTUALIZAR);
        return "/usuario/usuarioForm?faces-redirect=true";
    }
    
    public String prepareIndex(){
        init();
        return "/usuario/listadoUsuario?faces-redirect=true";
    }
    
    public String back(){
        return prepareIndex();
    }
    
    public Boolean validate(){
        boolean valido = true;
        return valido;
    }
    
    public String add(){
        Boolean valido = validate();
        if(valido){
            dao.create(dto);
            if(valido){
                return prepareIndex();
            }else{
                return prepareAdd();
            }
        }
        return prepareAdd();
    }
    
    public String update(){
        Boolean valido = validate();
        if(valido){
            dao.update(dto);
            if(valido){
                return prepareIndex();
            }else{
                return prepareUpdate();
            }
        }
        return prepareUpdate();
    }
    
    public String delete(){
        dao.delete(dto);
        return prepareIndex();
    }
    
    public void seleccionarUsuario(ActionEvent event){
        String claveSel = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("claveSel");
        dto = new UsuarioDTO();
        dto.getEntidad().setIdUsuario(Long.parseLong(claveSel));
        try{
            dto = dao.load(dto);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public String nombreusu(){
     String user = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
     return user;
    }
    
     public void verificarsesion(){
        
        try {
            String user = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
            if (user == null) {
                
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }
     
     public void cerrarsesion(){
     FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
     }
     
     public String correo(){
         String email = null;
         try {
             email = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("correo");
             
         } catch (Exception e) {
             e.printStackTrace();
         }
         
         return email; 
     }
    
    
}
