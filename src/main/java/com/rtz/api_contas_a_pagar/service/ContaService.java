package com.rtz.api_contas_a_pagar.service;

import com.rtz.api_contas_a_pagar.model.Conta;
import com.rtz.api_contas_a_pagar.dto.ContaRequestDTO;
import com.rtz.api_contas_a_pagar.dto.ContaResponseDTO;
import com.rtz.api_contas_a_pagar.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContaService {

    @Autowired
    private final ContaRepository repository;

    public ContaService(ContaRepository repository) {
        this.repository = repository;
    }

    public ContaResponseDTO salvar(ContaRequestDTO request) {    Conta conta;
        conta = new Conta();
        conta.setNome(request.getNome());
        conta.setValorOriginal(request.getValorOriginal());
        conta.setDataVencimento(request.getDataVencimento());
        conta.setDataPagamento(request.getDataPagamento());

        int diasAtraso = 0;
        LocalDate vencimento = conta.getDataVencimento();
        LocalDate pagamento = conta.getDataPagamento();

        if (pagamento.compareTo(vencimento) > 0) {
            LocalDate dataTemp = vencimento;
            while (dataTemp.compareTo(pagamento) < 0) {
                diasAtraso++;
                dataTemp = dataTemp.plusDays(1);
            }
        }

        conta.setDiasAtraso(diasAtraso);


        double valorOriginal = conta.getValorOriginal().doubleValue();
        double multa = 0;
        double jurosDia = 0;
        double valorCorrigido = valorOriginal;

        if (diasAtraso > 0) {
            if (diasAtraso <= 3) {
                multa = 0.02;
                jurosDia = 0.001;
            } else if (diasAtraso <= 5) {
                multa = 0.03;
                jurosDia = 0.002;
            } else {
                multa = 0.05;
                jurosDia = 0.003;
            }

            double valorMulta = valorOriginal * multa;
            double valorJuros = valorOriginal * jurosDia * diasAtraso;
            valorCorrigido = valorOriginal + valorMulta + valorJuros;
        }

        conta.setMulta(BigDecimal.valueOf(multa * 100));
        conta.setJurosDia(BigDecimal.valueOf(jurosDia * 100));
        conta.setValorCorrigido(BigDecimal.valueOf(valorCorrigido));

        Conta salva = repository.save(conta);

        return new ContaResponseDTO(
                salva.getId(),
                salva.getNome(),
                salva.getValorOriginal(),
                salva.getValorCorrigido(),
                salva.getDiasAtraso(),
                salva.getMulta(),
                salva.getJurosDia(),
                salva.getDataVencimento(),
                salva.getDataPagamento()
        );
    }


    public List<ContaResponseDTO> listar() {
        return repository.findAll().stream()
                .map(salva -> new ContaResponseDTO(
                        salva.getId(),
                        salva.getNome(),
                        salva.getValorOriginal(),
                        salva.getValorCorrigido(),
                        salva.getDiasAtraso(),
                        salva.getMulta(),
                        salva.getJurosDia(),
                        salva.getDataVencimento(),
                        salva.getDataPagamento()
                ))
                .collect(Collectors.toList());
    }
}
