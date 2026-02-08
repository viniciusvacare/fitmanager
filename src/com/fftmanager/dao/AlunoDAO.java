package com.fftmanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fftmanager.database.DatabaseConnection;
import com.fftmanager.model.Aluno;

public class AlunoDAO {

	public Aluno inserir(Aluno aluno) throws SQLException {
		String sql = "INSERT INTO alunos (nome, cpf, telefone, email, data_nascimento) VALUES (?, ?, ?, ?, ?)";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, aluno.getNome());
			stmt.setString(2, aluno.getCpf());
			stmt.setString(3, aluno.getTelefone());
			stmt.setString(4, aluno.getEmail());
			stmt.setObject(5, aluno.getDataNascimento());

			stmt.executeUpdate();

			try (ResultSet rs = stmt.getGeneratedKeys()) {
				if (rs.next()) {
					aluno.setId(rs.getInt(1));
				}
			}
		}
		return aluno;
	}

	public boolean atualizar(Aluno aluno) throws SQLException {
		String sql = "UPDATE alunos SET nome = ?, cpf = ?, telefone = ?, email = ?, data_nascimento = ? WHERE id = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, aluno.getNome());
			stmt.setString(2, aluno.getCpf());
			stmt.setString(3, aluno.getTelefone());
			stmt.setString(4, aluno.getEmail());
			stmt.setObject(5, aluno.getDataNascimento());
			stmt.setInt(6, aluno.getId());

			return stmt.executeUpdate() > 0;
		}
	}

	public boolean deletar(int id) throws SQLException {
		String sql = "DELETE FROM alunos WHERE id = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			return stmt.executeUpdate() > 0;
		}
	}

	public Aluno buscarPorId(int id) throws SQLException {
		String sql = "SELECT id, nome, cpf, telefone, email, data_nascimento, data_cadastro FROM alunos WHERE id = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return mapearAluno(rs);
				}
			}
		}
		return null;
	}

	public Aluno buscarPorCpf(String cpf) throws SQLException {
		String sql = "SELECT id, nome, cpf, telefone, email, data_nascimento, data_cadastro FROM alunos WHERE cpf = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, cpf);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return mapearAluno(rs);
				}
			}
		}
		return null;
	}

	public List<Aluno> listarTodos() throws SQLException {
		String sql = "SELECT id, nome, cpf, telefone, email, data_nascimento, data_cadastro FROM alunos ORDER BY nome";
		List<Aluno> alunos = new ArrayList<>();

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				alunos.add(mapearAluno(rs));
			}
		}
		return alunos;
	}

	private Aluno mapearAluno(ResultSet rs) throws SQLException {
		Aluno aluno = new Aluno();
		aluno.setId(rs.getInt("id"));
		aluno.setNome(rs.getString("nome"));
		aluno.setCpf(rs.getString("cpf"));
		aluno.setTelefone(rs.getString("telefone"));
		aluno.setEmail(rs.getString("email"));

		java.sql.Date dataNasc = rs.getDate("data_nascimento");
		aluno.setDataNascimento(dataNasc != null ? dataNasc.toLocalDate() : null);

		java.sql.Timestamp dataCad = rs.getTimestamp("data_cadastro");
		aluno.setDataCadastro(dataCad != null ? dataCad.toLocalDateTime() : null);

		return aluno;
	}
}
