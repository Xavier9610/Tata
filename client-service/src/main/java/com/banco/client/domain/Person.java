package com.banco.client.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "personas")
@Inheritance(strategy = InheritanceType.JOINED)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre", nullable = false, length = 120)
    private String name;
    @Column(name = "genero")
    private String gender;
    @Column(name = "edad")
    private Integer age;
    @Column(name = "identificacion", nullable = false, unique = true, length = 40)
    private String identification;
    @Column(name = "direccion")
    private String address;
    @Column(name = "telefono")
    private String phone;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String v) {
        name = v;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String v) {
        gender = v;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer v) {
        age = v;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String v) {
        identification = v;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String v) {
        address = v;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String v) {
        phone = v;
    }
}
