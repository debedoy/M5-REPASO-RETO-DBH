package com.banco.prestamos.services;

import com.banco.prestamos.entities.Cliente;
import com.banco.prestamos.entities.Prestamo;
import com.banco.prestamos.entities.dto.SolicitarPrestamoDTO;
import com.banco.prestamos.repositories.ClienteRepository;
import com.banco.prestamos.repositories.PrestamoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrestamoService {

    private PrestamoRepository prestamoRepository;

    private ClienteRepository clienteRepository;

    public PrestamoService(PrestamoRepository prestamoRepository, ClienteRepository clienteRepository) {
        this.prestamoRepository = prestamoRepository;
        this.clienteRepository = clienteRepository;
    }

    public String consultarEstadoPrestamo(Long idPrestamo) {
        Optional<Prestamo> prestamo = prestamoRepository.findById(idPrestamo);
        if (prestamo.isPresent()) {
            return "El estado del prestamo es: " + prestamo.get().getEstado();
        }
        throw new NullPointerException("El prestamo no existe");
    }

    public String consultarHistorialPrestamos(Long idCliente){
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        if (cliente.isPresent()) {
            return "El historial de los últimos tres prestamos del cliente " + cliente.get().getNombre() + " son: " + cliente.get().getPrestamos();
        }
        throw new NullPointerException("El cliente no existe");
    }

    public String solicitarPrestamo(SolicitarPrestamoDTO prestamoDTO) {
        return clienteRepository.findById(prestamoDTO.getClienteId())
                .map(cliente -> {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setCliente(cliente);
                    prestamo.setMonto(prestamoDTO.getMonto());
                    prestamo.setEstado("Pendiente");
                    prestamo.setInteres(prestamoDTO.getInteres());
                    prestamo.setDuracionMeses(prestamoDTO.getDuracionMeses());
                    prestamoRepository.save(prestamo);

                    return "El prestamo ha sido solicitado con exito";
                })
                .orElseThrow(() -> new NullPointerException("El cliente no existe"));
    }

    public String aprobarPrestamo(Long idPrestamo) {
        return prestamoRepository.findById(idPrestamo)
                .map(prestamo -> {
                    if ("Pendiente".equals(prestamo.getEstado())) {
                        prestamo.setEstado("Aprobado");
                        prestamoRepository.save(prestamo);

                        return "Prestamo aprobado";
                    } else {
                        return "El prestamo no se puede aprobar, ya cuenta con el estado " + prestamo.getEstado();
                    }
                })
                .orElseThrow(() -> new NullPointerException("El prestamo no existe"));
    }

    public String rechazarPrestamo(Long idPrestamo) {
        return prestamoRepository.findById(idPrestamo)
                .map(prestamo -> {
                    if ("Pendiente".equals(prestamo.getEstado())) {
                        prestamo.setEstado("Rechazado");
                        prestamoRepository.save(prestamo);

                        return "Prestamo Rechazado";
                    } else {
                        return "El prestamo no se puede rechazar, ya cuenta con el estado " + prestamo.getEstado();
                    }
                })
                .orElseThrow(() -> new NullPointerException("El prestamo no existe"));
    }

    public String simularCuotas(SolicitarPrestamoDTO prestamoDTO) {
        Prestamo prestamo = new Prestamo();
        return prestamo.calcularCuota(prestamoDTO.getMonto(), prestamoDTO.getInteres(), prestamoDTO.getDuracionMeses());
    }

}
