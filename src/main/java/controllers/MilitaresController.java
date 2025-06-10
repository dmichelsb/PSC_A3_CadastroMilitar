package controllers;

import database.DB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Militar;

import java.io.IOException;
import java.sql.*;
import java.util.function.Predicate;

public class MilitaresController {

    @FXML private TextField filtroField;
    @FXML private TableView<Militar> tabelaMilitares;
    @FXML private TableColumn<Militar, String> colSaram, colNome, colPosto, colAdmissao, colCpf,
            colSexo, colNascimento, colNaturalidade, colQuadro, colUnidade, colSituacao;

    private static final ObservableList<Militar> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colSaram.setCellValueFactory(d -> d.getValue().saramProperty());
        colNome.setCellValueFactory(d -> d.getValue().nomeCompletoProperty());
        colPosto.setCellValueFactory(d -> d.getValue().postoProperty());
        colAdmissao.setCellValueFactory(d -> d.getValue().dataAdmissaoProperty());
        colCpf.setCellValueFactory(d -> d.getValue().cpfProperty());
        colSexo.setCellValueFactory(d -> d.getValue().sexoProperty());
        colNascimento.setCellValueFactory(d -> d.getValue().dataNascimentoProperty());
        colNaturalidade.setCellValueFactory(d -> d.getValue().naturalidadeProperty());
        colQuadro.setCellValueFactory(d -> d.getValue().quadroProperty());
        colUnidade.setCellValueFactory(d -> d.getValue().unidadeProperty());
        colSituacao.setCellValueFactory(d -> d.getValue().situacaoAtualProperty());

        carregarDados();
    }

    public void carregarDados() {
        lista.clear();
        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM militares")) {

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

            tabelaMilitares.setItems(lista);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void adicionar() {
        abrirFormulario(null);
    }

    @FXML
    public void editar() {
        Militar selecionado = tabelaMilitares.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            abrirFormulario(selecionado);
        }
    }

    @FXML
    public void remover() {
        Militar m = tabelaMilitares.getSelectionModel().getSelectedItem();
        if (m == null) return;

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM militares WHERE saram=?")) {
            stmt.setString(1, m.getSaram());
            stmt.executeUpdate();
            carregarDados();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void filtrar() {
        String texto = filtroField.getText().toLowerCase();
        Predicate<Militar> filtro = m ->
                m.getNomeCompleto().toLowerCase().contains(texto) ||
                        m.getSaram().toLowerCase().contains(texto);

        tabelaMilitares.setItems(lista.filtered(filtro));
    }

    @FXML
    public void limparFiltro() {
        filtroField.clear();
        tabelaMilitares.setItems(lista);
    }

    private void abrirFormulario(Militar militar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/cadastro.fxml"));
            Parent root = loader.load();

            CadastroController controller = loader.getController();
            controller.setMilitar(militar);

            Stage stage = new Stage();
            stage.setTitle(militar == null ? "Adicionar Militar" : "Editar Militar");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            carregarDados();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void inserirMilitar(Militar m) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO militares VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
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

    public static void atualizarMilitar(Militar m) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE militares SET nomeCompleto=?, posto=?, dataAdmissao=?, cpf=?, sexo=?, dataNascimento=?, naturalidade=?, quadro=?, unidade=?, situacaoAtual=? WHERE saram=?")) {
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
}
