package dao;

import database.DB;
import models.Missao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MissaoDAO {

    // Guarda o último ID inserido nesta instância
    private int ultimoId = -1;

    public int inserir(Missao missao) {
        String sql = "INSERT INTO Missoes (tipo, local, dataInicio, dataTermino, status, descricao) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, missao.getTipo());
            stmt.setString(2, missao.getLocal());
            stmt.setString(3, missao.getDataInicio());
            stmt.setString(4, missao.getDataTermino());
            stmt.setString(5, missao.getStatus());
            stmt.setString(6, missao.getDescricao());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);  // ✅ chave real gerada pelo banco
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // caso de erro
    }


    public void atualizar(Missao missao) {
        String sql = "UPDATE Missoes SET tipo=?, local=?, dataInicio=?, dataTermino=?, status=?, descricao=? WHERE id=?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, missao.getTipo());
            stmt.setString(2, missao.getLocal());
            stmt.setString(3, missao.getDataInicio());
            stmt.setString(4, missao.getDataTermino());
            stmt.setString(5, missao.getStatus());
            stmt.setString(6, missao.getDescricao());
            stmt.setString(7, missao.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remover(String id) {
        String sql = "DELETE FROM Missoes WHERE id=?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getUltimoId() {
        return ultimoId; // agora retorna o que foi capturado na inserção
    }

    public Missao getById(String id) {
        String sql = "SELECT * FROM Missoes WHERE id=?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Missao(
                        rs.getString("id"),
                        rs.getString("tipo"),
                        rs.getString("local"),
                        rs.getString("dataInicio"),
                        rs.getString("dataTermino"),
                        rs.getString("status"),
                        rs.getString("descricao")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
