package com.banco.sistemabancario.repository.AuditoriaRepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.sistemabancario.entity.Events.AuditoriaEntity;

public interface AuditLogRepository extends JpaRepository<AuditoriaEntity, Integer>{
    
}
