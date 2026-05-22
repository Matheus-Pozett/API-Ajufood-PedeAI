package br.com.ajufood.pedeai.repository;

import br.com.ajufood.pedeai.model.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, Integer> {
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    Optional<ClienteModel> findByCpf(String cpf);
    Optional<ClienteModel> findByEmail(String email);
}
