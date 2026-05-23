package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.PagamentoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.PagamentoResponseDTO;
import br.com.ajufood.pedeai.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {
  private final PagamentoService pagamentoService;

  @Operation(summary = "Lista todos os pagamentos")
  @GetMapping
  public ResponseEntity<List<PagamentoResponseDTO>> getAll() {
    return ResponseEntity.ok(pagamentoService.findAll());
  }

  @Operation(summary = "Busca pagamento pelo id")
  @GetMapping("/{id}")
  public ResponseEntity<PagamentoResponseDTO> getById(@PathVariable int id) {
    return ResponseEntity.ok(pagamentoService.findById(id));
  }

  @Operation(summary = "Cadastra um novo pagamento")
  @ApiResponse(responseCode = "201", description = "Pagamento criado com sucesso")
  @PostMapping
  public ResponseEntity<PagamentoResponseDTO> create(
    @Valid @RequestBody PagamentoRequestDTO pagamentoRequestDTO)
  {
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(pagamentoService.save(pagamentoRequestDTO));
  }

  @Operation(summary = "Atualiza um pagamento pelo id")
  @ApiResponse(responseCode = "200", description = "Pagamento atualizado com sucesso")
  @PutMapping("/{id}")
  public ResponseEntity<PagamentoResponseDTO> update(
    @PathVariable int id,
    @Valid @RequestBody PagamentoRequestDTO pagamentoRequestDTO)
  {
    return ResponseEntity.ok(pagamentoService.update(id, pagamentoRequestDTO));
  }

  @Operation(summary = "Remove um pagamento pelo id")
  @ApiResponse(responseCode = "204", description = "Pagamento removido com sucesso")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    pagamentoService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
