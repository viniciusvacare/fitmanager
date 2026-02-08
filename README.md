# 🏋️ FitManager

## Descrição do Projeto

O **FitManager** é um sistema de gerenciamento de academia desenvolvido em Java com interface **CLI (linha de comando)**. O projeto implementa uma arquitetura em camadas (Model, DAO, Service, View) e permite o controle completo de alunos, planos, matrículas e pagamentos.

### Funcionalidades

| Módulo | Recursos |
|--------|----------|
| **Alunos** | Cadastro, edição, remoção, listagem e busca por CPF/nome |
| **Planos** | Criação, edição, remoção e listagem de planos |
| **Matrículas** | Nova matrícula (com geração automática de pagamentos), listagem e inativação |
| **Pagamentos** | Registrar pagamento, ver pendentes, histórico (com multa por atraso) |
| **Relatórios** | Alunos ativos/inativos, inadimplentes, receita mensal, planos mais populares |

### Validações de Negócio

- CPF único por aluno
- E-mail válido
- Idade mínima de 14 anos
- Apenas uma matrícula ativa por aluno
- Juros de 0,1% ao dia em pagamentos atrasados

---

## Instruções de Instalação

### Pré-requisitos

- **Java 11** ou superior (projeto configurado para Java 21)
- **MySQL 8.0** ou superior
- **Eclipse** (recomendado) ou outra IDE compatível

### 1. Clonar ou baixar o projeto

```bash
git clone <url-do-repositorio>
cd fitmanager
```

### 2. Criar o banco de dados no MySQL

```sql
CREATE DATABASE fitmanager;
USE fitmanager;
```

### 3. Executar o script do schema

Execute o conteúdo do arquivo `resources/schema.sql` no MySQL (via DBeaver, MySQL Workbench ou linha de comando):

```bash
mysql -u root -p fitmanager < resources/schema.sql
```

### 4. Configurar as credenciais

1. Copie o arquivo de exemplo:
   ```bash
   cp database.properties.example database.properties
   ```

2. Edite `database.properties` na raiz do projeto:
   ```properties
   db.url=jdbc:mysql://localhost:3306/fitmanager
   db.user=seu_usuario
   db.password=sua_senha
   ```

### 5. Configurar o driver MySQL (Eclipse)

O projeto já inclui o driver em `src/lib/mysql-connector-j-9.6.0.jar`. Se estiver usando Eclipse, o `.classpath` já está configurado. Para compilar manualmente, inclua o JAR no classpath.

---

## Como Usar

### Via Eclipse

1. Importe o projeto como **Existing Projects into Workspace**
2. Aguarde o build automático
3. Clique com o botão direito em `Main.java` → **Run As** → **Java Application**
4. O menu principal será exibido no console

### Via linha de comando

```bash
# Compilar (na raiz do projeto)
javac -d bin -cp "src/lib/mysql-connector-j-9.6.0.jar" src/com/fftmanager/database/*.java src/com/fftmanager/model/*.java src/com/fftmanager/dao/*.java src/com/fftmanager/service/*.java src/com/fftmanager/view/*.java src/com/fftmanager/Main.java

# Executar (certifique-se de estar na raiz do projeto)
java -cp "bin;src/lib/mysql-connector-j-9.6.0.jar" com.fftmanager.Main
```

### Navegação no menu

| Opção | Ação |
|-------|------|
| 1 | Gerenciar Alunos → Cadastrar, listar, editar, remover, buscar |
| 2 | Gerenciar Planos → Cadastrar, listar, editar, remover |
| 3 | Gerenciar Matrículas → Nova matrícula, listar, inativar |
| 4 | Gerenciar Pagamentos → Registrar, pendentes, histórico |
| 5 | Relatórios → Ativos, inativos, inadimplentes, receita, planos populares |
| 0 | Sair |

**Datas:** Use o formato `dd/MM/yyyy` (ex: 15/03/2025).

---

## Tecnologias Utilizadas

| Tecnologia | Uso |
|------------|-----|
| **Java 21** | Linguagem principal |
| **MySQL** | Banco de dados relacional |
| **JDBC** | Conexão e acesso ao banco |
| **MySQL Connector/J 9.6** | Driver JDBC para MySQL |

### Arquitetura do Projeto

```
src/com/fftmanager/
├── database/     # Conexão com o banco
├── model/        # Entidades (Aluno, Plano, Matricula, Pagamento)
├── dao/          # Acesso a dados (Data Access Object)
├── service/      # Regras de negócio
├── view/         # Interface CLI (menus)
└── Main.java     # Ponto de entrada
```

### Padrões utilizados

- **DAO** – Separação entre lógica de negócio e acesso a dados
- **Camadas** – Model → DAO → Service → View
- **Properties** – Configuração externa para credenciais do banco
