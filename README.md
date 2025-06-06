# 🚂 Simulador de Trem - Sistema de Transporte Urbano

> **Disciplina:** Programação II  
> **Projeto:** Desafio 01 - Simulador de Trem  
> **Paradigma:** Programação Orientada a Objetos com Estruturas Encadeadas  

Este projeto implementa um simulador completo de sistema ferroviário urbano, modelando o transporte entre diferentes bairros de uma cidade do interior através de uma linha única de trem bidirecional.

## 📋 Índice

- [Visão Geral](#-visão-geral)
- [Características do Sistema](#-características-do-sistema)
- [Arquitetura](#-arquitetura)
- [Funcionalidades](#-funcionalidades)
- [Algoritmos Implementados](#-algoritmos-implementados)
- [Como Executar](#-como-executar)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Relatórios Gerados](#-relatórios-gerados)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)

## 🔍 Visão Geral

O simulador modela um sistema ferroviário com as seguintes características:

### 🚉 **Trajeto**
- **Origem**: Ponto A ↔️ **Destino**: Ponto B
- **Estações**: N estações intermediárias (N entre 10 e 30)
- **Distância**: 20 km entre estações consecutivas
- **Trilho único**: Linha bidirecional com sistema de desvios

### ⏰ **Operação**
- **Horário**: 08h00 às 17h30 (último trem)
- **Frequência**: Trens a cada 30 minutos
- **Velocidade**: 60 km/h constante
- **Simulação**: Tempo real por minutos

## 🎯 Características do Sistema

### 🛤️ **Infraestrutura**
```
Ponto A ←→ [Estação 1] ←→ [Estação 2] ←→ ... ←→ [Estação N] ←→ Ponto B
```

**Componentes:**
- **Trilho principal**: Estrutura encadeada com 20 nós por segmento
- **Desvios**: Nós especiais 1 km antes de cada estação
- **Estações**: Pontos de parada com embarque/desembarque

### 👥 **Sistema de Passageiros**
- **Capacidade**: 10-50 passageiros por trem (aleatório)
- **Embarque/Desembarque**: Máximo 10 pessoas por estação
- **Tempo de movimento**: 30 segundos por pessoa
- **Regra**: Total de movimentação deve ser par

### 🚦 **Controle de Tráfego**
- **Desvios automáticos**: Prevenção de colisões
- **Sincronização**: Controle de entrada/saída nos terminais
- **Parada mínima**: 1 minuto quando não há movimentação

## 🏗️ Arquitetura

### Classes Principais

#### `Trem`
```java
public class Trem {
    private int id;
    private int passageiros;
    private String direcao; // A->B ou B->A
    private LocalTime horarioPartida;
    private EstadoTrem estado;
}
```

#### `Estacao`
```java
public class Estacao {
    private int numero;
    private String nome;
    private int pessoasEmbarcaram;
    private int pessoasDesembarcaram;
    private NoTrilho desvioAntes;
    private NoTrilho desvioDepois;
}
```

#### `NoTrilho`
```java
public class NoTrilho {
    private int quilometro;
    private TipoNo tipo; // TRILHO, ESTACAO, DESVIO
    private Trem tremOcupando;
    private NoTrilho proximo;
    private NoTrilho anterior;
}
```

#### `SimuladorTrem`
```java
public class SimuladorTrem {
    private ListaEncadeada<Estacao> estacoes;
    private ListaEncadeada<Trem> trensAtivos;
    private LocalTime tempoAtual;
    private int totalEstacoes;
}
```

## ⚡ Funcionalidades

### 🎮 **Simulação Interativa**
- **Controle manual**: Avanço por tecla pressionada
- **Visualização**: Representação gráfica ASCII do sistema
- **Tempo real**: Simulação minuto a minuto
- **Status completo**: Posição de todos os trens

### 🎲 **Geração Aleatória**
- **Número de estações**: 10-30 estações
- **Passageiros por trem**: 10-50 pessoas
- **Movimentação**: Embarque/desembarque aleatório por estação

### 🛡️ **Sistema de Segurança**
- **Detecção de colisão**: Algoritmo de prevenção
- **Desvios automáticos**: Uso de trilhos alternativos
- **Sincronização de terminais**: Controle de acesso

### 📊 **Relatórios e Estatísticas**
- **Log de movimentação**: Registro por estação
- **Arquivo de saída**: Relatório final em TXT
- **Estatísticas gerais**: Total de passageiros transportados

## 🔧 Algoritmos Implementados

### 1. **Controle de Movimento**
```java
public void moverTrem(Trem trem) {
    // Verifica colisões potenciais
    // Calcula próxima posição
    // Aplica desvios se necessário
    // Atualiza estado do trem
}
```

### 2. **Detecção de Colisão**
```java
private boolean verificarColisao(NoTrilho destino, String direcao) {
    // Analisa ocupação do trilho
    // Verifica trens em sentido oposto
    // Retorna necessidade de desvio
}
```

### 3. **Gerenciamento de Desvios**
```java
private void aplicarDesvio(Trem trem, Estacao estacao) {
    // Move trem para desvio
    // Aguarda liberação do trilho
    // Retorna ao trilho principal
}
```

### 4. **Processamento de Estação**
```java
private void processarParadaEstacao(Trem trem, Estacao estacao) {
    // Calcula embarque/desembarque
    // Aplica tempo de parada
    // Atualiza estatísticas
}
```

## 🚀 Como Executar

### Pré-requisitos
- Java 11 ou superior
- IDE Java (Eclipse, IntelliJ, VS Code)
- Console para visualização da simulação

### Compilação e Execução
```bash
# Compilar o projeto
javac -d bin src/**/*.java

# Executar simulação
java -cp bin SimuladorTrem

# Ou executar diretamente
java SimuladorTrem
```

### Controles da Simulação
```
[ENTER] - Avançar 1 minuto
[Q] - Sair da simulação
[R] - Relatório atual
[P] - Pausar/Continuar
```

## 📁 Estrutura do Projeto

```
simulador-trem/
├── src/
│   ├── modelo/
│   │   ├── Trem.java
│   │   ├── Estacao.java
│   │   ├── NoTrilho.java
│   │   └── TipoNo.java
│   ├── estruturas/
│   │   ├── ListaEncadeada.java
│   │   └── FilaTrens.java
│   ├── simulacao/
│   │   ├── SimuladorTrem.java
│   │   ├── ControleMovimento.java
│   │   └── GeradorAleatorio.java
│   ├── visualizacao/
│   │   ├── DisplayConsole.java
│   │   └── RelatorioTexto.java
│   └── Main.java
├── recursos/
│   └── configuracao.properties
├── saida/
│   └── relatorio_final.txt
├── docs/
│   └── Desafio01.pdf
└── README.md
```

## 📊 Relatórios Gerados

### Arquivo de Saída: `relatorio_final.txt`
```
========== RELATÓRIO FINAL - SIMULADOR DE TREM ==========
Período: 08:00 - 17:30
Total de Estações: 15
Total de Trens Operados: 38

ESTATÍSTICAS POR ESTAÇÃO:
Estação 01 - Centro
  - Embarques: 245 pessoas
  - Desembarques: 198 pessoas
  - Movimento Total: 443 pessoas

Estação 02 - Vila Nova
  - Embarques: 189 pessoas
  - Desembarques: 234 pessoas
  - Movimento Total: 423 pessoas

[... continua para todas as estações ...]

RESUMO GERAL:
- Total de Passageiros Transportados: 1.247
- Média de Passageiros por Trem: 32.8
- Estação Mais Movimentada: Centro (443 pessoas)
- Tempo Total de Simulação: 9h30min
```

## 🖥️ Visualização da Simulação

### Display ASCII
```
════════════════ SIMULADOR DE TREM ════════════════
Tempo: 14:23 | Trens Ativos: 3

Ponto A ←→ Est.1 ←→ Est.2 ←→ Est.3 ←→ ... ←→ Ponto B
   [T1]    🚂       [T2]                        

Trem T1: A→B | Pos: Estação 1 | Pass: 35 | Parado (2min)
Trem T2: B→A | Pos: Km 45     | Pass: 28 | Movimento
Trem T3: A→B | Pos: Desvio 3  | Pass: 41 | Aguardando

[ENTER] Próximo minuto | [Q] Sair | [R] Relatório
```

## 🛠️ Tecnologias Utilizadas

- **Linguagem**: Java 11+
- **Paradigma**: Programação Orientada a Objetos
- **Estruturas de Dados**: Listas Encadeadas, Filas
- **Algoritmos**: Busca, Ordenação, Controle de Estado
- **I/O**: Manipulação de arquivos, Console interativo
- **Geração Aleatória**: Random para simulação realística

## 🧮 Conceitos Implementados

### Estruturas de Dados
- **Lista Encadeada**: Representação do trilho
- **Fila de Trens**: Controle de saída/chegada
- **Árvore de Estados**: Controle de tráfego

### Algoritmos
- **Simulação Discreta**: Avanço temporal controlado
- **Detecção de Colisão**: Prevenção de acidentes
- **Pathfinding**: Roteamento com desvios
- **Geração Procedural**: Criação aleatória de cenários

### Padrões de Projeto
- **Observer**: Monitoramento de eventos
- **Strategy**: Diferentes estratégias de movimento
- **State**: Controle de estados dos trens
- **Singleton**: Controle único da simulação

## 🎯 Objetivos de Aprendizado

Este projeto consolida conhecimentos em:
- **Estruturas de dados encadeadas complexas**
- **Simulação de sistemas em tempo real**
- **Algoritmos de controle de tráfego**
- **Programação orientada a objetos avançada**
- **Geração de relatórios e estatísticas**
- **Interface de usuário em console**

## 🚧 Desafios Técnicos

### Principais Complexidades
1. **Sincronização**: Coordenação de múltiplos trens
2. **Detecção de Colisão**: Prevenção em tempo real
3. **Gerenciamento de Estado**: Controle complexo de estados
4. **Otimização**: Performance com muitos objetos
5. **Visualização**: Representação clara do sistema

---

**Desenvolvido como parte do currículo de Programação II**

*Um projeto que simula a complexidade real de sistemas de transporte urbano!* 🚆
