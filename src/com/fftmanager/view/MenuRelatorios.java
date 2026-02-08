package com.fftmanager.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.fftmanager.dao.MatriculaDAO;
import com.fftmanager.dao.PagamentoDAO;
import com.fftmanager.model.Aluno;
import com.fftmanager.model.Matricula;
import com.fftmanager.model.Plano;
import com.fftmanager.service.AlunoService;
import com.fftmanager.service.PagamentoService;
import com.fftmanager.service.PagamentoService.InadimplenteInfo;
import com.fftmanager.service.PlanoService;

public class MenuRelatorios {

	private final Scanner scanner;
	private final AlunoService alunoService = new AlunoService();
	private final PlanoService planoService = new PlanoService();
	private final PagamentoService pagamentoService = new PagamentoService();
	private final MatriculaDAO matriculaDAO = new MatriculaDAO();
	private final PagamentoDAO pagamentoDAO = new PagamentoDAO();

	public MenuRelatorios(Scanner scanner) {
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
		System.out.println("\n--- RELATÓRIOS ---");
		System.out.println("1. Alunos ativos");
		System.out.println("2. Alunos inativos");
		System.out.println("3. Inadimplentes");
		System.out.println("4. Receita mensal");
		System.out.println("5. Planos mais populares");
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
				case 1 -> relatorioAlunosAtivos();
				case 2 -> relatorioAlunosInativos();
				case 3 -> relatorioInadimplentes();
				case 4 -> relatorioReceitaMensal();
				case 5 -> relatorioPlanosPopulares();
				case 0 -> {}
				default -> System.out.println("❌ Opção inválida.");
			}
		} catch (Exception e) {
			System.out.println("❌ Erro: " + e.getMessage());
		}
	}

	private void relatorioAlunosAtivos() throws Exception {
		List<Matricula> ativas = matriculaDAO.listarAtivas();
		List<Integer> alunoIds = ativas.stream()
				.map(Matricula::getAlunoId)
				.distinct()
				.collect(Collectors.toList());
		System.out.println("\n--- Alunos Ativos ---");
		if (alunoIds.isEmpty()) {
			System.out.println("Nenhum aluno ativo.");
		} else {
			for (Integer id : alunoIds) {
				Aluno a = alunoService.buscarPorId(id);
				if (a != null) {
					System.out.printf("  [%d] %s | CPF: %s | Tel: %s%n", a.getId(), a.getNome(), a.getCpf(), a.getTelefone());
				}
			}
			System.out.println("Total: " + alunoIds.size() + " aluno(s) ativo(s)");
		}
	}

	private void relatorioAlunosInativos() throws Exception {
		List<Matricula> ativas = matriculaDAO.listarAtivas();
		List<Integer> idsAtivos = ativas.stream().map(Matricula::getAlunoId).distinct().collect(Collectors.toList());
		List<Aluno> todos = alunoService.listar();
		List<Aluno> inativos = todos.stream()
				.filter(a -> !idsAtivos.contains(a.getId()))
				.collect(Collectors.toList());
		System.out.println("\n--- Alunos Inativos ---");
		if (inativos.isEmpty()) {
			System.out.println("Nenhum aluno inativo (todos têm matrícula ativa ou não há alunos).");
		} else {
			for (Aluno a : inativos) {
				System.out.printf("  [%d] %s | CPF: %s | Tel: %s%n", a.getId(), a.getNome(), a.getCpf(), a.getTelefone());
			}
			System.out.println("Total: " + inativos.size() + " aluno(s) inativo(s)");
		}
	}

	private void relatorioInadimplentes() throws Exception {
		List<InadimplenteInfo> relatorio = pagamentoService.relatorioInadimplencia();
		System.out.println("\n--- Relatório de Inadimplentes ---");
		if (relatorio.isEmpty()) {
			System.out.println("Nenhum inadimplente.");
		} else {
			for (InadimplenteInfo info : relatorio) {
				String atraso = info.diasAtraso > 0 ? " (" + info.diasAtraso + " dias atraso)" : "";
				System.out.printf("  %s | CPF: %s | R$ %.2f | Venc: %s%s%n",
						info.alunoNome, info.cpf, info.valor, info.dataVencimento, atraso);
			}
			System.out.println("Total: " + relatorio.size() + " inadimplente(s)");
		}
	}

	private void relatorioReceitaMensal() throws Exception {
		LocalDate hoje = LocalDate.now();
		List<com.fftmanager.model.Pagamento> pagos = pagamentoDAO.buscarPagosPorMes(hoje.getYear(), hoje.getMonthValue());
		BigDecimal total = BigDecimal.ZERO;
		for (com.fftmanager.model.Pagamento p : pagos) {
			if (p.getValor() != null) {
				total = total.add(p.getValor());
			}
		}
		System.out.println("\n--- Receita Mensal (" + hoje.getMonth() + "/" + hoje.getYear() + ") ---");
		System.out.println("Pagamentos recebidos: " + pagos.size());
		System.out.printf("Total: R$ %.2f%n", total);
	}

	private void relatorioPlanosPopulares() throws Exception {
		Map<Integer, Long> contagem = matriculaDAO.contarPorPlano();
		System.out.println("\n--- Planos Mais Populares ---");
		if (contagem.isEmpty()) {
			System.out.println("Nenhuma matrícula cadastrada.");
		} else {
			int pos = 1;
			for (Map.Entry<Integer, Long> entry : contagem.entrySet()) {
				Plano plano = planoService.buscarPorId(entry.getKey());
				String nome = plano != null ? plano.getNome() : "Plano #" + entry.getKey();
				System.out.printf("  %d. %s - %d matrícula(s)%n", pos++, nome, entry.getValue());
			}
		}
	}
}
