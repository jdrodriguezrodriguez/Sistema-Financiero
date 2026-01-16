package com.banco.sistemabancario.entity.Events;

import java.util.Map;

import com.banco.sistemabancario.entity.enums.AuditoriaActionEnums;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditoriaEvent {

    private AuditoriaActionEnums accion;
    private Integer targetId;
    private Map<String, Object> cambios;


    public AuditoriaEvent(AuditoriaActionEnums accion,
                          Integer targetId,
                          Map<String, Object> cambios) {
        this.accion = accion;
        this.targetId = targetId;
        this.cambios = cambios;
    }
}
