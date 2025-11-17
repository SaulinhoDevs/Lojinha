package com.lojinhasystem.system.services;

import com.lojinhasystem.system.entities.Cliente;
import com.lojinhasystem.system.entities.Venda;
import com.lojinhasystem.system.entities.enums.StatusVenda;
import com.lojinhasystem.system.repositories.ClienteRepository;
import com.lojinhasystem.system.repositories.VendaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VendaService vendaService;

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Cliente findById(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.get();
    }

    public Cliente update(Long id, Cliente obj) {
        Cliente entity = clienteRepository.getReferenceById(id);
        updateData(entity, obj);
        return clienteRepository.save(entity);
    }

    private void updateData(Cliente entity, Cliente obj) {
        entity.setNome(obj.getNome());
        entity.setTelefone(obj.getTelefone());
        entity.setRua(obj.getRua());
        entity.setBairro(obj.getBairro());
        entity.setNumero(obj.getNumero());
    }

    public Cliente insert(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public void delete(Long id) {
        clienteRepository.deleteById(id);
    }

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