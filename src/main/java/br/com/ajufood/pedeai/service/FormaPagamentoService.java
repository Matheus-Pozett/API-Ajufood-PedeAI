package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.model.FormaPagamentoModel;
import br.com.ajufood.pedeai.repository.FormaPagamentoRepository;
import br.com.ajufood.pedeai.rest.dto.request.FormaPagamentoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.FormaPagamentoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormaPagamentoService {
  private final FormaPagamentoRepository formaPagamentoRepository;
  private final ModelMapper modelMapper;

  @Transactional(readOnly = true)
  public List<FormaPagamentoResponseDTO> findAll() {
    return formaPagamentoRepository.findAll().stream()
      .map(formaPagamento -> modelMapper.map(formaPagamento, FormaPagamentoResponseDTO.class))
      .toList();
  }

  @Transactional(readOnly = true)
  public FormaPagamentoResponseDTO findById(int id) {
    FormaPagamentoModel formaPagamentoModel = formaPagamentoRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(
        "Forma de pagamento com ID " + id + " não encontrado"
      ));

    return modelMapper.map(formaPagamentoModel, FormaPagamentoResponseDTO.class);
  }

  @Transactional
  public FormaPagamentoResponseDTO save(FormaPagamentoRequestDTO formaPagamentoRequestDTO) {
    try {
      FormaPagamentoModel formaPagamentoModel = modelMapper.map(formaPagamentoRequestDTO, FormaPagamentoModel.class);

      FormaPagamentoModel formaPagamentoSalvo = formaPagamentoRepository.save(formaPagamentoModel);

      return modelMapper.map(formaPagamentoSalvo, FormaPagamentoResponseDTO.class);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException(
        "Erro de integridade ao salvar a forma de pagamento " + formaPagamentoRequestDTO.getNome() + ".", e
      );
    }
  }

  @Transactional
  public FormaPagamentoResponseDTO update(
    int id, FormaPagamentoRequestDTO formaPagamentoRequestDTO) {
    try {
      FormaPagamentoModel formaPagamentoExistente = findEntityById(id);

      modelMapper.map(formaPagamentoRequestDTO, formaPagamentoExistente);

      FormaPagamentoModel formaPagamentoSalvo = formaPagamentoRepository.save(formaPagamentoExistente);

      return modelMapper.map(formaPagamentoSalvo, FormaPagamentoResponseDTO.class);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException(
        "Erro de integridade ao atualizar a forma de pagamento "
          + formaPagamentoRequestDTO.getNome() + ".", e
      );
    }
  }

  @Transactional
  public void delete(int id) {
    try {
      findEntityById(id);
      formaPagamentoRepository.deleteById(id);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException(
        "Não foi possível excluir a Forma de pagamento," +
          " pois ela possui vínculos com outros registros.", e
      );
    }
  }

  private FormaPagamentoModel findEntityById(int id) {
    return formaPagamentoRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(
        "Forma de pagamento com ID " + id + " não encontrado"
      ));
  }
}
