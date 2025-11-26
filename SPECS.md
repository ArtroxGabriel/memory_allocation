# Especificações Técnicas: Simulador de Algoritmos de Substituição de Páginas

## 1. Visão Geral do Projeto

O objetivo deste projeto é desenvolver uma ferramenta de linha de comando para simular o comportamento de algoritmos clássicos de substituição de páginas utilizados no gerenciamento de memória de Sistemas Operacionais. O simulador processará uma cadeia de referências (trace) e calculará métricas de desempenho com base em um número especificado de molduras (frames) de memória física.

## 2. Algoritmos Suportados

A aplicação deve implementar os seguintes algoritmos de substituição de páginas:

1. FIFO (First-In, First-Out)
2. LRU (Least Recently Used)
3. OPT (Ótimo)Segunda Chance
4. Clock (Relógio)
5. NRU (Not Recently Used)
6. LFU (Least Frequently Used)
7. MFU (Most Frequently Used)

## 3. Especificações da Interface

### 3.1 Interface de Linha de Comando (CLI)

O programa deve ser executável via CLI utilizando a seguinte assinatura:

```bash
./pager --algo <ALGO> --frames <N> --trace <arquivo> [--verbose]
```

**Parâmetros:**

- --algo: O algoritmo a ser executado (ex: lru, fifo, opt).
- --frames: Inteiro representando o número de molduras de memória física disponíveis.
- --trace: Caminho para o arquivo de entrada contendo as referências de página.
- --verbose: (Opcional) Flag para habilitar saída detalhada.

### 3.2 Formato dos Dados de Entrada (.trace)

- O arquivo de entrada deve ser um arquivo de texto.
- Formato: Um número inteiro (referência de página) por linha.
- Nome do Arquivo de Exemplo: silberschatz2001.traceConteúdo de Entrada Exemplo:7

  ```bash
  0
  1
  2
  ...

  ```

### 3.3 Formato de Saída

O simulador deve enviar as seguintes estatísticas para a saída padrão (stdout):

```sh
Algoritmo: <NOME_ALGORITMO>

Frames: <NUMERO_DE_FRAMES>
Referências: <TOTAL_REFERENCIAS>
Faltas de página: <TOTAL_FALTAS>
Taxa de faltas: <PORCENTAGEM_TAXA_FALTAS>
Evicções: <TOTAL_EVICCOES>

Conjunto residente final:
frame_ids: <ID_1> <ID_2> ... <ID_N>
page_ids: <PAGINA_1> <PAGINA_2> ... <PAGINA_N>

```

## 4. Dados de Referência e Validação

O sistema será validado utilizando a sequência de referência fornecida por Silberschatz et al. (2001).
**Sequência de Referência:** `7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1` (20 referências, 6 páginas distintas).

### 4.1 Expectativas de Benchmark

A implementação deve ser testada contra os seguintes benchmarks:

| Frames | Faults FIFO | Faults LRU | Faults Ótimo |
| ------ | ----------- | ---------- | ------------ |
| 3      | 15          | 12         | 9            |
| 4      | 10          | 8          | 8            |

## 5. Requisitos de Implementação

- Linguagem: C, C++, Java, JavaScript ou Python.
- Documentação: Deve ser fornecido um documento explicando as decisões de implementação, especificamente:
  - Estruturas de dados utilizadas (Mapas, Listas Encadeadas, etc.).
  - Padrões de projeto aplicados (se houver).Estruturas de controle (mapeamento de IDs para tamanho/status).
  - Qualidade de Código: O código deve estar devidamente comentado.

## 6. Entregáveis

- Código fonte.
- Arquivo silberschatz2001.trace.
- Documentação de implementação.
