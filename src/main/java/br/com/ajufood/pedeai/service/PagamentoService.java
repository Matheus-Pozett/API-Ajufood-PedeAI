package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.model.PagamentoModel;
import br.com.ajufood.pedeai.repository.PagamentoRepository;
import br.com.ajufood.pedeai.rest.dto.request.PagamentoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.PagamentoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PagamentoService {
  private final PagamentoRepository pagamentoRepository;
  private final ModelMapper modelMapper;

  @Transactional(readOnly = true)
  public List<PagamentoResponseDTO> findAll() {
    return pagamentoRepository.findAll().stream()
      .map(pagamento -> modelMapper.map(pagamento, PagamentoResponseDTO.class))
      .toList();
  }

  @Transactional(readOnly = true)
  public PagamentoResponseDTO findById(int id) {
    PagamentoModel pagamento = pagamentoRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(
        "Pagamento com ID " + id + " não encontrado"
      ));

    return modelMapper.map(pagamento, PagamentoResponseDTO.class);
  }

  @Transactional
  public PagamentoResponseDTO save(PagamentoRequestDTO pagamentoRequestDTO) {
    try {
      PagamentoModel pagamento = modelMapper.map(pagamentoRequestDTO, PagamentoModel.class);

      PagamentoModel pagamentoSalvo = pagamentoRepository.save(pagamento);

      return modelMapper.map(pagamentoSalvo, PagamentoResponseDTO.class);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException(
        "Erro de integridade ao salvar o pagamento", e);
    }
  }

  @Transactional
  public PagamentoResponseDTO update(int id, PagamentoRequestDTO pagamentoRequestDTO) {
    try {
      PagamentoModel pagamentoExistente = findEntityById(id);

      modelMapper.map(pagamentoRequestDTO, pagamentoExistente);

      PagamentoModel pagamentoSalvo = pagamentoRepository.save(pagamentoExistente);

      return modelMapper.map(pagamentoSalvo, PagamentoResponseDTO.class);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException(
        "Erro de integridade ao atualizar o pagamento", e
      );
    }
  }

  @Transactional
  public void delete(int id) {
    try {
      findEntityById(id);
      pagamentoRepository.deleteById(id);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException(
        "Não foi possível excluir o pagamento, pois ele possui vínculos com outros registros.", e
      );
    }
  }

  private PagamentoModel findEntityById(int id) {
    return pagamentoRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(
        "Pagamento com ID " + id + " não encontrado"
      ));
  }
}
