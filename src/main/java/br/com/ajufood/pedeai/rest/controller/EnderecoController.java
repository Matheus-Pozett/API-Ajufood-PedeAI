package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.EnderecoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.EnderecoResponseDTO;
import br.com.ajufood.pedeai.service.EnderecoService;
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
@RequestMapping("/enderecos")
@RequiredArgsConstructor
public class EnderecoController {
  private final EnderecoService enderecoService;

  @Operation(summary = "Lista todos os endereços")
  @GetMapping
  public ResponseEntity<List<EnderecoResponseDTO>> getAll() {
    return ResponseEntity.ok(enderecoService.findAll());
  }

  @Operation(summary = "Busca um endereço pelo id")
  @GetMapping("/{id}")
  public ResponseEntity<EnderecoResponseDTO> getById(@PathVariable int id) {
    return ResponseEntity.ok(enderecoService.findById(id));
  }

  @Operation(summary = "Cadastra um novo endereço")
  @ApiResponse(responseCode = "201", description = "Endereço criado com sucesso")
  @PostMapping
  public ResponseEntity<EnderecoResponseDTO> create(
    @Valid @RequestBody EnderecoRequestDTO enderecoRequestDTO)
  {
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(enderecoService.save(enderecoRequestDTO));
  }

  @Operation(summary = "Atualiza um endereço pelo id")
  @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso")
  @PutMapping("/{id}")
  public ResponseEntity<EnderecoResponseDTO> update(
    @PathVariable int id,
    @Valid @RequestBody EnderecoRequestDTO enderecoRequestDTO)
  {
    return ResponseEntity.ok(enderecoService.update(id, enderecoRequestDTO));
  }

  @Operation(summary = "Remove um endereço pelo id")
  @ApiResponse(responseCode = "204", description = "Endereço removido com sucesso")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    enderecoService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
