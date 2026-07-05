package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.model.PedidoModel;
import br.com.ajufood.pedeai.repository.ClienteRepository;
import br.com.ajufood.pedeai.repository.PedidoRepository;
import br.com.ajufood.pedeai.repository.projection.VendaCategoriaProjection;
import br.com.ajufood.pedeai.rest.dto.request.PedidoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.ItemPedidoDTO;
import br.com.ajufood.pedeai.rest.dto.response.PedidoResponseDTO;
import br.com.ajufood.pedeai.rest.dto.response.PedidoResumoDTO;
import br.com.ajufood.pedeai.rest.dto.response.RelatorioVendaCategoriaDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {
  private final PedidoRepository pedidoRepository;
  private final ClienteRepository clienteRepository;
  private final ModelMapper modelMapper;

  @Transactional(readOnly = true)
  public List<PedidoResponseDTO> findAll() {
    return pedidoRepository.findAll().stream()
      .map(pedido -> modelMapper.map(pedido, PedidoResponseDTO.class))
      .toList();
  }

  @Transactional(readOnly = true)
  public PedidoResponseDTO findById(int id) {
    PedidoModel pedido = pedidoRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(
        "Pedido com ID " + id + " não encontrado"
      ));

    return modelMapper.map(pedido, PedidoResponseDTO.class);
  }

  @Transactional
  public PedidoResponseDTO save(PedidoRequestDTO pedidoRequestDTO) {
    try {
      PedidoModel pedido = modelMapper.map(pedidoRequestDTO, PedidoModel.class);

      PedidoModel pedidoSalvo = pedidoRepository.save(pedido);

      return modelMapper.map(pedidoSalvo, PedidoResponseDTO.class);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException(
        "Erro de integridade ao salvar o pedido", e);
    }
  }

  @Transactional
  public PedidoResponseDTO update(int id, PedidoRequestDTO pedidoRequestDTO) {
    try {
      PedidoModel pedidoExistente = findEntityById(id);
      modelMapper.map(pedidoRequestDTO, pedidoExistente);

      PedidoModel pedidoSalvo = pedidoRepository.save(pedidoExistente);

      return modelMapper.map(pedidoSalvo, PedidoResponseDTO.class);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException(
        "Erro de integridade ao atualizar o pedido", e
      );
    }
  }

  @Transactional
  public void delete(int id) {
    try {
      findEntityById(id);
      pedidoRepository.deleteById(id);
    }
    catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException(
        "Não foi possível excluir o pedido, pois ele possui vínculos com outros registros.", e
      );
    }
  }

  @Transactional(readOnly = true)
  public Page<PedidoResumoDTO> findHistory(int clienteId, String status, int pagina, int tamanho) {

    // Valida se o cliente existe → lança 404 se não existir
    if (!clienteRepository.existsById(clienteId)) {
      throw new ObjectNotFoundException(
        "Cliente com ID " + clienteId + " não encontrado"
      );
    }

    Pageable pageable = PageRequest.of(pagina, tamanho);

    return pedidoRepository
      .findHistoricoByCliente(clienteId, status, pageable)
      .map(this::toResumoDTO);
  }

  @Transactional(readOnly = true)
  public List<RelatorioVendaCategoriaDTO> gerarRelatorioVendasPorCategoria(
    LocalDate dataInicio, LocalDate dataFim) {

    if (dataInicio.isAfter(dataFim)) {
      throw new IllegalArgumentException(
        "A data de início não pode ser posterior à data de fim."
      );
    }

    LocalDateTime inicio = dataInicio.atStartOfDay();
    LocalDateTime fimExclusivo = dataFim.plusDays(1).atStartOfDay();

    List<VendaCategoriaProjection> resultados =
      pedidoRepository.buscarVendasPorCategoria(inicio, fimExclusivo);

    return resultados.stream()
      .map(this::toRelatorioDTO)
      .sorted(Comparator.comparing(
        RelatorioVendaCategoriaDTO::totalFaturado).reversed())
      .toList();
  }

  private RelatorioVendaCategoriaDTO toRelatorioDTO(VendaCategoriaProjection p) {
    BigDecimal ticketMedio = p.getTotalFaturado()
      .divide(BigDecimal.valueOf(p.getTotalPedidos()), 2, RoundingMode.HALF_UP);

    return new RelatorioVendaCategoriaDTO(
      p.getNomeCategoria(),
      p.getTotalItensVendidos(),
      p.getTotalPedidos(),
      p.getTotalFaturado(),
      ticketMedio
    );
  }

  private PedidoModel findEntityById(int id) {
    return pedidoRepository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException(
      "Pedido com ID " + id + " não encontrado"
    ));
  }

  private PedidoResumoDTO toResumoDTO(PedidoModel p) {
    List<ItemPedidoDTO> itens = p.getItensPedido().stream()
      .map(i -> new ItemPedidoDTO(
        i.getProduto().getNome(),
        i.getQuantidade(),
        i.getPrecoUnitario(),
        i.getSubTotal()
      ))
      .toList();

    return new PedidoResumoDTO(
      p.getId(),
      p.getDataHora(),
      p.getStatus(),
      p.getValorTotal(),
      p.getEndereco().getEndereco(),
      p.getEndereco().getNumero(),
      p.getEndereco().getComplemento(),
      p.getEndereco().getBairro(),
      p.getEndereco().getCidade(),
      itens
    );
  }
}
