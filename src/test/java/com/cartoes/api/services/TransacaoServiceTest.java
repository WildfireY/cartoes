package com.cartoes.api.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.cartoes.api.entities.Cartao;
import com.cartoes.api.entities.Transacao;
import com.cartoes.api.repositories.CartaoRepository;
import com.cartoes.api.repositories.TransacaoRepository;
import com.cartoes.api.utils.ConsistenciaException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TransacaoServiceTest {
	@MockBean
	private TransacaoRepository transacaoRepository;
	@MockBean
	private CartaoRepository cartaoRepository;
	@Autowired
	private TransacaoService transacaoService;

	@Test
	public void testBuscarPorNumeroCartaoExistente() throws ConsistenciaException{

		List<Transacao> lstTransacao = new ArrayList<Transacao>();
		lstTransacao.add(new Transacao());

		BDDMockito.given(transacaoRepository.findByNumeroCartao(Mockito.anyString()))
				.willReturn(Optional.of(lstTransacao));

		Optional<List<Transacao>> resultado = transacaoService.buscarPorNumeroCartao("1230981203");

		assertTrue(resultado.isPresent());
	}

	@Test(expected = ConsistenciaException.class)
	public void testBuscarPorNumeroCartaoNaoExistente() throws ConsistenciaException {

		BDDMockito.given(transacaoRepository.findByNumeroCartao(Mockito.anyString())).willReturn(Optional.empty());

		transacaoService.buscarPorNumeroCartao("12315135131");

	}

	@Test
	public void testSalvarComSucesso() throws ConsistenciaException, ParseException{
		

		Cartao cartao = new Cartao();
		cartao.setNumero("1321564654");
		cartao.setDataValidade(new SimpleDateFormat("dd/MM/yyyy").parse("01/12/2020"));
		Transacao transacao = new Transacao();
		transacao.setCartao(cartao);
		
		BDDMockito.given(cartaoRepository.findByNumero(Mockito.anyString()))
		.willReturn(Optional.of(cartao));
		BDDMockito.given(transacaoRepository.save(Mockito.any(Transacao.class)))
		.willReturn(new Transacao());
		
		Transacao resultado = transacaoService.salvar(transacao);
		
		assertNotNull(resultado);
	}
	@Test(expected = ConsistenciaException.class)
    public void testSalvarSemSucesso() throws ConsistenciaException {

        BDDMockito.given(transacaoRepository.findByNumeroCartao(Mockito.anyString()))
        .willReturn(Optional.empty());

        Cartao cartao = new Cartao();
        cartao.setNumero("342442345455");
        Transacao transacao = new Transacao();

        transacao.setCartao(cartao);

        transacaoService.salvar(transacao);

    }
}
