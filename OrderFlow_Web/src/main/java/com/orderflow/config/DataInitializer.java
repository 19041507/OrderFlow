package com.orderflow.config;

import com.orderflow.model.Cliente;
import com.orderflow.model.Contem;
import com.orderflow.model.Entrega;
import com.orderflow.model.Pagamento;
import com.orderflow.model.Pedido;
import com.orderflow.model.Produto;
import com.orderflow.repository.ClienteRepository;
import com.orderflow.repository.ProdutoRepository;
import com.orderflow.service.ClienteService;
import com.orderflow.service.ContemService;
import com.orderflow.service.EntregaService;
import com.orderflow.service.PagamentoService;
import com.orderflow.service.PedidoService;
import com.orderflow.service.ProdutoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(
            ClienteRepository clienteRepository,
            ProdutoRepository produtoRepository,
            ClienteService clienteService,
            ProdutoService produtoService,
            PedidoService pedidoService,
            ContemService contemService,
            PagamentoService pagamentoService,
            EntregaService entregaService
    ) {
        return args -> {
            if (clienteRepository.count() > 0 || produtoRepository.count() > 0) {
                return;
            }

            Cliente cliente = new Cliente();
            cliente.setNome("Cliente Exemplo");
            cliente.setEmail("cliente@orderflow.com");
            cliente.setTelefone("81999999999");
            cliente.setLogradouro("Rua Central");
            cliente.setNumero(100);
            cliente.setBairro("Centro");
            cliente.setCidade("Recife");
            cliente.setCep("50000000");
            cliente.setComplemento("Casa");
            cliente.setEstado("PE");
            Cliente clienteSalvo = clienteService.salvar(cliente);

            Produto hamburguer = new Produto();
            hamburguer.setNome("Combo da Casa");
            hamburguer.setPreco(29.90);
            hamburguer.setEstoque(30);
            Produto hamburguerSalvo = produtoService.salvar(hamburguer);

            Produto acai = new Produto();
            acai.setNome("Açaí Premium 500ml");
            acai.setPreco(18.50);
            acai.setEstoque(25);
            produtoService.salvar(acai);

            Produto pizza = new Produto();
            pizza.setNome("Pizza Média Artesanal");
            pizza.setPreco(42.00);
            pizza.setEstoque(15);
            produtoService.salvar(pizza);

            Pedido pedido = new Pedido();
            pedido.setIdCliente(clienteSalvo.getIdCliente());
            Pedido pedidoSalvo = pedidoService.criarPedido(pedido);

            Contem item = new Contem();
            item.setIdPedido(pedidoSalvo.getIdPedido());
            item.setIdProduto(hamburguerSalvo.getIdProduto());
            item.setQuantidade(2);
            item.setObs("Sem cebola");
            contemService.salvar(item);

            Pagamento pagamento = new Pagamento();
            pagamento.setIdPedido(pedidoSalvo.getIdPedido());
            pagamento.setMetodoPagamento("PIX");
            pagamentoService.registrarPagamento(pagamento);

            Entrega entrega = new Entrega();
            entrega.setIdPedido(pedidoSalvo.getIdPedido());
            entrega.setCodigoRastreio("OF-0001");
            entrega.setTransporte("Equipe de entrega");
            entregaService.registrarEnvio(entrega);
        };
    }
}
