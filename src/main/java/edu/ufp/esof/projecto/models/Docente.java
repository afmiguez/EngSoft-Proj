package edu.ufp.esof.projecto.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
public class Docente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String code;

    @OneToMany(mappedBy = "docente",cascade = CascadeType.PERSIST)
    @JsonManagedReference
    private Set<Componente> componentes=new HashSet<>();





    public Docente(String name, String code) {
        this.setName(name);
        this.setCode(code);
    }

}