package com.banco.sistemabancario.entity.failureAuthentication;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Table(name = "user_security")
@Entity
@Getter
@Setter
@AllArgsConstructor
public class SeguridadUsuario {

    @Id
    @Column(name = "id_usuario")
    private Integer idUsuarioLog;

    private int failed_attempts;

    @CreationTimestamp
    private LocalDateTime last_failed_at;

    private LocalDateTime locked_until;
    private int lock_count; // 1 = 5 & 2 = 10
    private int permanently_locked;

    @Version
    private Long version;

    public SeguridadUsuario() {
    }

    public SeguridadUsuario(Integer idUsuarioLog, int failed_attempts, LocalDateTime last_failed_at,
            LocalDateTime locked_until, int lock_count, int permanently_locked) {
        this.idUsuarioLog = idUsuarioLog;
        this.failed_attempts = failed_attempts;
        this.last_failed_at = last_failed_at;
        this.locked_until = locked_until;
        this.lock_count = lock_count;
        this.permanently_locked = permanently_locked;
    }
}
