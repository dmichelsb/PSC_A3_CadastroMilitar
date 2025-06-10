package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Militar {
    private final StringProperty saram;
    private final StringProperty nomeCompleto;
    private final StringProperty posto;
    private final StringProperty dataAdmissao;
    private final StringProperty cpf;
    private final StringProperty sexo;
    private final StringProperty dataNascimento;
    private final StringProperty naturalidade;
    private final StringProperty quadro;
    private final StringProperty unidade;
    private final StringProperty situacaoAtual;

    public Militar(String saram, String nomeCompleto, String posto, String dataAdmissao,
                   String cpf, String sexo, String dataNascimento, String naturalidade,
                   String quadro, String unidade, String situacaoAtual) {
        this.saram = new SimpleStringProperty(saram);
        this.nomeCompleto = new SimpleStringProperty(nomeCompleto);
        this.posto = new SimpleStringProperty(posto);
        this.dataAdmissao = new SimpleStringProperty(dataAdmissao);
        this.cpf = new SimpleStringProperty(cpf);
        this.sexo = new SimpleStringProperty(sexo);
        this.dataNascimento = new SimpleStringProperty(dataNascimento);
        this.naturalidade = new SimpleStringProperty(naturalidade);
        this.quadro = new SimpleStringProperty(quadro);
        this.unidade = new SimpleStringProperty(unidade);
        this.situacaoAtual = new SimpleStringProperty(situacaoAtual);
    }

    public String getSaram() { return saram.get(); }
    public StringProperty saramProperty() { return saram; }

    public String getNomeCompleto() { return nomeCompleto.get(); }
    public StringProperty nomeCompletoProperty() { return nomeCompleto; }

    public String getPosto() { return posto.get(); }
    public StringProperty postoProperty() { return posto; }

    public String getDataAdmissao() { return dataAdmissao.get(); }
    public StringProperty dataAdmissaoProperty() { return dataAdmissao; }

    public String getCpf() { return cpf.get(); }
    public StringProperty cpfProperty() { return cpf; }

    public String getSexo() { return sexo.get(); }
    public StringProperty sexoProperty() { return sexo; }

    public String getDataNascimento() { return dataNascimento.get(); }
    public StringProperty dataNascimentoProperty() { return dataNascimento; }

    public String getNaturalidade() { return naturalidade.get(); }
    public StringProperty naturalidadeProperty() { return naturalidade; }

    public String getQuadro() { return quadro.get(); }
    public StringProperty quadroProperty() { return quadro; }

    public String getUnidade() { return unidade.get(); }
    public StringProperty unidadeProperty() { return unidade; }

    public String getSituacaoAtual() { return situacaoAtual.get(); }
    public StringProperty situacaoAtualProperty() { return situacaoAtual; }
}
