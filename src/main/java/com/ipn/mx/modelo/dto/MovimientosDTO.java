/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mx.modelo.dto;

import com.ipn.mx.modelo.entidades.Movimientos;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author alfre
 */
@Data
@AllArgsConstructor
@Builder
public class MovimientosDTO implements Serializable{
    private Movimientos entidad;

    public MovimientosDTO() {
        entidad = new Movimientos();
    }    
}
