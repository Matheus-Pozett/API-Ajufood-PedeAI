package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.ClienteRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.ClienteResponseDTO;
import br.com.ajufood.pedeai.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cliente")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @Operation(summary = "Busca um cliente pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> obterPorId(@PathVariable int id) {
        return ResponseEntity.ok(clienteService.obterPorId(id));
    }

    @Operation(summary = "Lista todos os clientes")
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> obterTodos() {
        return ResponseEntity.ok(clienteService.obterTodos());
    }

    @Operation(summary = "Cadastra um novo cliente")
    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso")
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> salvar(@Valid @RequestBody ClienteRequestDTO clienteRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.salvar(clienteRequestDTO));
    }

    @Operation(summary = "Cliente atualizado com sucesso")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(
      @PathVariable int id, @Valid @RequestBody ClienteRequestDTO clienteRequestDTO
    ) {
        return ResponseEntity.status(HttpStatus.OK)
          .body(clienteService.atualizar(id, clienteRequestDTO));
    }

    @Operation(summary = "Remove um cliente pelo id")
    @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
