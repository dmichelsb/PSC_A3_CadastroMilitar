package controllers;

import dao.MilitarMissaoDAO;
import dao.MissaoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Missao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DB;

public class VincularMissaoController {

    @FXML private TableView<Missao> tabelaTodasMissoes;
    @FXML private TableColumn<Missao, String> colId, colTipo, colLocal, colInicio, colTermino;

    private final MissaoDAO missaoDAO = new MissaoDAO();
    private final MilitarMissaoDAO militarMissaoDAO = new MilitarMissaoDAO();
    private final ObservableList<Missao> lista = FXCollections.observableArrayList();

    private String militarSaram; // Só armazena, NÃO carrega

    // --- chamado pelo MilitaresController ---
    public void setMilitarSaram(String saram) {
        this.militarSaram = saram;
    }

    // --- inicializa quando FXML carrega ---
    @FXML
    public void initialize() {
        // Listras alternadas na tabela de missões (popup de Vincular)
        tabelaTodasMissoes.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Missao item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else if (isSelected()) {
                    setStyle("-fx-background-color: #879E6D; -fx-text-fill: white;");
                } else if (getIndex() % 2 == 0) {
                    setStyle("-fx-background-color: #cccccc;");
                } else {
                    setStyle("-fx-background-color: white;");
                }
            }
        });

        colId.setCellValueFactory(d -> d.getValue().idProperty());
        colTipo.setCellValueFactory(d -> d.getValue().tipoProperty());
        colLocal.setCellValueFactory(d -> d.getValue().localProperty());
        colInicio.setCellValueFactory(d -> d.getValue().dataInicioProperty());
        colTermino.setCellValueFactory(d -> d.getValue().dataTerminoProperty());

        carregar();
    }

    private void carregar() {
        lista.clear();
        lista.addAll(missaoDAO.getAll());
        tabelaTodasMissoes.setItems(lista);
    }

    private MilitaresController militaresController;

    public void setMilitaresController(MilitaresController controller) {
        this.militaresController = controller;
    }

    // --- Botões funcionando ---
    @FXML
    public void vincular() {
        Missao m = tabelaTodasMissoes.getSelectionModel().getSelectedItem();
        if (m == null) return;

        militarMissaoDAO.vincular(militarSaram, m.getId());

        // 🔑 Atualiza tabela do próprio popup
        carregar();

        // 🔑 Atualiza a lista do MilitaresController IMEDIATAMENTE
        if (militaresController != null) {
            militaresController.carregarMissoes(militarSaram);
        }
    }

    @FXML
    public void adicionarTodos() {
        Missao m = tabelaTodasMissoes.getSelectionModel().getSelectedItem();
        if (m == null) return;

        try (Connection conn = DB.getConnection()) {
            // Primeiro: pegar todos os saram
            PreparedStatement psSaram = conn.prepareStatement("SELECT saram FROM militares");
            ResultSet rs = psSaram.executeQuery();

            // Segundo: preparar UM statement de inserção
            PreparedStatement psInsert = conn.prepareStatement(
                    "INSERT OR IGNORE INTO Militar_Missoes (militar_id, missao_id) VALUES (?, ?)"
            );

            while (rs.next()) {
                String saram = rs.getString("saram");
                psInsert.setString(1, saram);
                psInsert.setString(2, m.getId());
                psInsert.addBatch();
            }

            // Executa tudo de uma vez
            psInsert.executeBatch();

            // Atualiza tabela popup
            carregar();

            // Atualiza lista do pai se precisar (opcional)
            if (militaresController != null) {
                militaresController.carregarMissoes(militarSaram);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void removerTodos() {
        Missao m = tabelaTodasMissoes.getSelectionModel().getSelectedItem();
        if (m == null) return;

        try (Connection conn = DB.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Militar_Missoes WHERE missao_id = ?");
            ps.setString(1, m.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 🔑 Atualiza a tabela do pop-up:
        carregar();

        // 🔑 Atualiza IMEDIATAMENTE a lista do MilitaresController:
        if (militaresController != null) {
            militaresController.carregarMissoes(militarSaram);
        }
    }


    @FXML
    public void excluirMissaoDoBanco() {
        Missao m = tabelaTodasMissoes.getSelectionModel().getSelectedItem();
        if (m == null) return;

        try (Connection conn = DB.getConnection()) {
            PreparedStatement ps1 = conn.prepareStatement("DELETE FROM Militar_Missoes WHERE missao_id = ?");
            ps1.setString(1, m.getId());
            ps1.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement("DELETE FROM Missoes WHERE id = ?");
            ps2.setString(1, m.getId());
            ps2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        carregar();
    }

    @FXML
    public void cancelar() {
        fechar();
    }

    private void fechar() {
        Stage s = (Stage) tabelaTodasMissoes.getScene().getWindow();
        s.close();
    }




}
