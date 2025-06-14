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

    // Inicialização, se precisar popular comboBoxes, etc.
    public void initialize() {
        // Exemplo: postoBox.getItems().addAll("Soldado", "Cabo", "Sargento");
    }

    // Preenche os campos se estiver editando um militar existente
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

        // Verifica se o campo SARAM foi preenchido
        if (novoSaram.isEmpty()) {
            showAlerta("Erro", "O campo SARAM é obrigatório!");
            return;
        }

        // Verifica se já existe OUTRO militar com o mesmo SARAM (para cadastro E edição)
        boolean saramExiste = MilitaresController.getListaMilitares()
                .stream()
                .anyMatch(m -> m.getSaram().equals(novoSaram)
                        && (militar == null || !m.getSaram().equals(militar.getSaram())));

        if (saramExiste) {
            showAlerta("Erro", "Já existe um militar com esse SARAM!");
            return;
        }

        // Monta o objeto Militar com os campos preenchidos
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

        // Se estiver editando -> atualiza | senão -> insere
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

    // Método auxiliar para mostrar alertas de erro
    private void showAlerta(String titulo, String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
