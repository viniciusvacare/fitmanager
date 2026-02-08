package com.fftmanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fftmanager.database.DatabaseConnection;
import com.fftmanager.model.Plano;

public class PlanoDAO {

	public Plano inserir(Plano plano) throws SQLException {
		String sql = "INSERT INTO planos (nome, valor, duracao_meses, descricao) VALUES (?, ?, ?, ?)";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, plano.getNome());
			stmt.setBigDecimal(2, plano.getValor());
			stmt.setInt(3, plano.getDuracaoMeses());
			stmt.setString(4, plano.getDescricao());

			stmt.executeUpdate();

			try (ResultSet rs = stmt.getGeneratedKeys()) {
				if (rs.next()) {
					plano.setId(rs.getInt(1));
				}
			}
		}
		return plano;
	}

	public boolean atualizar(Plano plano) throws SQLException {
		String sql = "UPDATE planos SET nome = ?, valor = ?, duracao_meses = ?, descricao = ? WHERE id = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, plano.getNome());
			stmt.setBigDecimal(2, plano.getValor());
			stmt.setInt(3, plano.getDuracaoMeses());
			stmt.setString(4, plano.getDescricao());
			stmt.setInt(5, plano.getId());

			return stmt.executeUpdate() > 0;
		}
	}

	public boolean deletar(int id) throws SQLException {
		String sql = "DELETE FROM planos WHERE id = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			return stmt.executeUpdate() > 0;
		}
	}

	public Plano buscarPorId(int id) throws SQLException {
		String sql = "SELECT id, nome, valor, duracao_meses, descricao FROM planos WHERE id = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return mapearPlano(rs);
				}
			}
		}
		return null;
	}

	public List<Plano> listarTodos() throws SQLException {
		String sql = "SELECT id, nome, valor, duracao_meses, descricao FROM planos ORDER BY nome";
		List<Plano> planos = new ArrayList<>();

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				planos.add(mapearPlano(rs));
			}
		}
		return planos;
	}

	private Plano mapearPlano(ResultSet rs) throws SQLException {
		Plano plano = new Plano();
		plano.setId(rs.getInt("id"));
		plano.setNome(rs.getString("nome"));
		plano.setValor(rs.getBigDecimal("valor"));
		plano.setDuracaoMeses(rs.getInt("duracao_meses"));
		plano.setDescricao(rs.getString("descricao"));
		return plano;
	}
}
