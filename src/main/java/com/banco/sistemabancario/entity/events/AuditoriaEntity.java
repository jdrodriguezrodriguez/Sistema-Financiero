package com.banco.sistemabancario.entity.events;

import java.time.LocalDateTime;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.banco.sistemabancario.entity.enums.AuditoriaActionEnums;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Setter;
import lombok.Getter;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
public class AuditoriaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_audit")
    private Integer idAudit;

    @Column(name = "action")
    @Enumerated(EnumType.STRING)
    private AuditoriaActionEnums accion;

    @Column(name = "performed_by")
    private Integer performedBy;

    @Column(name = "target_id")
    private Integer targetId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "changes")
    private Map<String, Object> cambios;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    public AuditoriaEntity(){}
    public AuditoriaEntity(AuditoriaActionEnums accion, Integer targetId, Map<String, Object> cambios, LocalDateTime createdAt){
        this.accion = accion;
        this.targetId = targetId;
        this.cambios = cambios;
        this.createdAt = createdAt;
    }
}
