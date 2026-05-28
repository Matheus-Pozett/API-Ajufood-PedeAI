package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.response.ClienteResponseDTO;
import br.com.ajufood.pedeai.service.ClienteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@WebMvcTest(ClienteController.class) // Testa apenas o ClienteController de forma isolada, sem carregar banco ou services reais
class ClienteControllerTest {

  @Autowired
  private MockMvc mockMvc; // Simula requisições HTTP (GET, POST, etc.) diretamente no Controller, sem subir um servidor real

  @MockitoBean // Cria um objeto dublê (Mock) do ClienteService para o Controller não quebrar
  private ClienteService clienteService;

  @Test
  @DisplayName("Deve buscar cliente por id com sucesso")
  void deveBuscarClientePorIdComSucesso() throws Exception {
    // Arrange
    int id = 1;
    String nome = "Matheus";
    String cpf = "07837132580";
    String email = "test@test.com";
    String telefone = "79988008198";

    ClienteResponseDTO clienteEncontrado = new ClienteResponseDTO(
      id,
      nome,
      cpf,
      email,
      telefone);

    Mockito.when(clienteService.findById(id)).thenReturn(clienteEncontrado);
    // Act and Assertion
    mockMvc.perform(get("/clientes/" + id).accept(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(id))
      .andExpect(jsonPath("$.nome").value(nome))
      .andExpect(jsonPath("$.cpf").value(cpf))
      .andExpect(jsonPath("$.email").value(email))
      .andExpect(jsonPath("$.telefone").value(telefone));

    Mockito.verify(clienteService).findById(id);
  }

  @Test
  void getAll() {
  }

  @Test
  void create() {
  }

  @Test
  void update() {
  }

  @Test
  void delete() {
  }
}