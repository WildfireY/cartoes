package com.cartoes.api.repositories;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.cartoes.api.entities.Cartao;
import com.cartoes.api.entities.Cliente;
import com.cartoes.api.entities.Transacao;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TransacaoRepositoryTest {
	@Autowired
	private CartaoRepository cartaoRepository;
	@Autowired
	private TransacaoRepository transacaoRepository;
	@Autowired
	private ClienteRepository clienteRepository;

	private Cliente clienteTeste;
	private Cartao cartaoTeste;
	private Transacao transacaoTeste;	

	private void CriarTransacaoTestes() throws ParseException {

		clienteTeste = new Cliente();

		clienteTeste.setNome("Nome Teste");
		clienteTeste.setCpf("05887098082");
		clienteTeste.setUf("CE");
		clienteTeste.setDataAtualizacao(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));

		cartaoTeste = new Cartao();
		
		cartaoTeste.setBloqueado(false);
		cartaoTeste.setCliente(clienteTeste);
		cartaoTeste.setDataAtualizacao(new SimpleDateFormat("dd/MM/yyyy").parse("01/02/2020"));
		cartaoTeste.setDataValidade(new SimpleDateFormat("dd/MM/yyyy").parse("01/12/2020"));
		cartaoTeste.setNumero("1230981203");
		
		transacaoTeste = new Transacao();
		
		transacaoTeste.setCartao(cartaoTeste);
		transacaoTeste.setCnpj("18808626000194");
		transacaoTeste.setDataTransacao(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"));
		transacaoTeste.setJuros(0.05);
		transacaoTeste.setQdtParcelas(3);
		transacaoTeste.setValor(1500.00);
		
	}
	@Before
	public void setUp() throws Exception {
		
		CriarTransacaoTestes();
		clienteRepository.save(clienteTeste);
		cartaoRepository.save(cartaoTeste);
		transacaoRepository.save(transacaoTeste);
		
	}
	@After
	public void tearDown() throws Exception {
		
		clienteRepository.deleteAll();
		cartaoRepository.deleteAll();
		transacaoRepository.deleteAll();
		
	}
	@Test
    public void testFindByNumeroCartao() {
        Optional<List<Transacao>> transacoes = transacaoRepository.findByNumeroCartao(transacaoTeste.getCartao().getNumero());
        List<Transacao> transacao = transacoes.get();

        assertEquals(transacaoTeste.getCartao().getNumero(), transacao.get(0).getCartao().getNumero());
    }
}