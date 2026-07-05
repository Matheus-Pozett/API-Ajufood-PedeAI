package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.ClienteRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.ClienteResponseDTO;
import br.com.ajufood.pedeai.rest.dto.response.PedidoResumoDTO;
import br.com.ajufood.pedeai.service.ClienteService;
import br.com.ajufood.pedeai.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("clientes")
@RequiredArgsConstructor // Cria o construtor automaticamente com as variáveis 'final'
public class ClienteController {

    private final ClienteService clienteService;
    private final PedidoService pedidoService;

    @Operation(summary = "Busca um cliente pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> getById(@PathVariable int id) {
        return ResponseEntity.ok(clienteService.findById(id));
    }

    @Operation(summary = "Lista todos os clientes")
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> getAll() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    @Operation(summary = "Cadastra um novo cliente")
    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso")
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> create(
      @Valid @RequestBody ClienteRequestDTO clienteRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
          .body(clienteService.save(clienteRequestDTO));
    }

    @Operation(summary = "Atualiza um cliente pelo id")
    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> update(
      @PathVariable int id, @Valid @RequestBody ClienteRequestDTO clienteRequestDTO
    ) {
        return ResponseEntity.status(HttpStatus.OK)
          .body(clienteService.update(id, clienteRequestDTO));
    }

    @Operation(summary = "Remove um cliente pelo id")
    @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Retorna o histórico de pedidos de um cliente")
    @GetMapping("/{clienteId}/pedidos")
    public ResponseEntity<Page<PedidoResumoDTO>> getHistoricoPedidos(
      @PathVariable int clienteId,
      @RequestParam(required = false) String status,
      @RequestParam(defaultValue = "0") int pagina,
      @RequestParam(defaultValue = "10") int tamanho) {
        Page<PedidoResumoDTO> historico = pedidoService.findHistory(clienteId, status, pagina, tamanho);
        return ResponseEntity.ok(historico);
    }
}
