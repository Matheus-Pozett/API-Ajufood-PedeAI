package br.com.ajufood.pedeai.rest.dto.response;

import java.math.BigDecimal;

public record ItemPedidoDTO(
  String nomeProduto,
  int quantidade,
  BigDecimal precoUnitario,
  BigDecimal subTotal
) {}