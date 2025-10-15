package com.rtz.api_contas_a_pagar.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContaResponseDTO {

    private Long id;
    private String nome;
    private BigDecimal valorOriginal;
    private BigDecimal valorCorrigido;
    private Integer diasAtraso;
    private BigDecimal multa;
    private BigDecimal jurosDia;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;

}
