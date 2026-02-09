package com.banco.sistemabancario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.sistemabancario.entity.failureAuthentication.LoginFailure;

public interface LoginFailureRepository extends JpaRepository<LoginFailure, Integer>{
    
}
