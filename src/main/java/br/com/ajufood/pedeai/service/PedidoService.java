package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.model.PedidoModel;
import br.com.ajufood.pedeai.repository.PedidoRepository;
import br.com.ajufood.pedeai.rest.dto.request.PedidoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.PedidoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {
  private final PedidoRepository pedidoRepository;
  private final ModelMapper modelMapper;

  @Transactional(readOnly = true)
  public List<PedidoResponseDTO> findAll() {
    return pedidoRepository.findAll().stream()
      .map(pedido -> modelMapper.map(pedido, PedidoResponseDTO.class))
      .toList();
  }

  @Transactional(readOnly = true)
  public PedidoResponseDTO findById(int id) {
    PedidoModel pedido = pedidoRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(
        "Pedido com ID " + id + " não encontrado"
      ));

    return modelMapper.map(pedido, PedidoResponseDTO.class);
  }

  @Transactional
  public PedidoResponseDTO save(PedidoRequestDTO pedidoRequestDTO) {
    try {
      PedidoModel pedido = modelMapper.map(pedidoRequestDTO, PedidoModel.class);

      PedidoModel pedidoSalvo = pedidoRepository.save(pedido);

      return modelMapper.map(pedidoSalvo, PedidoResponseDTO.class);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException(
        "Erro de integridade ao salvar o pedido", e);
    }
  }

  @Transactional
  public PedidoResponseDTO update(int id, PedidoRequestDTO pedidoRequestDTO) {
    try {
      PedidoModel pedidoExistente = findEntityById(id);
      modelMapper.map(pedidoRequestDTO, pedidoExistente);

      PedidoModel pedidoSalvo = pedidoRepository.save(pedidoExistente);

      return modelMapper.map(pedidoSalvo, PedidoResponseDTO.class);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException(
        "Erro de integridade ao atualizar o pedido", e
      );
    }
  }

  @Transactional
  public void delete(int id) {
    try {
      findEntityById(id);
      pedidoRepository.deleteById(id);
    }
    catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException(
        "Não foi possível excluir o pedido, pois ele possui vínculos com outros registros.", e
      );
    }
  }

  private PedidoModel findEntityById(int id) {
    return pedidoRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(
      "Pedido com ID " + id + " não encontrado"
    ));
  }
}
