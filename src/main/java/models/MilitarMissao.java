package models;

public class MilitarMissao {

    private int id; // PK da tabela de ligação
    private String militarSaram; // FK para Militar (usa SARAM como ID)
    private String missaoId;     // FK para Missao (usa ID da missão)

    public MilitarMissao(int id, String militarSaram, String missaoId) {
        this.id = id;
        this.militarSaram = militarSaram;
        this.missaoId = missaoId;
    }

    public int getId() {
        return id;
    }

    public String getMilitarSaram() {
        return militarSaram;
    }

    public String getMissaoId() {
        return missaoId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMilitarSaram(String militarSaram) {
        this.militarSaram = militarSaram;
    }

    public void setMissaoId(String missaoId) {
        this.missaoId = missaoId;
    }
}
