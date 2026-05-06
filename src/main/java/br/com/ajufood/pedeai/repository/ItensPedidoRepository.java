package br.com.ajufood.pedeai.repository;

import br.com.ajufood.pedeai.model.ItensPedidoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItensPedidoRepository extends JpaRepository<ItensPedidoModel, Long> {
}
