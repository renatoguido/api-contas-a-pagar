package com.rtz.api_contas_a_pagar.controller;

import com.rtz.api_contas_a_pagar.dto.ContaRequestDTO;
import com.rtz.api_contas_a_pagar.dto.ContaResponseDTO;
import com.rtz.api_contas_a_pagar.service.ContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContaControllerTest {

    @Mock
    private ContaService contaService;

    @InjectMocks
    private ContaController contaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSalvarConta() {
        ContaRequestDTO request = ContaRequestDTO.builder()
                .nome("Conta Luz")
                .valorOriginal(new BigDecimal("100"))
                .dataVencimento(LocalDate.now())
                .dataPagamento(LocalDate.now())
                .build();

        ContaResponseDTO responseDTO = ContaResponseDTO.builder()
                .id(1L)
                .nome("Conta Luz")
                .valorOriginal(new BigDecimal("100"))
                .valorCorrigido(new BigDecimal("100"))
                .diasAtraso(0)
                .multa(BigDecimal.ZERO)
                .jurosDia(BigDecimal.ZERO)
                .dataVencimento(LocalDate.now())
                .dataPagamento(LocalDate.now())
                .build();

        when(contaService.salvar(any(ContaRequestDTO.class))).thenReturn(responseDTO);

        ResponseEntity<ContaResponseDTO> response = contaController.salvar(request);

        assertNotNull(response);
        assertEquals(1L, response.getBody().getId());
        assertEquals("Conta Luz", response.getBody().getNome());

        verify(contaService, times(1)).salvar(any(ContaRequestDTO.class));
    }

    @Test
    void testListarContas() {
        ContaResponseDTO conta1 = ContaResponseDTO.builder().id(1L).nome("Conta1").valorOriginal(new BigDecimal("100")).valorCorrigido(new BigDecimal("100")).diasAtraso(0).dataVencimento(LocalDate.now()).dataPagamento(LocalDate.now()).build();
        ContaResponseDTO conta2 = ContaResponseDTO.builder().id(2L).nome("Conta2").valorOriginal(new BigDecimal("200")).valorCorrigido(new BigDecimal("206")).diasAtraso(3).dataVencimento(LocalDate.now()).dataPagamento(LocalDate.now()).build();

        when(contaService.listar()).thenReturn(Arrays.asList(conta1, conta2));

        ResponseEntity<List<ContaResponseDTO>> response = contaController.listar();

        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        assertEquals("Conta1", response.getBody().get(0).getNome());

        verify(contaService, times(1)).listar();
    }
}