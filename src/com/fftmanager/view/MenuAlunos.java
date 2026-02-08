package com.fftmanager.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import com.fftmanager.model.Aluno;
import com.fftmanager.service.AlunoService;

public class MenuAlunos {

	private final Scanner scanner;
	private final AlunoService alunoService = new AlunoService();

	public MenuAlunos(Scanner scanner) {
		this.scanner = scanner;
	}

	public void executar() {
		int opcao;
		do {
			exibirMenu();
			opcao = capturarOpcao();
			processarOpcao(opcao);
		} while (opcao != 0);
	}

	private void exibirMenu() {
		System.out.println("\n--- GERENCIAR ALUNOS ---");
		System.out.println("1. Cadastrar aluno");
		System.out.println("2. Listar alunos");
		System.out.println("3. Editar aluno");
		System.out.println("4. Remover aluno");
		System.out.println("5. Buscar aluno");
		System.out.println("0. Voltar");
		System.out.print("Escolha: ");
	}

	private int capturarOpcao() {
		try {
			return Integer.parseInt(scanner.nextLine().trim());
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private void processarOpcao(int opcao) {
		try {
			switch (opcao) {
				case 1 -> cadastrarAluno();
				case 2 -> listarAlunos();
				case 3 -> editarAluno();
				case 4 -> removerAluno();
				case 5 -> buscarAluno();
				case 0 -> {}
				default -> System.out.println("❌ Opção inválida.");
			}
		} catch (Exception e) {
			System.out.println("❌ Erro: " + e.getMessage());
		}
	}

	private void cadastrarAluno() throws Exception {
		System.out.println("\n--- Cadastrar Aluno ---");
		Aluno aluno = lerDadosAluno(null);
		aluno = alunoService.cadastrar(aluno);
		System.out.println("✅ Aluno cadastrado com sucesso! ID: " + aluno.getId());
	}

	private void listarAlunos() throws Exception {
		List<Aluno> alunos = alunoService.listar();
		System.out.println("\n--- Lista de Alunos ---");
		if (alunos.isEmpty()) {
			System.out.println("Nenhum aluno cadastrado.");
		} else {
			for (Aluno a : alunos) {
				System.out.printf("  [%d] %s | CPF: %s | Tel: %s | Email: %s%n",
						a.getId(), a.getNome(), a.getCpf(), a.getTelefone(), a.getEmail());
			}
			System.out.println("Total: " + alunos.size() + " aluno(s)");
		}
	}

	private void editarAluno() throws Exception {
		System.out.print("\nID do aluno a editar: ");
		int id = Integer.parseInt(scanner.nextLine().trim());
		Aluno aluno = alunoService.buscarPorId(id);
		if (aluno == null) {
			System.out.println("❌ Aluno não encontrado.");
			return;
		}
		System.out.println("Aluno atual: " + aluno.getNome());
		Aluno dados = lerDadosAluno(aluno);
		aluno.setNome(dados.getNome());
		aluno.setCpf(dados.getCpf());
		aluno.setTelefone(dados.getTelefone());
		aluno.setEmail(dados.getEmail());
		aluno.setDataNascimento(dados.getDataNascimento());
		alunoService.atualizar(aluno);
		System.out.println("✅ Aluno atualizado com sucesso!");
	}

	private void removerAluno() throws Exception {
		System.out.print("\nID do aluno a remover: ");
		int id = Integer.parseInt(scanner.nextLine().trim());
		alunoService.deletar(id);
		System.out.println("✅ Aluno removido com sucesso!");
	}

	private void buscarAluno() throws Exception {
		System.out.print("\nCPF ou nome para buscar: ");
		String busca = scanner.nextLine().trim();
		if (busca.isEmpty()) {
			System.out.println("❌ Digite um CPF ou nome.");
			return;
		}
		Aluno aluno = null;
		if (busca.replaceAll("[^0-9]", "").length() >= 11) {
			aluno = alunoService.buscarPorCpf(busca);
		} else {
			List<Aluno> todos = alunoService.listar();
			aluno = todos.stream()
					.filter(a -> a.getNome() != null && a.getNome().toLowerCase().contains(busca.toLowerCase()))
					.findFirst()
					.orElse(null);
		}
		if (aluno == null) {
			System.out.println("❌ Aluno não encontrado.");
		} else {
			System.out.printf("  [%d] %s | CPF: %s | Tel: %s | Email: %s | Nasc: %s%n",
					aluno.getId(), aluno.getNome(), aluno.getCpf(), aluno.getTelefone(),
					aluno.getEmail(), aluno.getDataNascimento());
		}
	}

	private Aluno lerDadosAluno(Aluno atual) {
		System.out.print("Nome: ");
		String nome = scanner.nextLine().trim();
		System.out.print("CPF: ");
		String cpf = scanner.nextLine().trim();
		System.out.print("Telefone: ");
		String telefone = scanner.nextLine().trim();
		System.out.print("Email: ");
		String email = scanner.nextLine().trim();
		System.out.print("Data nascimento (dd/MM/yyyy): ");
		LocalDate dataNasc = null;
		try {
			String dataStr = scanner.nextLine().trim();
			if (!dataStr.isEmpty()) {
				dataNasc = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			}
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Data inválida. Use dd/MM/yyyy");
		}
		return new Aluno(nome, cpf, telefone, email.isEmpty() ? null : email, dataNasc);
	}
}
