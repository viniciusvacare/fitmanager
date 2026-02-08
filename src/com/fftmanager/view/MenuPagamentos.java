package com.fftmanager.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import com.fftmanager.dao.PagamentoDAO;
import com.fftmanager.model.Aluno;
import com.fftmanager.model.Matricula;
import com.fftmanager.model.Pagamento;
import com.fftmanager.service.AlunoService;
import com.fftmanager.service.MatriculaService;
import com.fftmanager.service.PagamentoService;

public class MenuPagamentos {

	private final Scanner scanner;
	private final PagamentoService pagamentoService = new PagamentoService();
	private final PagamentoDAO pagamentoDAO = new PagamentoDAO();
	private final AlunoService alunoService = new AlunoService();
	private final MatriculaService matriculaService = new MatriculaService();

	public MenuPagamentos(Scanner scanner) {
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
		System.out.println("\n--- GERENCIAR PAGAMENTOS ---");
		System.out.println("1. Registrar pagamento");
		System.out.println("2. Ver pendentes");
		System.out.println("3. Ver histórico");
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
				case 1 -> registrarPagamento();
				case 2 -> verPendentes();
				case 3 -> verHistorico();
				case 0 -> {}
				default -> System.out.println("❌ Opção inválida.");
			}
		} catch (Exception e) {
			System.out.println("❌ Erro: " + e.getMessage());
		}
	}

	private void registrarPagamento() throws Exception {
		System.out.println("\n--- Registrar Pagamento ---");
		List<Pagamento> pendentes = pagamentoService.listarPendentes();
		if (pendentes.isEmpty()) {
			System.out.println("Nenhum pagamento pendente.");
			return;
		}
		System.out.println("Pagamentos pendentes:");
		for (Pagamento p : pendentes) {
			Matricula mat = matriculaService.buscarPorId(p.getMatriculaId());
			Aluno aluno = mat != null ? alunoService.buscarPorId(mat.getAlunoId()) : null;
			String nome = aluno != null ? aluno.getNome() : "?";
			System.out.printf("  [%d] %s | R$ %.2f | Venc: %s%n",
					p.getId(), nome, p.getValor(), p.getDataVencimento());
		}
		System.out.print("ID do pagamento a registrar: ");
		int id = Integer.parseInt(scanner.nextLine().trim());
		System.out.print("Data do pagamento (dd/MM/yyyy ou Enter para hoje): ");
		String dataStr = scanner.nextLine().trim();
		LocalDate dataPag;
		if (dataStr.isEmpty()) {
			dataPag = LocalDate.now();
		} else {
			try {
				dataPag = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			} catch (DateTimeParseException e) {
				throw new IllegalArgumentException("Data inválida. Use dd/MM/yyyy");
			}
		}
		pagamentoService.registrarPagamento(id, dataPag);
		System.out.println("✅ Pagamento registrado com sucesso!");
	}

	private void verPendentes() throws Exception {
		List<Pagamento> pendentes = pagamentoService.listarPendentes();
		System.out.println("\n--- Pagamentos Pendentes ---");
		if (pendentes.isEmpty()) {
			System.out.println("Nenhum pagamento pendente.");
		} else {
			for (Pagamento p : pendentes) {
				Matricula mat = matriculaService.buscarPorId(p.getMatriculaId());
				Aluno aluno = mat != null ? alunoService.buscarPorId(mat.getAlunoId()) : null;
				String nome = aluno != null ? aluno.getNome() : "?";
				String status = "ATRASADO".equals(p.getStatus()) ? " (ATRASADO)" : "";
				System.out.printf("  [%d] %s | R$ %.2f | Venc: %s%s%n",
						p.getId(), nome, p.getValor(), p.getDataVencimento(), status);
			}
			System.out.println("Total: " + pendentes.size() + " pendente(s)");
		}
	}

	private void verHistorico() throws Exception {
		List<Pagamento> historico = pagamentoDAO.listarTodos();
		System.out.println("\n--- Histórico de Pagamentos ---");
		for (Pagamento p : historico) {
			Matricula mat = matriculaService.buscarPorId(p.getMatriculaId());
			Aluno aluno = mat != null ? alunoService.buscarPorId(mat.getAlunoId()) : null;
			String nome = aluno != null ? aluno.getNome() : "?";
			String dataPag = p.getDataPagamento() != null ? p.getDataPagamento().toString() : "-";
			System.out.printf("  [%d] %s | R$ %.2f | Venc: %s | Pago: %s | %s%n",
					p.getId(), nome, p.getValor(), p.getDataVencimento(), dataPag, p.getStatus());
		}
		if (historico.isEmpty()) {
			System.out.println("Nenhum pagamento registrado.");
		} else {
			System.out.println("Total: " + historico.size() + " pagamento(s)");
		}
	}
}
