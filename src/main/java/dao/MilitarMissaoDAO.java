package dao;

import database.DB;
import models.Missao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MilitarMissaoDAO {

    public void vincular(String militarSaram, String missaoId) {
        String sql = "INSERT INTO Militar_Missoes (militar_id, missao_id) VALUES (?, ?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, militarSaram);
            stmt.setString(2, missaoId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Missao> buscarMissoesPorMilitar(String militarSaram) {
        List<Missao> lista = new ArrayList<>();
        String sql = "SELECT m.* FROM Missoes m " +
                "JOIN Militar_Missoes mm ON m.id = mm.missao_id " +
                "WHERE mm.militar_id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, militarSaram);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Missao m = new Missao(
                        rs.getString("id"),
                        rs.getString("tipo"),
                        rs.getString("local"),
                        rs.getString("dataInicio"),
                        rs.getString("dataTermino"),
                        rs.getString("status"),
                        rs.getString("descricao")
                );
                lista.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
