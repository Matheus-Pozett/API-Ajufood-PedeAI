package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.ClienteRequestDTO;
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
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
  @DisplayName("Deve buscar todos os clientes")
  void deveBuscarTodosClientesComSucesso() throws Exception {
    int id = 1;
    String nome = "Matheus";
    String cpf = "00000000000";
    String email = "test@test.com";
    String telefone = "79988008198";

    ClienteResponseDTO cliente1 = new ClienteResponseDTO(
      id,
      nome,
      cpf,
      email,
      telefone);

    ClienteResponseDTO cliente2 = new ClienteResponseDTO(
      2,
      "Pozett",
      cpf,
      email,
      telefone);

    Mockito.when(clienteService.findAll()).thenReturn(List.of(cliente1, cliente2));

    mockMvc.perform(get("/clientes").accept(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()").value(2))
      .andExpect(jsonPath("$[0].id").value(1))
      .andExpect(jsonPath("$[0].nome").value("Matheus"))
      .andExpect(jsonPath("$[1].id").value(2))
      .andExpect(jsonPath("$[1].nome").value("Pozett"));

    Mockito.verify(clienteService).findAll();
  }

  @Test
  @DisplayName("Deve retornar o cliente salvo")
  void deveSalvarClienteComSucesso() throws Exception {
    ClienteRequestDTO clienteRequestDTO = new ClienteRequestDTO(
      "Matheus",
      "07837132580",
      "matheus@test.com",
      "79988009898"
    );

    ClienteResponseDTO clienteSalvo = new ClienteResponseDTO(
      1,
      "Matheus",
      "07837132580",
      "matheus@test.com",
      "79988009898"
    );

    Mockito.when(clienteService.save(clienteRequestDTO)).thenReturn(clienteSalvo);

    mockMvc.perform(post("/clientes")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
          {
            "nome":"Matheus",
            "cpf":"07837132580",
            "email":"matheus@test.com",
            "telefone":"79988009898"
          }
        """)
        .accept(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").value(1))
      .andExpect(jsonPath("$.nome").value("Matheus"))
      .andExpect(jsonPath("$.cpf").value("07837132580"))
      .andExpect(jsonPath("$.email").value("matheus@test.com"))
      .andExpect(jsonPath("$.telefone").value("79988009898"));

    Mockito.verify(clienteService).save(clienteRequestDTO);
  }

  @Test
  void update() {
  }

  @Test
  void delete() {
  }
}