package com.banco.prestamos.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "prestamo")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal monto;
    private BigDecimal interes;

    @Column(nullable = false)
    private Integer duracionMeses;

    private String estado;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    public Prestamo() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public BigDecimal getInteres() { return interes; }
    public void setInteres(BigDecimal interes) { this.interes = interes; }

    public Integer getDuracionMeses() { return duracionMeses; }
    public void setDuracionMeses(Integer duracionMeses) { this.duracionMeses = duracionMeses; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }

    public String calcularCuota(BigDecimal monto, BigDecimal tasaAnual, int meses) {
        if (meses <= 0 || monto.compareTo(BigDecimal.ZERO) <= 0 || tasaAnual.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Los valores deben ser mayores a 0.");
        }

        BigDecimal tasaMensual = tasaAnual.divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_EVEN);
        BigDecimal unoMasTasa = BigDecimal.ONE.add(tasaMensual);
        BigDecimal potencia = unoMasTasa.pow(-meses, new MathContext(10, RoundingMode.HALF_EVEN));
        BigDecimal divisor = BigDecimal.ONE.subtract(potencia);

        return "El valor de la cuota será de " + monto.multiply(tasaMensual).divide(divisor, 2, RoundingMode.HALF_EVEN) + " para la solicitud de un prestamo de " + monto + " con una tasa de interes anual del " + tasaAnual + "% a " + meses + " meses.";
    }

    @Override
    public String toString() {
        return "Prestamo { " +
                "id=" + id +
                ", monto=" + monto +
                ", interes=" + interes +
                ", duracionMeses=" + duracionMeses +
                ", estado='" + estado + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaActualizacion=" + fechaActualizacion +
                " }";
    }
}
