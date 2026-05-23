package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.ItensPedidoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.ItensPedidoResponseDTO;
import br.com.ajufood.pedeai.service.ItensPedidoService;
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
@RequestMapping("/itens-pedido")
@RequiredArgsConstructor
public class ItensPedidoController {
  private final ItensPedidoService itensPedidoService;

  @Operation(summary = "Lista todos os itens dos pedidos")
  @GetMapping
  public ResponseEntity<List<ItensPedidoResponseDTO>> getAll() {
    return ResponseEntity.ok(itensPedidoService.findAll());
  }

  @Operation(summary = "Busca um item do pedido pelo id")
  @GetMapping("/{id}")
  public ResponseEntity<ItensPedidoResponseDTO> getById(@PathVariable long id) {
    return ResponseEntity.ok(itensPedidoService.findById(id));
  }

  @Operation(summary = "Cadastra um novo item no pedido")
  @ApiResponse(responseCode = "201", description = "Item do Pedido criado com sucesso")
  @PostMapping
  public ResponseEntity<ItensPedidoResponseDTO> create(
    @Valid @RequestBody ItensPedidoRequestDTO itensPedidoRequestDTO)
  {
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(itensPedidoService.save(itensPedidoRequestDTO));
  }

  @Operation(summary = "Atualiza um item do pedido pelo id")
  @ApiResponse(responseCode = "200", description = "Item do Pedido atualizado com sucesso")
  @PutMapping("/{id}")
  public ResponseEntity<ItensPedidoResponseDTO> update(
    @PathVariable long id, @Valid @RequestBody ItensPedidoRequestDTO itensPedidoRequestDTO)
  {
    return ResponseEntity.ok(itensPedidoService.update(id, itensPedidoRequestDTO));
  }

  @Operation(summary = "Remove um item do pedido pelo id")
  @ApiResponse(responseCode = "204", description = "Item do Pedido removido com sucesso")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable long id) {
    itensPedidoService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
