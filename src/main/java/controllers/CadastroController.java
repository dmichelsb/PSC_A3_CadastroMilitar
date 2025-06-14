package controllers;

import dao.MilitarDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Militar;

public class CadastroController {

    @FXML private TextField saramField;
    @FXML private TextField nomeCompletoField;
    @FXML private ComboBox<String> postoBox;
    @FXML private DatePicker dataAdmissaoField;
    @FXML private TextField cpfField;
    @FXML private ComboBox<String> sexoBox;
    @FXML private DatePicker dataNascimentoField;
    @FXML private TextField naturalidadeField;
    @FXML private ComboBox<String> quadroBox;
    @FXML private TextField unidadeField;
    @FXML private ComboBox<String> situacaoBox;

    private Militar militar;
    private boolean editando = false;

    private final MilitarDAO militarDAO = new MilitarDAO();

    public void initialize() {
        // Popular ComboBoxes se quiser
        // postoBox.getItems().addAll("Soldado", "Cabo", "Sargento");
    }

    public void setMilitar(Militar m) {
        if (m != null) {
            this.militar = m;
            editando = true;

            saramField.setText(m.getSaram());
            nomeCompletoField.setText(m.getNomeCompleto());
            postoBox.setValue(m.getPosto());
            dataAdmissaoField.setValue(java.time.LocalDate.parse(m.getDataAdmissao()));
            cpfField.setText(m.getCpf());
            sexoBox.setValue(m.getSexo());
            dataNascimentoField.setValue(java.time.LocalDate.parse(m.getDataNascimento()));
            naturalidadeField.setText(m.getNaturalidade());
            quadroBox.setValue(m.getQuadro());
            unidadeField.setText(m.getUnidade());
            situacaoBox.setValue(m.getSituacaoAtual());
        }
    }

    @FXML
    private void salvar() {
        String novoSaram = saramField.getText().trim();

        if (novoSaram.isEmpty()) {
            showAlerta("Erro", "O campo SARAM é obrigatório!");
            return;
        }

        boolean saramExiste = militarDAO.getAll()
                .stream()
                .anyMatch(m -> m.getSaram().equals(novoSaram)
                        && (militar == null || !m.getSaram().equals(militar.getSaram())));

        if (saramExiste) {
            showAlerta("Erro", "Já existe um militar com esse SARAM!");
            return;
        }

        Militar novo = new Militar(
                novoSaram,
                nomeCompletoField.getText(),
                postoBox.getValue(),
                dataAdmissaoField.getValue().toString(),
                cpfField.getText(),
                sexoBox.getValue(),
                dataNascimentoField.getValue().toString(),
                naturalidadeField.getText(),
                quadroBox.getValue(),
                unidadeField.getText(),
                situacaoBox.getValue()
        );

        if (editando) {
            militarDAO.atualizar(novo);
        } else {
            militarDAO.inserir(novo);
        }

        fechar();
    }

    @FXML
    private void cancelar() {
        fechar();
    }

    private void fechar() {
        Stage stage = (Stage) saramField.getScene().getWindow();
        stage.close();
    }

    private void showAlerta(String titulo, String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
