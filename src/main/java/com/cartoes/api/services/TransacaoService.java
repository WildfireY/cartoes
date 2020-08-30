package com.cartoes.api.services;

import java.util.List;
import java.util.Optional;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cartoes.api.entities.Cartao;
import com.cartoes.api.entities.Transacao;
import com.cartoes.api.repositories.CartaoRepository;
import com.cartoes.api.repositories.TransacaoRepository;
import com.cartoes.api.utils.ConsistenciaException;


@Service
public class TransacaoService {
	private static final Logger log = LoggerFactory.getLogger(TransacaoService.class);

	@Autowired
	private CartaoRepository cartaoRepository;
	@Autowired
	private TransacaoRepository transacaoRepository;
	
	
	public Optional<List<Transacao>> buscarPorNumeroCartao(String numeroCartao) throws ConsistenciaException{
		
		log.info("Service buscando as transações do cartão de numero: {}", numeroCartao);
		
		Optional<List<Transacao>> transacoes = transacaoRepository.findByNumeroCartao(numeroCartao);
		
		if(!transacoes.isPresent() || transacoes.get().size() < 1) {  
			
			log.info("Nenhuma transacao encontrada para o cartao de número: {}", numeroCartao);
			throw new ConsistenciaException("Nenhuma transacao encontrada para o cartao de número: {}", numeroCartao); 
		}
		
		return transacoes;
	}

	 Date dataAtual = new Date();
	
	public Transacao salvar(Transacao transacao) throws ConsistenciaException {
		log.info("Service: salvando o cartao: {}", transacao);
		
		Optional<Cartao> cartao = cartaoRepository.findByNumero(transacao.getCartao().getNumero());
		
		if (!cartao.isPresent()) {
			log.info("Service: Nenhum cartao com id: {} foi encontrado", transacao.getCartao().getId());
			throw new ConsistenciaException("Nenhum cartao de numero: {} foi encontrado", transacao.getCartao().getId());
		}
		
		if (transacao.getId() > 0) {
			log.info("Transações não podem ser alteradas, apenas incluídas");
			throw new ConsistenciaException("Transações não podem ser alteradas, apenas incluídas");
		}
		
		if (cartao.get().getBloqueado() == true) {
			log.info("Não é possível incluir transações para este cartão, pois o cartao de id {} encontra-se bloqueado", transacao.getCartao().getId());
			throw new ConsistenciaException(
					"Não é possível incluir transações para este cartão, pois cartao de id {} encontra-se bloqueado", transacao.getCartao().getId());
		}
		
		if (cartao.get().getDataValidade().before(dataAtual)) {
			log.info("Não é possível incluir transações para este cartão, pois o cartao de id {} encontra-se vencido.", transacao.getCartao().getId());
			throw new ConsistenciaException("Não é possível incluir transações para este cartão, pois o cartao de id {} encontra-se vencido", transacao.getCartao().getId());
		}
		
		transacao.setCartao(cartao.get());
		
		return transacaoRepository.save(transacao);
		
	}
	
}