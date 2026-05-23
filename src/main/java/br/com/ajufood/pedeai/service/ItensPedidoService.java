package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.model.ItensPedidoModel;
import br.com.ajufood.pedeai.repository.ItensPedidoRepository;
import br.com.ajufood.pedeai.rest.dto.request.ItensPedidoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.ItensPedidoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItensPedidoService {
  private final ItensPedidoRepository itensPedidoRepository;
  private final ModelMapper modelMapper;

  @Transactional(readOnly = true)
  public List<ItensPedidoResponseDTO> findAll() {
    return itensPedidoRepository.findAll().stream()
      .map(pedido -> modelMapper.map(pedido, ItensPedidoResponseDTO.class))
      .toList();
  }

  @Transactional(readOnly = true)
  public ItensPedidoResponseDTO findById(long id) {
    ItensPedidoModel itensPedidoModel = itensPedidoRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(
        "Itens do pedido com ID " + id + " não encontrado"
      ));

    return modelMapper.map(itensPedidoModel, ItensPedidoResponseDTO.class);
  }

  @Transactional
  public ItensPedidoResponseDTO save(ItensPedidoRequestDTO itensPedidoRequestDTO) {
    try {
      ItensPedidoModel itensPedido = modelMapper.map(itensPedidoRequestDTO, ItensPedidoModel.class);

      ItensPedidoModel itensPedidoSalvo = itensPedidoRepository.save(itensPedido);

      return modelMapper.map(itensPedidoSalvo, ItensPedidoResponseDTO.class);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException(
        "Erro de integridade ao salvar o item do pedido com id "
          + itensPedidoRequestDTO.getPedidoId() + ".", e);
    }
  }

  @Transactional
  public ItensPedidoResponseDTO update(long id, ItensPedidoRequestDTO itensPedidoRequestDTO) {
    try {
      ItensPedidoModel itensPedidoExistente = findEntityById(id);
      modelMapper.map(itensPedidoRequestDTO, itensPedidoExistente);

      ItensPedidoModel itensPedidoSalvo = itensPedidoRepository.save(itensPedidoExistente);

      return modelMapper.map(itensPedidoSalvo, ItensPedidoResponseDTO.class);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException(
        "Erro de integridade ao atualizar o item do pedido com id "
          + itensPedidoRequestDTO.getPedidoId() + ".", e
      );
    }
  }

  @Transactional
  public void delete(long id) {
    try {
      findEntityById(id);
      itensPedidoRepository.deleteById(id);
    }
    catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException(
        "Não foi possível excluir o item do pedido, " +
          "pois ele possui vínculos com outros registros.", e
      );
    }
  }

  private ItensPedidoModel findEntityById(long id) {
    return itensPedidoRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(
        "Itens do pedido com ID " + id + " não encontrado"
      ));
  }
}
