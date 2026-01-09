package com.lojinhasystem.system.services;

import com.lojinhasystem.system.entities.Cliente;
import com.lojinhasystem.system.entities.Venda;
import com.lojinhasystem.system.entities.enums.StatusVenda;
import com.lojinhasystem.system.repositories.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DividaService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VendaService vendaService;

    @Transactional
    public Cliente registrarDivida(Venda obj) {

        // Recarrega a venda do banco
        Venda venda = vendaService.findById(obj.getId());

        // Força carregamento dos itens (Lazy Loading)
        venda.getItens().size();

        Cliente cliente = venda.getCliente();

        // Se a venda estiver aguardando pagamento
        if (venda.getStatus() == StatusVenda.AGUARDANDO_PAGAMENTO) {

            // Calcula o total da venda usando o service da venda
            Double total = vendaService.calcularTotalVenda(venda);

            // Garante que a dívida não é nula
            if (cliente.getDivida() == null) {
                cliente.setDivida(0.0);
            }

            // Atualiza a dívida
            cliente.setDivida(cliente.getDivida() + total);

            // Salva cliente
            cliente = clienteRepository.save(cliente);
        }
        return cliente;
    }
}
