/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mx.web.bean;

import com.ipn.mx.modelo.dao.ArticuloDAO;
import com.ipn.mx.modelo.dto.ArticuloDTO;
import static com.ipn.mx.web.bean.BaseBean.ACC_ACTUALIZAR;
import static com.ipn.mx.web.bean.BaseBean.ACC_CREAR;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author alfre
 */
@Named(value = "articuloMB")
@SessionScoped
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticuloMB extends BaseBean implements Serializable {
    private ArticuloDAO dao = new ArticuloDAO();
    private ArticuloDTO dto;
    private UploadedFile file;
    private UploadedFile file2;
    private List<ArticuloDTO> listaDeArticulos;
    
    @PostConstruct
    public void init(){
        listaDeArticulos = new ArrayList<>();
        listaDeArticulos = dao.buscarTodos();
    }
    
    public String prepareAdd(){
        dto = new ArticuloDTO();
        setAccion(ACC_CREAR);
        return "/articulo/articuloForm?faces-redirect=true";
    }
    
    public String prepareUpdate(){
        setAccion(ACC_ACTUALIZAR);
        return "/articulo/articuloForm?faces-redirect=true";
    }
    
    public String prepareIndex(){
        init();
        return "/articulo/listadoArticulo?faces-redirect=true";
    }
    
    public String back(){
        return prepareIndex();
    }
    
    public Boolean validate(){
        boolean valido = true;
        return valido;
    }
    
    public String add() throws IOException{
        Boolean valido = validate();
        if(valido){
            InputStream input = file.getInputStream();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int i = 0; (i=input.read(buffer)) > 0;) {
                
                output.write(buffer, 0, i);
                
            }
            dto.getEntidad().setImagen(output.toByteArray());
            dao.create(dto);
            if(valido){
                file = null;
                return prepareIndex();
            }else{
                return prepareAdd();
            }
        }
        return prepareAdd();
    }
    
    public String update() throws IOException{
        Boolean valido = validate();
        if(valido){
             InputStream input = file.getInputStream();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int i = 0; (i=input.read(buffer)) > 0;) {
                
                output.write(buffer, 0, i);
                
            }
            dto.getEntidad().setImagen(output.toByteArray());
            dao.update(dto);
            if(valido){
                file = null;
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
    
    public void seleccionarArticulo(ActionEvent event){
        String claveSel = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("claveSel");
        dto = new ArticuloDTO();
        dto.getEntidad().setClaveArticulo(Long.parseLong(claveSel));
        try{
            dto = dao.load(dto);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public StreamedContent getImage() throws IOException {
    FacesContext context = FacesContext.getCurrentInstance();
    if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE)
        return new DefaultStreamedContent();
    else {
        String id = context.getExternalContext().getRequestParameterMap().get("id");
        
            return new DefaultStreamedContent(new ByteArrayInputStream(dto.getEntidad().getImagen()), "image/jpg");
        
            
    }
}
 
}
