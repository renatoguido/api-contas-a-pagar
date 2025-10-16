package com.rtz.api_contas_a_pagar.service;

import com.rtz.api_contas_a_pagar.dto.ContaRequestDTO;
import com.rtz.api_contas_a_pagar.dto.ContaResponseDTO;
import com.rtz.api_contas_a_pagar.model.Conta;
import com.rtz.api_contas_a_pagar.repository.ContaRepository;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContaServiceTest {

    @Mock
    private ContaRepository repository;

    @InjectMocks
    private ContaService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSalvarContaSemAtraso() {
        ContaRequestDTO request = new ContaRequestDTO(
                "Energia",
                BigDecimal.valueOf(100.00),
                LocalDate.of(2025, 10, 10),
                LocalDate.of(2025, 10, 10)
        );

        Conta contaSalva = new Conta();
        contaSalva.setId(1L);
        contaSalva.setNome("Energia");
        contaSalva.setValorOriginal(BigDecimal.valueOf(100.00));
        contaSalva.setValorCorrigido(BigDecimal.valueOf(100.00));
        contaSalva.setDiasAtraso(0);
        contaSalva.setMulta(BigDecimal.ZERO);
        contaSalva.setJurosDia(BigDecimal.ZERO);
        contaSalva.setDataVencimento(LocalDate.of(2025, 10, 10));
        contaSalva.setDataPagamento(LocalDate.of(2025, 10, 10));

        when(repository.save(any(Conta.class))).thenReturn(contaSalva);

        ContaResponseDTO response = service.salvar(request);

        assertEquals("Energia", response.getNome());
        assertEquals(BigDecimal.valueOf(100.00), response.getValorCorrigido());
        assertEquals(0, response.getDiasAtraso());
    }

    @Test
    void testSalvarContaComAtraso2Dias() {
        ContaRequestDTO request = new ContaRequestDTO(
                "Água",
                BigDecimal.valueOf(100.00),
                LocalDate.of(2025, 10, 10),
                LocalDate.of(2025, 10, 12)
        );

        when(repository.save(any(Conta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ContaResponseDTO response = service.salvar(request);

        double esperado = 100 + (100 * 0.02) + (100 * 0.001 * 2);
        assertEquals(BigDecimal.valueOf(esperado).setScale(2, BigDecimal.ROUND_HALF_UP),
                response.getValorCorrigido().setScale(2, BigDecimal.ROUND_HALF_UP));
        assertEquals(2, response.getDiasAtraso());
    }

    @Test
    void testSalvarContaComAtraso6Dias() {
        ContaRequestDTO request = new ContaRequestDTO(
                "Internet",
                BigDecimal.valueOf(200.00),
                LocalDate.of(2025, 10, 1),
                LocalDate.of(2025, 10, 7)
        );

        when(repository.save(any(Conta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ContaResponseDTO response = service.salvar(request);

        double esperado = 200 + (200 * 0.05) + (200 * 0.003 * 6);
        assertEquals(BigDecimal.valueOf(esperado).setScale(2, BigDecimal.ROUND_HALF_UP),
                response.getValorCorrigido().setScale(2, BigDecimal.ROUND_HALF_UP));
        assertEquals(6, response.getDiasAtraso());
    }

    @Test
    void testListarContas() {
        Conta conta = new Conta();
        conta.setId(1L);
        conta.setNome("Aluguel");
        conta.setValorOriginal(BigDecimal.valueOf(500.00));
        conta.setValorCorrigido(BigDecimal.valueOf(500.00));
        conta.setDiasAtraso(0);
        conta.setMulta(BigDecimal.ZERO);
        conta.setJurosDia(BigDecimal.ZERO);
        conta.setDataVencimento(LocalDate.of(2025, 10, 10));
        conta.setDataPagamento(LocalDate.of(2025, 10, 10));

        when(repository.findAll()).thenReturn(Collections.singletonList(conta));

        List<ContaResponseDTO> lista = service.listar();

        assertEquals(1, lista.size());
        assertEquals("Aluguel", lista.get(0).getNome());
    }
}