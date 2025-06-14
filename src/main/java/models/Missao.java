package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Missao {

    private final StringProperty id; // se for auto-increment, use String ou int
    private final StringProperty tipo;
    private final StringProperty local;
    private final StringProperty dataInicio;
    private final StringProperty dataTermino;
    private final StringProperty status;
    private final StringProperty descricao;

    public Missao(String id, String tipo, String local, String dataInicio, String dataTermino, String status, String descricao) {
        this.id = new SimpleStringProperty(id);
        this.tipo = new SimpleStringProperty(tipo);
        this.local = new SimpleStringProperty(local);
        this.dataInicio = new SimpleStringProperty(dataInicio);
        this.dataTermino = new SimpleStringProperty(dataTermino);
        this.status = new SimpleStringProperty(status);
        this.descricao = new SimpleStringProperty(descricao);
    }

    public String getId() { return id.get(); }
    public StringProperty idProperty() { return id; }

    public String getTipo() { return tipo.get(); }
    public StringProperty tipoProperty() { return tipo; }

    public String getLocal() { return local.get(); }
    public StringProperty localProperty() { return local; }

    public String getDataInicio() { return dataInicio.get(); }
    public StringProperty dataInicioProperty() { return dataInicio; }

    public String getDataTermino() { return dataTermino.get(); }
    public StringProperty dataTerminoProperty() { return dataTermino; }

    public String getStatus() { return status.get(); }
    public StringProperty statusProperty() { return status; }

    public String getDescricao() { return descricao.get(); }
    public StringProperty descricaoProperty() { return descricao; }
}
