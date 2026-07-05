package br.com.ajufood.pedeai.repository;

import br.com.ajufood.pedeai.model.PedidoModel;
import br.com.ajufood.pedeai.repository.projection.VendaCategoriaProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoModel, Integer> {

  @Query("""
        SELECT p FROM PedidoModel p
        JOIN FETCH p.cliente c
        JOIN FETCH p.endereco e
        LEFT JOIN FETCH p.itensPedido i
        LEFT JOIN FETCH i.produto pr
        WHERE c.id = :clienteId
          AND (:status IS NULL OR UPPER(p.status) = UPPER(:status))
        ORDER BY p.dataHora DESC
    """)
  Page<PedidoModel> findHistoricoByCliente(
    @Param("clienteId") int clienteId,
    @Param("status") String status,
    Pageable pageable
  );

  @Query("""
      SELECT
          cp.nome AS nomeCategoria,
          SUM(ip.quantidade) AS totalItensVendidos,
          COUNT(DISTINCT p.id) AS totalPedidos,
          SUM(ip.subTotal) AS totalFaturado
      FROM PedidoModel p
      JOIN p.itensPedido ip
      JOIN ip.produto pr
      JOIN pr.categoriaProduto cp
      WHERE UPPER(p.status) = 'ENTREGUE'
        AND p.dataHora >= :dataInicio
        AND p.dataHora < :dataFimExclusivo
      GROUP BY cp.nome
  """)
  List<VendaCategoriaProjection> buscarVendasPorCategoria(
    @Param("dataInicio") LocalDateTime dataInicio,
    @Param("dataFimExclusivo") LocalDateTime dataFimExclusivo
  );
}
