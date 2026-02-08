package com.fftmanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fftmanager.database.DatabaseConnection;
import com.fftmanager.model.Pagamento;

public class PagamentoDAO {

	public Pagamento inserir(Pagamento pagamento) throws SQLException {
		String sql = "INSERT INTO pagamentos (matricula_id, valor, data_vencimento, status) VALUES (?, ?, ?, ?)";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, pagamento.getMatriculaId());
			stmt.setBigDecimal(2, pagamento.getValor());
			stmt.setObject(3, pagamento.getDataVencimento());
			stmt.setString(4, pagamento.getStatus() != null ? pagamento.getStatus() : "PENDENTE");

			stmt.executeUpdate();

			try (ResultSet rs = stmt.getGeneratedKeys()) {
				if (rs.next()) {
					pagamento.setId(rs.getInt(1));
				}
			}
		}
		return pagamento;
	}

	public boolean atualizar(Pagamento pagamento) throws SQLException {
		String sql = "UPDATE pagamentos SET matricula_id = ?, valor = ?, data_vencimento = ?, data_pagamento = ?, status = ? WHERE id = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, pagamento.getMatriculaId());
			stmt.setBigDecimal(2, pagamento.getValor());
			stmt.setObject(3, pagamento.getDataVencimento());
			stmt.setObject(4, pagamento.getDataPagamento());
			stmt.setString(5, pagamento.getStatus());
			stmt.setInt(6, pagamento.getId());

			return stmt.executeUpdate() > 0;
		}
	}

	public boolean deletar(int id) throws SQLException {
		String sql = "DELETE FROM pagamentos WHERE id = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			return stmt.executeUpdate() > 0;
		}
	}

	public Pagamento buscarPorId(int id) throws SQLException {
		String sql = "SELECT id, matricula_id, valor, data_vencimento, data_pagamento, status FROM pagamentos WHERE id = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return mapearPagamento(rs);
				}
			}
		}
		return null;
	}

	public List<Pagamento> buscarPorMatricula(int matriculaId) throws SQLException {
		String sql = "SELECT id, matricula_id, valor, data_vencimento, data_pagamento, status FROM pagamentos WHERE matricula_id = ? ORDER BY data_vencimento";
		List<Pagamento> pagamentos = new ArrayList<>();

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, matriculaId);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					pagamentos.add(mapearPagamento(rs));
				}
			}
		}
		return pagamentos;
	}

	public List<Pagamento> buscarPendentes() throws SQLException {
		String sql = "SELECT id, matricula_id, valor, data_vencimento, data_pagamento, status FROM pagamentos WHERE status = 'PENDENTE' ORDER BY data_vencimento";
		List<Pagamento> pagamentos = new ArrayList<>();

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				pagamentos.add(mapearPagamento(rs));
			}
		}
		return pagamentos;
	}

	public boolean registrarPagamento(int id, LocalDate dataPagamento) throws SQLException {
		String sql = "UPDATE pagamentos SET data_pagamento = ?, status = 'PAGO' WHERE id = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setObject(1, dataPagamento);
			stmt.setInt(2, id);
			return stmt.executeUpdate() > 0;
		}
	}

	public List<Pagamento> listarTodos() throws SQLException {
		String sql = "SELECT id, matricula_id, valor, data_vencimento, data_pagamento, status FROM pagamentos ORDER BY data_vencimento DESC";
		List<Pagamento> pagamentos = new ArrayList<>();

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				pagamentos.add(mapearPagamento(rs));
			}
		}
		return pagamentos;
	}

	private Pagamento mapearPagamento(ResultSet rs) throws SQLException {
		Pagamento pagamento = new Pagamento();
		pagamento.setId(rs.getInt("id"));
		pagamento.setMatriculaId(rs.getInt("matricula_id"));
		pagamento.setValor(rs.getBigDecimal("valor"));

		java.sql.Date dataVenc = rs.getDate("data_vencimento");
		pagamento.setDataVencimento(dataVenc != null ? dataVenc.toLocalDate() : null);

		java.sql.Date dataPag = rs.getDate("data_pagamento");
		pagamento.setDataPagamento(dataPag != null ? dataPag.toLocalDate() : null);

		pagamento.setStatus(rs.getString("status"));

		return pagamento;
	}
}
