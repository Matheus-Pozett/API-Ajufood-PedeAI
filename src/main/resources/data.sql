INSERT INTO categoria_produto (nome, descricao) VALUES
    ('Lanches', 'Hambúrgueres artesanais, sanduíches e salgados deliciosos'),
    ('Bebidas', 'Sucos naturais, refrigerantes, águas e cervejas artesanais'),
    ('Sobremesas', 'Doces, bolos, tortas e sorvetes variados para adoçar o dia'),
    ('Porções', 'Porções completas de batata frita, anéis de cebola e frango a passarinho'),
    ('Combos', 'Combos promocionais de lanche, bebida e sobremesa');

INSERT INTO forma_pagamento (nome, descricao) VALUES
    ('Pix', 'Pagamento instantâneo via chave Pix ou QR Code direto no aplicativo'),
    ('Cartão de Crédito', 'Pagamento online via cartão de crédito das principais bandeiras'),
    ('Cartão de Débito', 'Pagamento via maquininha com o entregador no momento da entrega'),
    ('Dinheiro', 'Pagamento em espécie (lembre-se de informar se precisa de troco)'),
    ('Vale Refeição', 'Aceitamos os principais vales: Alelo, Ticket, Sodexo e VR');

INSERT INTO produto (nome, descricao, preco, disponivel, categoria_produto_id) VALUES
    ('X-Burguer Artesanal', 'Pão brioche, blend bovino de 160g e queijo prato derretido', 24.90, true, 1),
    ('Coca-Cola 2L', 'Refrigerante Coca-Cola garrafa pet 2 litros gelada', 12.50, true, 2),
    ('Pudim de Leite Condensado', 'Fatia deliciosa de pudim caseiro sem furinhos', 14.00, true, 3),
    ('Batata Frita Grande', 'Porção de 500g de batata frita rústica com maionese da casa', 32.90, true, 4),
    ('Combo Família', '4 X-Burguers Artesanais + 1 Porção de Batata Média + 1 Coca-Cola 2L', 115.00, true, 5);

INSERT INTO cliente (nome, cpf, email, telefone) VALUES
    ('João Silva', '08543781079', 'joao.silva@email.com', '79999998888'),
    ('Maria Oliveira', '31826042055', 'maria.oliveira@email.com', '79988887777'),
    ('Carlos Santos', '74152869062', 'carlos.santos@email.com', '79977776666');

INSERT INTO endereco (endereco, numero, complemento, bairro, cidade, estado, cep, cliente_id) VALUES
    ('Avenida Beira Mar', 100, 'Apartamento 101', 'Treze de Julho', 'Aracaju', 'SE', '49020010', 1),
    ('Rua Laranjeiras', 500, 'Casa de esquina', 'Centro', 'Aracaju', 'SE', '49010000', 2),
    ('Avenida Hermes Fontes', 1500, 'Sala comercial 5', 'Salgado Filho', 'Aracaju', 'SE', '49020550', 3);

INSERT INTO pedido (data_hora, status, valor_total, cliente_id, endereco_entrega_id) VALUES
    ('2023-11-10 19:20:00', 'ENTREGUE', 24.90, 1, 1),
    ('2023-11-11 20:00:00', 'ENTREGUE', 127.50, 2, 2),
    (CURRENT_TIMESTAMP, 'PREPARANDO', 46.90, 3, 3);

INSERT INTO itens_pedido (quantidade, preco_unitario, sub_total, pedido_id, produto_id) VALUES
-- Itens do Pedido 1 (Total: R$ 24.90)
(1, 24.90, 24.90, 1, 1),   -- 1 X-Burguer Artesanal

-- Itens do Pedido 2 (Total: R$ 127.50)
(1, 115.00, 115.00, 2, 5), -- 1 Combo Família
(1, 12.50, 12.50, 2, 2),   -- 1 Coca-Cola 2L

-- Itens do Pedido 3 (Total: R$ 46.90)
(1, 32.90, 32.90, 3, 4),   -- 1 Batata Frita Grande
(1, 14.00, 14.00, 3, 3);

INSERT INTO pagamento (valor_pago, data_hora, pedido_id, forma_pagamento_id) VALUES
                                                                                 (24.90, '2023-11-10 19:30:00', 1, 1),
                                                                                 (127.50, '2023-11-11 20:15:30', 2, 2),
                                                                                 (46.90, CURRENT_TIMESTAMP, 3, 4);