package br.com.rhscdeveloper.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

@MappedSuperclass
public abstract class BaseVO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Version
    @Column(nullable = false)
    protected LocalDateTime versao;

    public LocalDateTime getVersao() {
        return versao;
    }

    public void setVersao(LocalDateTime versao) {
        this.versao = versao;
    }

    public abstract Object getId();
}