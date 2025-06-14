package dao;

import database.DB;
import models.Militar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class MilitarDAO {

    public ObservableList<Militar> getAll() {
        ObservableList<Militar> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM militares";

        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Militar m = new Militar(
                        rs.getString("saram"),
                        rs.getString("nomeCompleto"),
                        rs.getString("posto"),
                        rs.getString("dataAdmissao"),
                        rs.getString("cpf"),
                        rs.getString("sexo"),
                        rs.getString("dataNascimento"),
                        rs.getString("naturalidade"),
                        rs.getString("quadro"),
                        rs.getString("unidade"),
                        rs.getString("situacaoAtual")
                );
                lista.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void inserir(Militar m) {
        String sql = "INSERT INTO militares VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, m.getSaram());
            stmt.setString(2, m.getNomeCompleto());
            stmt.setString(3, m.getPosto());
            stmt.setString(4, m.getDataAdmissao());
            stmt.setString(5, m.getCpf());
            stmt.setString(6, m.getSexo());
            stmt.setString(7, m.getDataNascimento());
            stmt.setString(8, m.getNaturalidade());
            stmt.setString(9, m.getQuadro());
            stmt.setString(10, m.getUnidade());
            stmt.setString(11, m.getSituacaoAtual());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizar(Militar m) {
        String sql = "UPDATE militares SET nomeCompleto=?, posto=?, dataAdmissao=?, cpf=?, sexo=?, dataNascimento=?, naturalidade=?, quadro=?, unidade=?, situacaoAtual=? WHERE saram=?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, m.getNomeCompleto());
            stmt.setString(2, m.getPosto());
            stmt.setString(3, m.getDataAdmissao());
            stmt.setString(4, m.getCpf());
            stmt.setString(5, m.getSexo());
            stmt.setString(6, m.getDataNascimento());
            stmt.setString(7, m.getNaturalidade());
            stmt.setString(8, m.getQuadro());
            stmt.setString(9, m.getUnidade());
            stmt.setString(10, m.getSituacaoAtual());
            stmt.setString(11, m.getSaram());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remover(String saram) {
        String sql = "DELETE FROM militares WHERE saram=?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, saram);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
