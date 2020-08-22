package com.cartoes.api.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.cartoes.api.entities.Transacao;
import com.cartoes.api.repositories.TransacaoRepository;
import com.cartoes.api.utils.ConsistenciaException;


@Service
public class TransacaoService {
    private static final Logger log = LoggerFactory.getLogger(TransacaoService.class);

    @Autowired
    private TransacaoRepository transacaoRepository;
    public Optional<List<Transacao>> listarTransacoes(int cartaoId) throws ConsistenciaException {
        log.info("Service: buscando as transações do cartão: {}", cartaoId);
        Optional<List<Transacao>> transacao = Optional.ofNullable(transacaoRepository.findByCartaoId(cartaoId));
        if (!transacao.isPresent()) {
            log.info("Service: Nenhuma transação com id: {} foi encontrado", cartaoId);
            throw new ConsistenciaException("Nenhuma transacao com o idCartao: {} foi encontrado", cartaoId);
        }
        return transacao;
    }

    public Transacao salvar(Transacao transacao) throws ConsistenciaException {
        log.info("Service: salvando o cartao: {}", transacao);
        try {
            return transacaoRepository.save(transacao);
        } catch (DataIntegrityViolationException e) {
            log.info("Service: Já existe um cartão de número {} cadastrado CORRGIR EXCESSAO");
            throw new ConsistenciaException("Já existe um cartão de número {} cadastrado CORRIGIR EXCESSAO");

        }
    }

}
