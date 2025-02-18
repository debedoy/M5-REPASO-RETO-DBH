package com.banco.prestamos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.banco.prestamos.entities.Prestamo;
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {}