package controllers;

import dao.MilitarMissaoDAO;
import dao.MissaoDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Missao;

public class MissaoController {

    @FXML private ComboBox<String> tipoBox;
    @FXML private TextField localField;
    @FXML private DatePicker inicioPicker;
    @FXML private DatePicker terminoPicker;
    @FXML private ComboBox<String> statusBox;
    @FXML private TextArea descricaoArea;

    private Missao missao;
    private boolean editando = false;
    private String militarSaram;

    private final MissaoDAO missaoDAO = new MissaoDAO();
    private final MilitarMissaoDAO militarMissaoDAO = new MilitarMissaoDAO();

    public void initialize() {
        tipoBox.getItems().addAll("Patrulhamento", "Resgate", "Reconhecimento", "Missão de Paz");
        statusBox.getItems().addAll("Planejada", "Em andamento", "Concluída", "Cancelada");
    }

    public void setDados(Missao missao, String militarSaram) {
        this.militarSaram = militarSaram;
        if (missao != null) {
            this.missao = missao;
            editando = true;

            tipoBox.setValue(missao.getTipo());
            localField.setText(missao.getLocal());
            inicioPicker.setValue(java.time.LocalDate.parse(missao.getDataInicio()));
            terminoPicker.setValue(java.time.LocalDate.parse(missao.getDataTermino()));
            statusBox.setValue(missao.getStatus());
            descricaoArea.setText(missao.getDescricao());
        }
    }

    @FXML
    private void salvar() {
        Missao nova = new Missao(
                editando ? missao.getId() : "0",
                tipoBox.getValue(),
                localField.getText(),
                inicioPicker.getValue().toString(),
                terminoPicker.getValue().toString(),
                statusBox.getValue(),
                descricaoArea.getText()
        );
        if (editando) {
            missaoDAO.atualizar(nova);
        } else {
            int novoId = missaoDAO.inserir(nova);
            if (novoId == -1) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Aviso");
                alerta.setHeaderText(null);
                alerta.setContentText("Erro ao obter o ID da nova missão!");
                alerta.showAndWait();
                return;
            }
            militarMissaoDAO.vincular(militarSaram, String.valueOf(novoId));
        }
        fechar();
    }



    @FXML
    private void cancelar() {
        fechar();
    }

    private void fechar() {
        Stage stage = (Stage) tipoBox.getScene().getWindow();
        stage.close();
    }

    private void showAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
