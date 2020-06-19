/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mx.web.bean;

import com.ipn.mx.modelo.dao.MovimientosDAO;
import com.ipn.mx.modelo.dto.ArticuloDTO;
import com.ipn.mx.modelo.dto.MovimientosDTO;
import static com.ipn.mx.web.bean.BaseBean.ACC_ACTUALIZAR;
import static com.ipn.mx.web.bean.BaseBean.ACC_CREAR;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
//import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author alfre
 */
@ManagedBean(name = "movimientosMB")
//@Named(value = "movimientosMB")
@SessionScoped
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovimientosMB extends BaseBean implements Serializable {
    private MovimientosDAO dao = new MovimientosDAO();
    private MovimientosDTO dto;
    private List<MovimientosDTO> listaDeMovimientos;
    private List<ArticuloDTO> listaproductos;
    
    @PostConstruct
    public void init(){
        listaDeMovimientos = new ArrayList<>();
        listaDeMovimientos = dao.buscarTodos();
        listaproductos = dao.getproductos();
    }
    
    public String prepareAdd(){
        dto = new MovimientosDTO();
        setAccion(ACC_CREAR);
        return "/movimientos/movimientosForm?faces-redirect=true";
    }
    
    public String prepareUpdate(){
        setAccion(ACC_ACTUALIZAR);
        return "/movimientos/movimientosForm?faces-redirect=true";
    }
    
    public String prepareIndex(){
        init();
        return "/movimientos/listadoMovimientos?faces-redirect=true";
    }
    
    public String back(){
        return prepareIndex();
    }
    
    public Boolean validate(){
        boolean valido = true;
        return valido;
    }
    
    public String add() throws SQLException{
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
    
    public String update() throws SQLException{
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
    
    public String delete() throws SQLException{
        dao.delete(dto);
        return prepareIndex();
    }
    
    public void seleccionarMovimiento(ActionEvent event){
        String claveSel = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("claveSel");
        dto = new MovimientosDTO();
        dto.getEntidad().setIdMovimiento(Long.parseLong(claveSel));
        try{
            dto = dao.load(dto);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    
}
