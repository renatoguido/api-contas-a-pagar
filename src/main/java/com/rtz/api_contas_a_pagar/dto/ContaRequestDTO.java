package com.rtz.api_contas_a_pagar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContaRequestDTO {

    @NotBlank
    private String nome;

    @NotNull
    private BigDecimal valorOriginal;

    @NotNull
    private LocalDate dataVencimento;

    @NotNull
    private LocalDate dataPagamento;
}
