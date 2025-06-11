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
import java.util.Map;
import java.util.function.Predicate;

public class MilitaresController {

    @FXML private Label totalLabel;
    @FXML private Label ativosLabel;
    @FXML private Label homensLabel;
    @FXML private Label mulheresLabel;

    @FXML private TextField filtroField;
    @FXML private TableView<Militar> tabelaMilitares;
    @FXML private TableColumn<Militar, String> colSaram, colNome, colPosto, colAdmissao, colCpf,
            colSexo, colNascimento, colNaturalidade, colQuadro, colUnidade, colSituacao;

    private static final ObservableList<Militar> lista = FXCollections.observableArrayList();


    private static final Map<String, Integer> hierarquiaPatente = Map.ofEntries(
            Map.entry("Recruta", 1),
            Map.entry("Soldado", 2),
            Map.entry("Cabo", 3),
            Map.entry("3º Sargento", 4),
            Map.entry("2º Sargento", 5),
            Map.entry("1º Sargento", 6),
            Map.entry("Subtenente", 7),
            Map.entry("Aspirante", 8),
            Map.entry("2º Tenente", 9),
            Map.entry("1º Tenente", 10),
            Map.entry("Capitão", 11),
            Map.entry("Major", 12),
            Map.entry("Tenente-Coronel", 13),
            Map.entry("Coronel", 14),
            Map.entry("General de Brigada", 15),
            Map.entry("General de Divisão", 16),
            Map.entry("General de Exército", 17)
    );

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
        colPosto.setComparator((a, b) -> {
            return Integer.compare(
                    hierarquiaPatente.getOrDefault(a, 0),
                    hierarquiaPatente.getOrDefault(b, 0)
            );
        });
    }

    public void carregarDados() {
        lista.clear();
        int total = 0, ativos = 0, homens = 0, mulheres = 0;
        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM militares")) {

            while (rs.next()) {
                String sexo = rs.getString("sexo");
                String situacao = rs.getString("situacaoAtual");

                if (sexo.equalsIgnoreCase("Masculino")) homens++;
                else if (sexo.equalsIgnoreCase("Feminino")) mulheres++;

                if (situacao.equalsIgnoreCase("Ativo")) ativos++;

                total++;

                Militar m = new Militar(
                        rs.getString("saram"),
                        rs.getString("nomeCompleto"),
                        rs.getString("posto"),
                        rs.getString("dataAdmissao"),
                        rs.getString("cpf"),
                        sexo,
                        rs.getString("dataNascimento"),
                        rs.getString("naturalidade"),
                        rs.getString("quadro"),
                        rs.getString("unidade"),
                        situacao
                );

                lista.add(m);
            }
            totalLabel.setText("Militares Cadastrados: " + total);
            ativosLabel.setText("Militares Ativos: " + ativos);
            homensLabel.setText("Militares Homens: " + homens);
            mulheresLabel.setText("Militares Mulheres: " + mulheres);

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

    @FXML
    public void logoff() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root));
            stage.show();

            // Fecha a janela atual
            Stage currentStage = (Stage) tabelaMilitares.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
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
