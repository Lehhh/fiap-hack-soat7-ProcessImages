# Processamento de Vídeo

## Visão Geral

Este projeto é um serviço de processamento de vídeo desenvolvido em Java utilizando o framework Spring Boot. Ele processa vídeos extraindo frames e salvando-os como imagens. O projeto utiliza Redis para gerenciamento de filas e status de processamento.

## Arquitetura

A arquitetura do projeto é composta pelos seguintes componentes principais:

- **Spring Boot**: Framework principal para construção do serviço.
- **Redis**: Utilizado para gerenciamento de filas e status de processamento.
- **FFmpeg**: Utilizado para manipulação e extração de frames de vídeos.
- **JavaCV**: Biblioteca Java para manipulação de vídeos e imagens.
- **Maven**: Ferramenta de build e gerenciamento de dependências.

### Estrutura do Projeto

- `src/main/java/br/com/fiap/soat7/application/service/ProcessVideoService.java`: Serviço principal que processa a fila de vídeos.
- `src/main/java/br/com/fiap/soat7/infrastructure/config/VideoProcessing.java`: Classe responsável por extrair frames dos vídeos.
- `src/main/resources/application.yml`: Arquivo de configuração do Spring Boot.
- `Dockerfile`: Arquivo de configuração para criação da imagem Docker do projeto.

## Variáveis de Ambiente

O projeto utiliza as seguintes variáveis de ambiente:

- `REDIS_MID_URL`: URL do servidor Redis.
- `SHARED_DISK`: Caminho do diretório compartilhado para upload de vídeos.

## Como Executar

### Pré-requisitos

- Docker
- Docker Compose

### Passos

1. Clone o repositório:
   ```sh
   git clone <URL_DO_REPOSITORIO>
   cd <NOME_DO_REPOSITORIO>