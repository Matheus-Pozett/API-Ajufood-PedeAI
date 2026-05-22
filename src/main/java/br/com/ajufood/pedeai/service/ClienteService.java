package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.exception.ConstraintException;
import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.model.ClienteModel;
import br.com.ajufood.pedeai.repository.ClienteRepository;
import br.com.ajufood.pedeai.rest.dto.request.ClienteRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.ClienteResponseDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public ClienteResponseDTO findById(int id) {
        ClienteModel cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                    "Cliente com ID " + id + " não encontrado"
        ));

        return modelMapper.map(cliente, ClienteResponseDTO.class);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> findAll() {
        return clienteRepository.findAll()
                .stream()
                .map(cliente -> modelMapper.map(cliente, ClienteResponseDTO.class))
                .toList();
    }

    @Transactional
    public ClienteResponseDTO save(ClienteRequestDTO clienteNovoDto) {
        try {
            ClienteModel clienteNovo = modelMapper.map(clienteNovoDto, ClienteModel.class);
            validarCpfEmailParaCadastro(clienteNovo);
            ClienteModel clienteSalvo = clienteRepository.save(clienteNovo);

            return modelMapper.map(clienteSalvo, ClienteResponseDTO.class);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "Erro de integridade ao salvar o cliente " + clienteNovoDto.getNome() + ".", e
            );
        }
    }

    @Transactional
    public ClienteResponseDTO update(int id, ClienteRequestDTO clienteRequestDto) {
        try {
            ClienteModel clienteAtualizadoModel = modelMapper.map(clienteRequestDto, ClienteModel.class);
            ClienteModel clienteExistenteModel = clienteRepository.findById(id)
                    .orElseThrow(() -> new ObjectNotFoundException(
                            "Cliente com ID " + id + " não encontrado."
                    ));
            validarCpfEmailParaAtualizar(id, clienteAtualizadoModel);

            clienteExistenteModel.setNome(clienteAtualizadoModel.getNome());
            clienteExistenteModel.setCpf(clienteAtualizadoModel.getCpf());
            clienteExistenteModel.setEmail(clienteAtualizadoModel.getEmail());
            clienteExistenteModel.setTelefone(clienteAtualizadoModel.getTelefone());

            ClienteModel clienteSalvo = clienteRepository.save(clienteExistenteModel);

            return modelMapper.map(clienteSalvo, ClienteResponseDTO.class);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "Erro de integridade ao atualizar o cliente " + clienteRequestDto.getNome() + ".", e
            );
        }
    }

    @Transactional
    public void delete(int id) {
        try {
            findById(id);
            clienteRepository.deleteById(id);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
              "Não foi possível excluir o cliente, pois ele possui vínculos com outros registros.", e
            );
        }
    }

    private void validarCpfEmailParaCadastro(ClienteModel cliente) {
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new ConstraintException(
                    "Já existe um cliente cadastrado com o CPF " + cliente.getCpf() + "."
            );
        }

        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new ConstraintException(
                    "Já existe um cliente cadastrado com o e-mail " + cliente.getEmail() + "."
            );
        }
    }

    private void validarCpfEmailParaAtualizar(int id, ClienteModel cliente) {
        clienteRepository.findByCpf(cliente.getCpf()).filter(clienteEncontrado -> clienteEncontrado.getId() != id)
                .ifPresent(clienteEncontrado -> {
                    throw new ConstraintException(
                            "Já existe um cliente cadastrado com o CPF " + cliente.getCpf() + "."
                    );
                });
        clienteRepository.findByEmail(cliente.getEmail()).filter(clienteEncontrado -> clienteEncontrado.getId() != id)
                .ifPresent(clienteEncontrado -> {
                    throw new ConstraintException(
                            "Já existe um cliente cadastrado com o e-mail " + cliente.getEmail() + "."
                    );
                });
    }
}
