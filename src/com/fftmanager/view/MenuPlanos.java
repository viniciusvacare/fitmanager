package com.fftmanager.view;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import com.fftmanager.model.Plano;
import com.fftmanager.service.PlanoService;

public class MenuPlanos {

	private final Scanner scanner;
	private final PlanoService planoService = new PlanoService();

	public MenuPlanos(Scanner scanner) {
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
		System.out.println("\n--- GERENCIAR PLANOS ---");
		System.out.println("1. Cadastrar plano");
		System.out.println("2. Listar planos");
		System.out.println("3. Editar plano");
		System.out.println("4. Remover plano");
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
				case 1 -> cadastrarPlano();
				case 2 -> listarPlanos();
				case 3 -> editarPlano();
				case 4 -> removerPlano();
				case 0 -> {}
				default -> System.out.println("❌ Opção inválida.");
			}
		} catch (Exception e) {
			System.out.println("❌ Erro: " + e.getMessage());
		}
	}

	private void cadastrarPlano() throws Exception {
		System.out.println("\n--- Cadastrar Plano ---");
		Plano plano = lerDadosPlano(null);
		plano = planoService.cadastrar(plano);
		System.out.println("✅ Plano cadastrado com sucesso! ID: " + plano.getId());
	}

	private void listarPlanos() throws Exception {
		List<Plano> planos = planoService.listar();
		System.out.println("\n--- Lista de Planos ---");
		if (planos.isEmpty()) {
			System.out.println("Nenhum plano cadastrado.");
		} else {
			for (Plano p : planos) {
				System.out.printf("  [%d] %s | R$ %.2f | %d mês(es) | %s%n",
						p.getId(), p.getNome(), p.getValor(), p.getDuracaoMeses(),
						p.getDescricao() != null ? p.getDescricao() : "-");
			}
			System.out.println("Total: " + planos.size() + " plano(s)");
		}
	}

	private void editarPlano() throws Exception {
		System.out.print("\nID do plano a editar: ");
		int id = Integer.parseInt(scanner.nextLine().trim());
		Plano plano = planoService.buscarPorId(id);
		if (plano == null) {
			System.out.println("❌ Plano não encontrado.");
			return;
		}
		Plano dados = lerDadosPlano(plano);
		plano.setNome(dados.getNome());
		plano.setValor(dados.getValor());
		plano.setDuracaoMeses(dados.getDuracaoMeses());
		plano.setDescricao(dados.getDescricao());
		planoService.atualizar(plano);
		System.out.println("✅ Plano atualizado com sucesso!");
	}

	private void removerPlano() throws Exception {
		System.out.print("\nID do plano a remover: ");
		int id = Integer.parseInt(scanner.nextLine().trim());
		planoService.deletar(id);
		System.out.println("✅ Plano removido com sucesso!");
	}

	private Plano lerDadosPlano(Plano atual) {
		System.out.print("Nome: ");
		String nome = scanner.nextLine().trim();
		System.out.print("Valor (R$): ");
		BigDecimal valor = new BigDecimal(scanner.nextLine().trim().replace(",", "."));
		System.out.print("Duração (meses): ");
		int duracao = Integer.parseInt(scanner.nextLine().trim());
		System.out.print("Descrição: ");
		String descricao = scanner.nextLine().trim();
		return new Plano(nome, valor, duracao, descricao.isEmpty() ? null : descricao);
	}
}
