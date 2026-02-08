package com.fftmanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fftmanager.database.DatabaseConnection;
import com.fftmanager.model.Matricula;

public class MatriculaDAO {

	public Matricula inserir(Matricula matricula) throws SQLException {
		String sql = "INSERT INTO matriculas (aluno_id, plano_id, data_inicio, data_fim, ativo) VALUES (?, ?, ?, ?, ?)";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, matricula.getAlunoId());
			stmt.setInt(2, matricula.getPlanoId());
			stmt.setObject(3, matricula.getDataInicio());
			stmt.setObject(4, matricula.getDataFim());
			stmt.setBoolean(5, matricula.isAtivo());

			stmt.executeUpdate();

			try (ResultSet rs = stmt.getGeneratedKeys()) {
				if (rs.next()) {
					matricula.setId(rs.getInt(1));
				}
			}
		}
		return matricula;
	}

	public boolean atualizar(Matricula matricula) throws SQLException {
		String sql = "UPDATE matriculas SET aluno_id = ?, plano_id = ?, data_inicio = ?, data_fim = ?, ativo = ? WHERE id = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, matricula.getAlunoId());
			stmt.setInt(2, matricula.getPlanoId());
			stmt.setObject(3, matricula.getDataInicio());
			stmt.setObject(4, matricula.getDataFim());
			stmt.setBoolean(5, matricula.isAtivo());
			stmt.setInt(6, matricula.getId());

			return stmt.executeUpdate() > 0;
		}
	}

	public boolean deletar(int id) throws SQLException {
		String sql = "DELETE FROM matriculas WHERE id = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			return stmt.executeUpdate() > 0;
		}
	}

	public Matricula buscarPorId(int id) throws SQLException {
		String sql = "SELECT id, aluno_id, plano_id, data_inicio, data_fim, ativo FROM matriculas WHERE id = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return mapearMatricula(rs);
				}
			}
		}
		return null;
	}

	public List<Matricula> buscarPorAluno(int alunoId) throws SQLException {
		String sql = "SELECT id, aluno_id, plano_id, data_inicio, data_fim, ativo FROM matriculas WHERE aluno_id = ? ORDER BY data_inicio DESC";
		List<Matricula> matriculas = new ArrayList<>();

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, alunoId);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					matriculas.add(mapearMatricula(rs));
				}
			}
		}
		return matriculas;
	}

	public List<Matricula> listarAtivas() throws SQLException {
		String sql = "SELECT id, aluno_id, plano_id, data_inicio, data_fim, ativo FROM matriculas WHERE ativo = TRUE ORDER BY data_inicio DESC";
		List<Matricula> matriculas = new ArrayList<>();

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				matriculas.add(mapearMatricula(rs));
			}
		}
		return matriculas;
	}

	public boolean inativar(int id) throws SQLException {
		String sql = "UPDATE matriculas SET ativo = FALSE WHERE id = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			return stmt.executeUpdate() > 0;
		}
	}

	public java.util.Map<Integer, Long> contarPorPlano() throws SQLException {
		String sql = "SELECT plano_id, COUNT(*) as total FROM matriculas GROUP BY plano_id ORDER BY total DESC";
		java.util.Map<Integer, Long> map = new java.util.LinkedHashMap<>();
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				map.put(rs.getInt("plano_id"), rs.getLong("total"));
			}
		}
		return map;
	}

	public List<Matricula> listarTodos() throws SQLException {
		String sql = "SELECT id, aluno_id, plano_id, data_inicio, data_fim, ativo FROM matriculas ORDER BY data_inicio DESC";
		List<Matricula> matriculas = new ArrayList<>();

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				matriculas.add(mapearMatricula(rs));
			}
		}
		return matriculas;
	}

	private Matricula mapearMatricula(ResultSet rs) throws SQLException {
		Matricula matricula = new Matricula();
		matricula.setId(rs.getInt("id"));
		matricula.setAlunoId(rs.getInt("aluno_id"));
		matricula.setPlanoId(rs.getInt("plano_id"));

		java.sql.Date dataInicio = rs.getDate("data_inicio");
		matricula.setDataInicio(dataInicio != null ? dataInicio.toLocalDate() : null);

		java.sql.Date dataFim = rs.getDate("data_fim");
		matricula.setDataFim(dataFim != null ? dataFim.toLocalDate() : null);

		matricula.setAtivo(rs.getBoolean("ativo"));

		return matricula;
	}
}
