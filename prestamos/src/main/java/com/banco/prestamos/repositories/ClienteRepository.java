package com.banco.prestamos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.banco.prestamos.entities.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {}
