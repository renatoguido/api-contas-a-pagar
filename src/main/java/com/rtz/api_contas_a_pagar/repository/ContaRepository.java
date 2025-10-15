package com.rtz.api_contas_a_pagar.repository;

import com.rtz.api_contas_a_pagar.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaRepository extends JpaRepository<Conta, Long> {
}
