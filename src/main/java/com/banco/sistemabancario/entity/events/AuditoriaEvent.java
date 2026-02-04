package com.banco.sistemabancario.entity.events;

import java.util.Map;

import com.banco.sistemabancario.entity.enums.AuditoriaActionEnums;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditoriaEvent {

    private AuditoriaActionEnums accion;
    private Integer performedBy;
    private Integer targetId;
    private Map<String, Object> cambios;


    public AuditoriaEvent(AuditoriaActionEnums accion,
                        Integer performedBy, Integer targetId,
                        Map<String, Object> cambios) {
        this.performedBy = performedBy;
        this.accion = accion;
        this.targetId = targetId;
        this.cambios = cambios;
    }
}
