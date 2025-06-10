package controllers;

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

    public void initialize() {

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
        Militar novo = new Militar(
                saramField.getText(),
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
            MilitaresController.atualizarMilitar(novo);
        } else {
            MilitaresController.inserirMilitar(novo);
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

}
