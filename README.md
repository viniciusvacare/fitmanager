# 🏋️ FitManager

Sistema de gerenciamento de academia desenvolvido em Java com interface CLI.

## 📋 Funcionalidades

- **Gerenciamento de Alunos**: Cadastro, edição, remoção e consulta
- **Gerenciamento de Planos**: Criação e manutenção de planos de academia
- **Controle de Matrículas**: Registro de matrículas e controle de status
- **Gestão de Pagamentos**: Controle de mensalidades e pagamentos
- **Relatórios**: Consulta de alunos ativos, inadimplentes e histórico

## 🚀 Como Usar

### Pré-requisitos

- Java 11 ou superior
- MySQL 8.0 ou superior
- Maven (opcional)

### Configuração do Banco de Dados

1. Crie um banco de dados no MySQL:
```sql
CREATE DATABASE academia;
```

2. Execute o script SQL localizado em `resources/schema.sql`

3. Configure as credenciais em `DatabaseConnection.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/academia";
private static final String USER = "seu_usuario";
private static final String PASSWORD = "sua_senha";
```

### Executando o Projeto
```bash
# Compilar
javac -d bin src/**/*.java

# Executar
java -cp bin Main
```

## 📦 Tecnologias

- Java
- MySQL
- JDBC
- Padrão DAO