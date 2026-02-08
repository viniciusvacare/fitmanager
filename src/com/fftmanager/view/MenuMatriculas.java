package com.fftmanager.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import com.fftmanager.model.Aluno;
import com.fftmanager.model.Matricula;
import com.fftmanager.model.Plano;
import com.fftmanager.service.AlunoService;
import com.fftmanager.service.MatriculaService;
import com.fftmanager.service.PlanoService;

public class MenuMatriculas {

	private final Scanner scanner;
	private final MatriculaService matriculaService = new MatriculaService();
	private final AlunoService alunoService = new AlunoService();
	private final PlanoService planoService = new PlanoService();

	public MenuMatriculas(Scanner scanner) {
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
		System.out.println("\n--- GERENCIAR MATRÍCULAS ---");
		System.out.println("1. Nova matrícula");
		System.out.println("2. Listar matrículas");
		System.out.println("3. Inativar matrícula");
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
				case 1 -> novaMatricula();
				case 2 -> listarMatriculas();
				case 3 -> inativarMatricula();
				case 0 -> {}
				default -> System.out.println("❌ Opção inválida.");
			}
		} catch (Exception e) {
			System.out.println("❌ Erro: " + e.getMessage());
		}
	}

	private void novaMatricula() throws Exception {
		System.out.println("\n--- Nova Matrícula ---");
		listarAlunosResumido();
		System.out.print("ID do aluno: ");
		int alunoId = Integer.parseInt(scanner.nextLine().trim());
		listarPlanosResumido();
		System.out.print("ID do plano: ");
		int planoId = Integer.parseInt(scanner.nextLine().trim());
		System.out.print("Data início (dd/MM/yyyy): ");
		LocalDate dataInicio;
		try {
			dataInicio = LocalDate.parse(scanner.nextLine().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Data inválida. Use dd/MM/yyyy");
		}
		Matricula matricula = matriculaService.cadastrar(alunoId, planoId, dataInicio);
		System.out.println("✅ Matrícula criada com sucesso! ID: " + matricula.getId());
		System.out.println("   Pagamentos gerados automaticamente.");
	}

	private void listarMatriculas() throws Exception {
		List<Matricula> matriculas = matriculaService.listar();
		System.out.println("\n--- Lista de Matrículas ---");
		if (matriculas.isEmpty()) {
			System.out.println("Nenhuma matrícula cadastrada.");
		} else {
			for (Matricula m : matriculas) {
				Aluno aluno = alunoService.buscarPorId(m.getAlunoId());
				Plano plano = planoService.buscarPorId(m.getPlanoId());
				String alunoNome = aluno != null ? aluno.getNome() : "?";
				String planoNome = plano != null ? plano.getNome() : "?";
				String status = m.isAtivo() ? "ATIVA" : "INATIVA";
				System.out.printf("  [%d] %s | %s | %s a %s | %s%n",
						m.getId(), alunoNome, planoNome, m.getDataInicio(), m.getDataFim(), status);
			}
			System.out.println("Total: " + matriculas.size() + " matrícula(s)");
		}
	}

	private void inativarMatricula() throws Exception {
		System.out.print("\nID da matrícula a inativar: ");
		int id = Integer.parseInt(scanner.nextLine().trim());
		matriculaService.inativar(id);
		System.out.println("✅ Matrícula inativada com sucesso!");
	}

	private void listarAlunosResumido() throws Exception {
		List<Aluno> alunos = alunoService.listar();
		System.out.println("Alunos cadastrados:");
		for (Aluno a : alunos) {
			System.out.printf("  [%d] %s%n", a.getId(), a.getNome());
		}
	}

	private void listarPlanosResumido() throws Exception {
		List<Plano> planos = planoService.listar();
		System.out.println("Planos disponíveis:");
		for (Plano p : planos) {
			System.out.printf("  [%d] %s - R$ %.2f/mês%n", p.getId(), p.getNome(), p.getValor());
		}
	}
}
