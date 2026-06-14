package br.com.ajufood.pedeai.repository;

import br.com.ajufood.pedeai.model.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoModel, Integer> {
  boolean existsByNome(String nome);
  boolean existsByNomeAndIdNot(String name, Integer id);

  List<ProdutoModel> findByDisponivelTrue();

  List<ProdutoModel> findByCategoriaProdutoIdAndDisponivelTrue(Long categoriaId);

}
