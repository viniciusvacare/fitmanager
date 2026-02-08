package com.fftmanager.view;

import java.util.Scanner;

public class MenuPrincipal {

	private final Scanner scanner = new Scanner(System.in);
	private final MenuAlunos menuAlunos = new MenuAlunos(scanner);
	private final MenuPlanos menuPlanos = new MenuPlanos(scanner);
	private final MenuMatriculas menuMatriculas = new MenuMatriculas(scanner);
	private final MenuPagamentos menuPagamentos = new MenuPagamentos(scanner);
	private final MenuRelatorios menuRelatorios = new MenuRelatorios(scanner);

	public void executar() {
		int opcao;
		do {
			exibirMenu();
			opcao = capturarOpcao();
			redirecionar(opcao);
		} while (opcao != 0);
		scanner.close();
	}

	private void exibirMenu() {
		System.out.println("\n=== SISTEMA DE GERENCIAMENTO DE ACADEMIA ===");
		System.out.println("1. Gerenciar Alunos");
		System.out.println("2. Gerenciar Planos");
		System.out.println("3. Gerenciar Matrículas");
		System.out.println("4. Gerenciar Pagamentos");
		System.out.println("5. Relatórios");
		System.out.println("0. Sair");
		System.out.print("\nEscolha uma opção: ");
	}

	private int capturarOpcao() {
		try {
			return Integer.parseInt(scanner.nextLine().trim());
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private void redirecionar(int opcao) {
		switch (opcao) {
			case 1 -> menuAlunos.executar();
			case 2 -> menuPlanos.executar();
			case 3 -> menuMatriculas.executar();
			case 4 -> menuPagamentos.executar();
			case 5 -> menuRelatorios.executar();
			case 0 -> System.out.println("\nEncerrando sistema... Até logo!");
			default -> System.out.println("\n❌ Opção inválida! Tente novamente.");
		}
	}
}
