package com.rtz.api_contas_a_pagar.controller;

import com.rtz.api_contas_a_pagar.dto.ContaRequestDTO;
import com.rtz.api_contas_a_pagar.dto.ContaResponseDTO;
import com.rtz.api_contas_a_pagar.service.ContaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contas")
public class ContaController {

    private final ContaService contaService;

    @Autowired
    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    public ResponseEntity<ContaResponseDTO> salvar(@Valid @RequestBody ContaRequestDTO request) {
        ContaResponseDTO response = contaService.salvar(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ContaResponseDTO>> listar() {
        List<ContaResponseDTO> contas = contaService.listar();
        return ResponseEntity.ok(contas);
    }
}