package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.PedidoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.PedidoResponseDTO;
import br.com.ajufood.pedeai.service.PedidoService;
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
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {
  private final PedidoService pedidoService;

  @Operation(summary = "Lista todos os pedidos")
  @GetMapping
  public ResponseEntity<List<PedidoResponseDTO>> getAll() {
    return ResponseEntity.ok(pedidoService.findAll());
  }

  @Operation(summary = "Busca um pedido pelo id")
  @GetMapping("/{id}")
  public ResponseEntity<PedidoResponseDTO> getById(@PathVariable int id) {
    return ResponseEntity.ok(pedidoService.findById(id));
  }

  @Operation(summary = "Cadastra um novo pedido")
  @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso")
  @PostMapping
  public ResponseEntity<PedidoResponseDTO> create(
    @Valid @RequestBody PedidoRequestDTO pedidoRequestDTO)
  {
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(pedidoService.save(pedidoRequestDTO));
  }

  @Operation(summary = "Atualiza um  pedido pelo id")
  @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso")
  @PutMapping("/{id}")
  public ResponseEntity<PedidoResponseDTO> update(
    @PathVariable int id, @Valid @RequestBody PedidoRequestDTO pedidoRequestDTO)
  {
    return ResponseEntity.ok(pedidoService.update(id, pedidoRequestDTO));
  }

  @Operation(summary = "Remove um pedido pelo id")
  @ApiResponse(responseCode = "204", description = "Pedido removido com sucesso")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    pedidoService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
