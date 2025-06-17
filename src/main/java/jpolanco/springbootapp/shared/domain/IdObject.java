package jpolanco.springbootapp.shared.domain;

public abstract class IdObject {
    protected final String value;
    protected IdObject(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
