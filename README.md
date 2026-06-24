# Taskify API

API REST de gerenciamento de tarefas com autenticação JWT, desenvolvida com Java e Spring Boot.

## Objetivo

Sistema de lista de tarefas com autenticação, onde cada usuário gerencia exclusivamente suas próprias tarefas. Projeto focado em demonstrar boas práticas de desenvolvimento com Spring Boot.

## Funcionalidades

- Autenticação JWT - Registro e login com tokens stateless
- CRUD de Tarefas - Gerenciamento completo com status e prioridade
- Isolamento por Usuário - Cada usuário vê apenas suas tarefas
- Validações - Título obrigatório, data futura, status válido
- Auditoria - Campos createdAt e updatedAt automáticos
- Logs Estruturados - Monitoramento de operações e erros

## Tecnologias

- Java 21 + Spring Boot 3x
- Spring Security com JWT
- PostgreSQL + Flyway (migrations)
- Docker + Docker Compose
- JUnit 5 + Mockito (testes unitários)

## Modelagem

## Regras de Negócio

- Apenas o proprietário pode acessar, editar ou deletar suas tarefas
- Tentativa de acesso a tarefa de outro usuário - 403 Forbidden
- Título é obrigatório e deve ter no mínimo 3 caracteres
- Data de criação da tarefa não pode ser no passado
- Prioridade deve ser válida (BAIXA, MEDIA, ALTA)
