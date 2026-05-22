package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.ProdutoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.ProdutoResponseDTO;
import br.com.ajufood.pedeai.service.ProdutoService;
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
@RequestMapping("/produto")
@RequiredArgsConstructor
public class ProdutoController {
  private final ProdutoService produtoService;

  @Operation(summary = "Lista todos os produtos")
  @GetMapping
  public ResponseEntity<List<ProdutoResponseDTO>> getAll() {
    return ResponseEntity.ok(produtoService.findAll());
  }

  @Operation(summary = "Busca um produto pelo id")
  @GetMapping("/{id}")
  public ResponseEntity<ProdutoResponseDTO> getById(@PathVariable int id) {
    return ResponseEntity.ok(produtoService.findById(id));
  }

  @Operation(summary = "Cadastra um novo produto")
  @ApiResponse(responseCode = "201", description = "Produto criado com sucesso")
  @PostMapping
  public ResponseEntity<ProdutoResponseDTO> create(
    @Valid @RequestBody ProdutoRequestDTO produtoRequestDTO) {
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(produtoService.save(produtoRequestDTO));
  }

  @Operation(summary = "Atualiza um produto pelo id")
  @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso")
  @PutMapping("/{id}")
  public ResponseEntity<ProdutoResponseDTO> update(
    @PathVariable int id, @Valid @RequestBody ProdutoRequestDTO produtoRequestDTO) {
    return ResponseEntity.status(HttpStatus.OK)
      .body(produtoService.update(id, produtoRequestDTO));
  }

  @Operation(summary = "Deleta um produto pelo id")
  @ApiResponse(responseCode = "204", description = "Produto removido com sucesso")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    produtoService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
