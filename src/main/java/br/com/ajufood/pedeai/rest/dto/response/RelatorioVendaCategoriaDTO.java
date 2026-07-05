package br.com.ajufood.pedeai.rest.dto.response;

import java.math.BigDecimal;

public record RelatorioVendaCategoriaDTO(
  String nomeCategoria,
  Long totalItensVendidos,
  Long totalPedidos,
  BigDecimal totalFaturado,
  BigDecimal ticketMedio
) {}
