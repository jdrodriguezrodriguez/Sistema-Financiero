package com.banco.sistemabancario.repository.auditoriaRepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.sistemabancario.entity.AuditoriaEntity;

public interface AuditLogRepository extends JpaRepository<AuditoriaEntity, Integer>{
    
}
