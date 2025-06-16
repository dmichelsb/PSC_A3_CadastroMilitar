package controllers;

import dao.MilitarDAO;
import dao.MilitarMissaoDAO;
import dao.MissaoDAO;
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
import models.Missao;

import java.io.IOException;
import java.util.Map;
import java.util.function.Predicate;

public class MilitaresController {

    @FXML private Label totalLabel, ativosLabel, homensLabel, mulheresLabel, nomeMilitarLabel;
    @FXML private TextField filtroField;
    @FXML private TableView<Militar> tabelaMilitares;
    @FXML private TableColumn<Militar, String> colSaram, colNome, colPosto, colAdmissao, colCpf,
            colSexo, colNascimento, colNaturalidade, colQuadro, colUnidade, colSituacao;

    @FXML private TableView<Missao> tabelaMissoes;
    @FXML private TableColumn<Missao, String> colTipo, colLocal, colInicio, colTermino, colStatus;
    @FXML private TextArea descricaoArea;

    private final MilitarDAO militarDAO = new MilitarDAO();
    private final MilitarMissaoDAO militarMissaoDAO = new MilitarMissaoDAO();
    private final MissaoDAO missaoDAO = new MissaoDAO();
    private final ObservableList<Militar> lista = FXCollections.observableArrayList();
    private final ObservableList<Missao> listaMissoes = FXCollections.observableArrayList();

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

        colId.setCellValueFactory(d -> d.getValue().idProperty());

        // Listras alternadas
        tabelaMilitares.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Militar item, boolean empty) {
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

        // Colunas Militar
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
        colPosto.setComparator((a, b) -> Integer.compare(
                hierarquiaPatente.getOrDefault(a, 0),
                hierarquiaPatente.getOrDefault(b, 0)
        ));

        // Colunas Missão
        colTipo.setCellValueFactory(d -> d.getValue().tipoProperty());
        colLocal.setCellValueFactory(d -> d.getValue().localProperty());
        colInicio.setCellValueFactory(d -> d.getValue().dataInicioProperty());
        colTermino.setCellValueFactory(d -> d.getValue().dataTerminoProperty());
        colStatus.setCellValueFactory(d -> d.getValue().statusProperty());

        // Listener Missão -> Descrição
        tabelaMissoes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {

            if (newVal != null) {
                descricaoArea.setText(newVal.getDescricao());
            } else {
                descricaoArea.clear();
            }
        });

        tabelaMissoes.setRowFactory(tv -> new TableRow<>() {
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


        // Listener Militar -> carrega Missões + nome
        tabelaMilitares.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                carregarMissoes(newVal.getSaram());
                nomeMilitarLabel.setText(newVal.getNomeCompleto());
            } else {
                listaMissoes.clear();
                descricaoArea.clear();
                nomeMilitarLabel.setText("");
            }
        });

        carregarDados();
    }

    public void carregarDados() {
        lista.clear();
        lista.addAll(militarDAO.getAll());

        int total = lista.size();
        int ativos = 0, homens = 0, mulheres = 0;

        for (Militar m : lista) {
            if (m.getSexo().equalsIgnoreCase("Masculino")) homens++;
            else if (m.getSexo().equalsIgnoreCase("Feminino")) mulheres++;
            if (m.getSituacaoAtual().equalsIgnoreCase("Ativo")) ativos++;
        }

        totalLabel.setText("Militares Cadastrados: " + total);
        ativosLabel.setText("Militares Ativos: " + ativos);
        homensLabel.setText("Militares Homens: " + homens);
        mulheresLabel.setText("Militares Mulheres: " + mulheres);

        tabelaMilitares.setItems(lista);
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

    @FXML
    public void adicionar() { abrirFormulario(null); }

    @FXML
    public void editar() {
        Militar m = tabelaMilitares.getSelectionModel().getSelectedItem();
        if (m != null) abrirFormulario(m);
    }

    @FXML
    public void remover() {
        Militar m = tabelaMilitares.getSelectionModel().getSelectedItem();
        if (m != null) {
            militarDAO.remover(m.getSaram());
            carregarDados();
        }
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
        } catch (IOException e) { e.printStackTrace(); }
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
            ((Stage) tabelaMilitares.getScene().getWindow()).close();
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    public void adicionarMissao() {
        Militar m = tabelaMilitares.getSelectionModel().getSelectedItem();
        if (m == null) return;
        abrirCadastroMissao(null, m.getSaram());
    }

    @FXML
    public void editarMissao() {
        Militar m = tabelaMilitares.getSelectionModel().getSelectedItem();
        Missao missao = tabelaMissoes.getSelectionModel().getSelectedItem();
        if (m == null || missao == null) return;
        abrirCadastroMissao(missao, m.getSaram());
    }

    @FXML
    public void removerMissao() {
        Militar militar = tabelaMilitares.getSelectionModel().getSelectedItem();
        Missao missao = tabelaMissoes.getSelectionModel().getSelectedItem();
        if (militar == null || missao == null) return;

        militarMissaoDAO.removerVinculo(militar.getSaram(), missao.getId());
        carregarMissoes(militar.getSaram());
    }


    private void abrirCadastroMissao(Missao missao, String saram) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/cadastroMissao.fxml"));
            Parent root = loader.load();
            MissaoController controller = loader.getController();
            controller.setDados(missao, saram);
            Stage stage = new Stage();
            stage.setTitle(missao == null ? "Adicionar Missão" : "Editar Missão");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            carregarMissoes(saram);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void carregarMissoes(String saram) {
        listaMissoes.clear();
        listaMissoes.addAll(militarMissaoDAO.buscarMissoesPorMilitar(saram));
        tabelaMissoes.setItems(listaMissoes);
        if (!listaMissoes.isEmpty()) {
            tabelaMissoes.getSelectionModel().selectFirst();
        } else {
            descricaoArea.clear();
        }
    }

    @FXML private TableColumn<Missao, String> colId;

    @FXML
    public void vincularMissaoExistente() {
        Militar m = tabelaMilitares.getSelectionModel().getSelectedItem();
        if (m == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/vincularMissao.fxml"));
            Parent root = loader.load();

            VincularMissaoController controller = loader.getController();
            controller.setMilitarSaram(m.getSaram());
            controller.setMilitaresController(this); // 👉 ISSO GARANTE A COMUNICAÇÃO

            Stage stage = new Stage();
            stage.setTitle("Vincular Missão Existente");
            stage.setScene(new Scene(root));
            stage.showAndWait();

// Recarregar depois que o popup fecha (opcional, pois já faz online)
            carregarMissoes(m.getSaram());
            ;


        } catch (IOException e) {
            e.printStackTrace();
        }
    }






}
