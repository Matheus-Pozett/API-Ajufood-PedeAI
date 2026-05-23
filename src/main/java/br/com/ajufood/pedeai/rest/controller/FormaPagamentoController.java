package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.FormaPagamentoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.FormaPagamentoResponseDTO;
import br.com.ajufood.pedeai.service.FormaPagamentoService;
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
@RequestMapping("/forma-pagamentos")
@RequiredArgsConstructor
public class FormaPagamentoController {
  private final FormaPagamentoService formaPagamentoService;

  @Operation(summary = "Lista todas as formas de pagamento")
  @GetMapping
  public ResponseEntity<List<FormaPagamentoResponseDTO>> getAll() {
    return ResponseEntity.ok(formaPagamentoService.findAll());
  }

  @Operation(summary = "Busca uma forma de pagamento pelo id")
  @GetMapping("/{id}")
  public ResponseEntity<FormaPagamentoResponseDTO> getById(@PathVariable int id) {
    return ResponseEntity.ok(formaPagamentoService.findById(id));
  }

  @Operation(summary = "Cadastra uma nova forma de pagamento")
  @ApiResponse(responseCode = "201", description = "Forma de pagamento criada com sucesso")
  @PostMapping
  public ResponseEntity<FormaPagamentoResponseDTO> create(
    @Valid @RequestBody FormaPagamentoRequestDTO formaPagamentoRequestDTO)
  {
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(formaPagamentoService.save(formaPagamentoRequestDTO));
  }

  @Operation(summary = "Atualiza uma forma de pagamento pelo id")
  @ApiResponse(responseCode = "200", description = "Forma de pagamento atualizada com sucesso")
  @PutMapping("/{id}")
  public ResponseEntity<FormaPagamentoResponseDTO> update(
    @PathVariable int id,
    @Valid @RequestBody FormaPagamentoRequestDTO formaPagamentoRequestDTO)
  {
    return ResponseEntity.ok(formaPagamentoService.update(id, formaPagamentoRequestDTO));
  }

  @Operation(summary = "Remove uma forma de pagamento pelo id")
  @ApiResponse(responseCode = "204", description = "Forma de pagamento removida com sucesso")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    formaPagamentoService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
