package com.fftmanager.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.fftmanager.dao.MatriculaDAO;
import com.fftmanager.dao.PagamentoDAO;
import com.fftmanager.dao.PlanoDAO;
import com.fftmanager.model.Matricula;
import com.fftmanager.model.Pagamento;
import com.fftmanager.model.Plano;

public class MatriculaService {

	private final MatriculaDAO matriculaDAO = new MatriculaDAO();
	private final PlanoDAO planoDAO = new PlanoDAO();
	private final PagamentoDAO pagamentoDAO = new PagamentoDAO();

	private Plano validarPlanoExiste(int planoId) throws SQLException {
		Plano plano = planoDAO.buscarPorId(planoId);
		if (plano == null) {
			throw new IllegalArgumentException("Plano não encontrado.");
		}
		return plano;
	}

	private void validarAlunoExiste(int alunoId) throws SQLException {
		AlunoService alunoService = new AlunoService();
		if (alunoService.buscarPorId(alunoId) == null) {
			throw new IllegalArgumentException("Aluno não encontrado.");
		}
	}

	private void validarMatriculaAtiva(int alunoId) throws SQLException {
		List<Matricula> ativas = matriculaDAO.buscarPorAluno(alunoId).stream()
				.filter(Matricula::isAtivo)
				.collect(Collectors.toList());
		if (!ativas.isEmpty()) {
			throw new IllegalArgumentException("Aluno já possui matrícula ativa. Inative a anterior antes de cadastrar nova.");
		}
	}

	public Matricula cadastrar(int alunoId, int planoId, LocalDate dataInicio) throws SQLException {
		validarAlunoExiste(alunoId);
		validarPlanoExiste(planoId);
		validarMatriculaAtiva(alunoId);

		Plano plano = planoDAO.buscarPorId(planoId);
		LocalDate dataFim = dataInicio.plusMonths(plano.getDuracaoMeses());

		Matricula matricula = new Matricula(alunoId, planoId, dataInicio, dataFim);
		matricula = matriculaDAO.inserir(matricula);

		gerarPagamentosAutomaticos(matricula, plano);

		return matricula;
	}

	private void gerarPagamentosAutomaticos(Matricula matricula, Plano plano) throws SQLException {
		BigDecimal valorMensal = plano.getValor();
		LocalDate dataVencimento = matricula.getDataInicio();
		int quantidadeMeses = plano.getDuracaoMeses();

		for (int i = 0; i < quantidadeMeses; i++) {
			Pagamento pagamento = new Pagamento(matricula.getId(), valorMensal, dataVencimento);
			pagamentoDAO.inserir(pagamento);
			dataVencimento = dataVencimento.plusMonths(1);
		}
	}

	public boolean inativar(int id) throws SQLException {
		Matricula matricula = matriculaDAO.buscarPorId(id);
		if (matricula == null) {
			throw new IllegalArgumentException("Matrícula não encontrada.");
		}
		if (!matricula.isAtivo()) {
			throw new IllegalArgumentException("Matrícula já está inativa.");
		}
		return matriculaDAO.inativar(id);
	}

	public Matricula buscarPorId(int id) throws SQLException {
		return matriculaDAO.buscarPorId(id);
	}

	public List<Matricula> listar() throws SQLException {
		return matriculaDAO.listarTodos();
	}

	public List<Matricula> listarAtivas() throws SQLException {
		return matriculaDAO.listarAtivas();
	}

	public List<Matricula> listarPorAluno(int alunoId) throws SQLException {
		return matriculaDAO.buscarPorAluno(alunoId);
	}
}
