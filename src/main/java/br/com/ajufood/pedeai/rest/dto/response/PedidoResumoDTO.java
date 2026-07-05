package br.com.ajufood.pedeai.rest.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResumoDTO(
  int id,
  LocalDateTime dataHora,
  String status,
  BigDecimal valorTotal,
  String endereco,
  int numero,
  String complemento,
  String bairro,
  String cidade,
  List<ItemPedidoDTO> itens
) {}