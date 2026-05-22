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
        "Produto com ID " + id + "não encontrado"
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
      validarNomeProdutoParaCadastro(produto);
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
      ProdutoModel produtoAtualizadoModel = modelMapper.map(produtoRequestDTO, ProdutoModel.class);
      ProdutoModel produtoExistenteModel = findByIdPrivate(id);

      validarNomeProdutoParaCadastro(produtoAtualizadoModel);

      produtoExistenteModel.setNome(produtoAtualizadoModel.getNome());
      produtoExistenteModel.setPreco(produtoAtualizadoModel.getPreco());
      produtoExistenteModel.setCategoriaProdutoId(produtoAtualizadoModel.getCategoriaProdutoId());
      produtoExistenteModel.setDescricao(produtoAtualizadoModel.getDescricao());
      produtoExistenteModel.setDisponivel(produtoAtualizadoModel.isDisponivel());

      ProdutoModel produtoSalvo = produtoRepository.save(produtoExistenteModel);

      return modelMapper.map(produtoSalvo, ProdutoResponseDTO.class);

    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException(
        "Erro de integridade ao atualizar o produto " + produtoRequestDTO.getNome() + ".", e
      );
    }
  }

  private void validarNomeProdutoParaCadastro(ProdutoModel produto) {
    if(produtoRepository.existsByNome(produto.getNome())) {
      throw new ConstraintException(
        "Já existe um produto cadastrado com esse nome " + produto.getNome() + "."
      );
    }
  }

  private ProdutoModel findByIdPrivate(int id) {
    return produtoRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(
        "Produto com ID " + id + "não encontrado"
      ));
  }
}
