package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.model.EnderecoModel;
import br.com.ajufood.pedeai.repository.EnderecoRepository;
import br.com.ajufood.pedeai.rest.dto.request.EnderecoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.EnderecoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnderecoService {
  private final EnderecoRepository enderecoRepository;
  private final ModelMapper modelMapper;

  @Transactional(readOnly = true)
  public List<EnderecoResponseDTO> findAll() {
    return enderecoRepository.findAll().stream()
      .map(endereco -> modelMapper.map(endereco, EnderecoResponseDTO.class))
      .toList();
  }

  @Transactional(readOnly = true)
  public EnderecoResponseDTO findById(int id) {
    EnderecoModel enderecoModel = enderecoRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(
        "Endereço com ID " + id + " não encontrado"
      ));

    return modelMapper.map(enderecoModel, EnderecoResponseDTO.class);
  }

  @Transactional
  public EnderecoResponseDTO save(EnderecoRequestDTO enderecoRequestDTO) {
    try {
      EnderecoModel endereco = modelMapper.map(enderecoRequestDTO, EnderecoModel.class);

      EnderecoModel enderecoSalvo = enderecoRepository.save(endereco);

      return modelMapper.map(enderecoSalvo, EnderecoResponseDTO.class);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException(
        "Erro de integridade ao salvar o endereço " + enderecoRequestDTO.getEndereco() + ".", e
      );
    }
  }

  @Transactional
  public EnderecoResponseDTO update(int id, EnderecoRequestDTO enderecoRequestDTO) {
    try {
      EnderecoModel enderecoExistente = findEntityById(id);

      modelMapper.map(enderecoRequestDTO, enderecoExistente);

      EnderecoModel enderecoSalvo = enderecoRepository.save(enderecoExistente);

      return modelMapper.map(enderecoSalvo, EnderecoResponseDTO.class);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException(
        "Erro de integridade ao atualizar o endereço " + enderecoRequestDTO.getEndereco() + ".", e
      );
    }
  }

  @Transactional
  public void delete(int id) {
    try {
      findEntityById(id);
      enderecoRepository.deleteById(id);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException(
        "Não foi possível excluir o endereço, pois ele possui vínculos com outros registros.", e
      );
    }
  }

  private EnderecoModel findEntityById(int id) {
    return enderecoRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(
        "Endereco com ID " + id + " não encontrado"
      ));
  }
}
