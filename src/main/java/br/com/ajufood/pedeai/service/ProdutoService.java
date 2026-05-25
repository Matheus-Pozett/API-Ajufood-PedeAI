package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.exception.ConstraintException;
import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.model.ProdutoModel;
import br.com.ajufood.pedeai.repository.ProdutoRepository;
import br.com.ajufood.pedeai.rest.dto.request.ProdutoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.ProdutoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {
  private final ProdutoRepository produtoRepository;
  private final ModelMapper modelMapper;

  @Transactional(readOnly = true)
  public ProdutoResponseDTO findById(int id) {
    ProdutoModel produto = produtoRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(
        "Produto com ID " + id + " não encontrado"
      ));

    return modelMapper.map(produto, ProdutoResponseDTO.class);
  }

  @Transactional(readOnly = true)
  public List<ProdutoResponseDTO> findAll() {
    return produtoRepository.findAll().stream()
      .map(produto -> modelMapper.map(produto, ProdutoResponseDTO.class))
      .toList();
  }

  @Transactional
  public ProdutoResponseDTO save(ProdutoRequestDTO produtoRequestDTO) {
    try {
      ProdutoModel produto = modelMapper.map(produtoRequestDTO, ProdutoModel.class);
      validarNomeProduto(produto.getNome(), null);
      ProdutoModel produtoSalvo = produtoRepository.save(produto);

      return modelMapper.map(produtoSalvo, ProdutoResponseDTO.class);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException(
        "Erro de integridade ao salvar o produto " + produtoRequestDTO.getNome() + ".", e);
    }
  }

  @Transactional
  public ProdutoResponseDTO update(int id, ProdutoRequestDTO produtoRequestDTO) {
    try {
      ProdutoModel produtoExistente = findEntityById(id);

      validarNomeProduto(produtoRequestDTO.getNome(), id);

      // O ModelMapper joga os dados do DTO para dentro do produtoExistente
      modelMapper.map(produtoRequestDTO, produtoExistente);

      ProdutoModel produtoSalvo = produtoRepository.save(produtoExistente);

      return modelMapper.map(produtoSalvo, ProdutoResponseDTO.class);

    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException(
        "Erro de integridade ao atualizar o produto " + produtoRequestDTO.getNome() + ".", e
      );
    }
  }

  @Transactional
  public void delete(int id) {
    try {
      findEntityById(id);
      produtoRepository.deleteById(id);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException(
        "Não foi possível excluir o produto, pois ele possui vínculos com outros registros.", e
      );
    }
  }

  private void validarNomeProduto(String nome, Integer id) {
    boolean existe;
    if (id == null) {
      existe = produtoRepository.existsByNome(nome);
    } else {
      existe = produtoRepository.existsByNomeAndIdNot(nome, id);
    }

    if (existe) {
      throw new ConstraintException("Já existe um produto cadastrado com esse nome: " + nome);
    }
  }

  private ProdutoModel findEntityById(int id) {
    return produtoRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(
        "Produto com ID " + id + " não encontrado"
      ));
  }
}
