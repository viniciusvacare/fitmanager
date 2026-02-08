package com.fftmanager.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import com.fftmanager.dao.PlanoDAO;
import com.fftmanager.model.Plano;

public class PlanoService {

	private static final int NOME_MAX_LENGTH = 50;
	private static final int DESCRICAO_MAX_LENGTH = 255;

	private final PlanoDAO planoDAO = new PlanoDAO();

	private void validarPlano(Plano plano) {
		if (plano == null) {
			throw new IllegalArgumentException("Plano não pode ser nulo.");
		}
		if (plano.getNome() == null || plano.getNome().trim().isEmpty()) {
			throw new IllegalArgumentException("Nome do plano é obrigatório.");
		}
		if (plano.getNome().length() > NOME_MAX_LENGTH) {
			throw new IllegalArgumentException("Nome do plano não pode exceder " + NOME_MAX_LENGTH + " caracteres.");
		}
		if (plano.getValor() == null || plano.getValor().compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Valor do plano deve ser maior que zero.");
		}
		if (plano.getDuracaoMeses() < 1) {
			throw new IllegalArgumentException("Duração do plano deve ser de pelo menos 1 mês.");
		}
		if (plano.getDescricao() != null && plano.getDescricao().length() > DESCRICAO_MAX_LENGTH) {
			throw new IllegalArgumentException("Descrição não pode exceder " + DESCRICAO_MAX_LENGTH + " caracteres.");
		}
	}

	public Plano cadastrar(Plano plano) throws SQLException {
		validarPlano(plano);
		return planoDAO.inserir(plano);
	}

	public boolean atualizar(Plano plano) throws SQLException {
		if (plano == null || plano.getId() <= 0) {
			throw new IllegalArgumentException("Plano inválido para atualização.");
		}
		if (planoDAO.buscarPorId(plano.getId()) == null) {
			throw new IllegalArgumentException("Plano não encontrado.");
		}
		validarPlano(plano);
		return planoDAO.atualizar(plano);
	}

	public boolean deletar(int id) throws SQLException {
		if (id <= 0) {
			throw new IllegalArgumentException("ID inválido.");
		}
		if (planoDAO.buscarPorId(id) == null) {
			throw new IllegalArgumentException("Plano não encontrado.");
		}
		return planoDAO.deletar(id);
	}

	public Plano buscarPorId(int id) throws SQLException {
		return planoDAO.buscarPorId(id);
	}

	public List<Plano> listar() throws SQLException {
		return planoDAO.listarTodos();
	}
}
