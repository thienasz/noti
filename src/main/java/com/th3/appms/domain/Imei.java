package com.th3.appms.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Imei.
 */
@Entity
@Table(name = "imei")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Imei implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "imei", nullable = false)
    private String imei;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "imei_type",
               joinColumns = @JoinColumn(name="imeis_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="types_id", referencedColumnName="ID"))
    private Set<Type> types = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImei() {
        return imei;
    }

    public Imei imei(String imei) {
        this.imei = imei;
        return this;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getEmail() {
        return email;
    }

    public Imei email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public Imei phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Type> getTypes() {
        return types;
    }

    public Imei types(Set<Type> types) {
        this.types = types;
        return this;
    }

    public Imei addType(Type type) {
        types.add(type);
        type.getImeis().add(this);
        return this;
    }

    public Imei removeType(Type type) {
        types.remove(type);
        type.getImeis().remove(this);
        return this;
    }

    public void setTypes(Set<Type> types) {
        this.types = types;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Imei imei = (Imei) o;
        if(imei.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, imei.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Imei{" +
            "id=" + id +
            ", imei='" + imei + "'" +
            ", email='" + email + "'" +
            ", phone='" + phone + "'" +
            '}';
    }
}
