package com.rtz.api_contas_a_pagar.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "conta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotNull
    @Column(name = "valor_original")
    private BigDecimal valorOriginal;

    @Column(name = "valor_corrigido")
    private BigDecimal valorCorrigido;

    @Column(name = "dias_atraso")
    private Integer diasAtraso;

    private BigDecimal multa;
    @Column(name = "juros_dia")
    private BigDecimal jurosDia;

    @NotNull
    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    @NotNull
    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;
}
