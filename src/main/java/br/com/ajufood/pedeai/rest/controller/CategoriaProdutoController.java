package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.CategoriaProdutoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.CategoriaProdutoResponseDTO;
import br.com.ajufood.pedeai.service.CategoriaProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/categoria")
@RequiredArgsConstructor // Cria o construtor automaticamente com as variáveis 'final'
public class CategoriaProdutoController {

  private final CategoriaProdutoService categoriaProdutoService;

  @Operation(summary = "Lista todas as categorias")
  @GetMapping
  public ResponseEntity<List<CategoriaProdutoResponseDTO>> getAll() {
    return ResponseEntity.ok(categoriaProdutoService.findAll());
  }

  @Operation(summary = "Lista uma categoria pelo id")
  @GetMapping("/{id}")
  public ResponseEntity<CategoriaProdutoResponseDTO> getById(
    @PathVariable int id
  ) {
    return ResponseEntity.ok(categoriaProdutoService.findById(id));
  }

  @Operation(summary = "Cadastra uma nova categoria")
  @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso")
  @PostMapping
  public ResponseEntity<CategoriaProdutoResponseDTO> create(
    @Valid @RequestBody CategoriaProdutoRequestDTO categoriaProdutoRequestDTO
    ) {
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(categoriaProdutoService.save(categoriaProdutoRequestDTO));
  }

  @Operation(summary = "Atualiza uma categoria")
  @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso")
  @PutMapping("/{id}")
  public ResponseEntity<CategoriaProdutoResponseDTO> update(
    @PathVariable int id,
    @Valid @RequestBody CategoriaProdutoRequestDTO categoriaProdutoRequestDTO
  ) {
    return ResponseEntity.ok(categoriaProdutoService.update(id , categoriaProdutoRequestDTO));
  }

  @Operation(summary = "Remove uma categoria pelo id")
  @ApiResponse(responseCode = "204", description = "Categoria removida com sucesso")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    categoriaProdutoService.delete(id);
    return ResponseEntity.noContent().build();
  }

}
