package edu.ufp.esof.projecto.models;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@JsonIdentityInfo(
        generator= ObjectIdGenerators.PropertyGenerator.class,
        property="id")
@NoArgsConstructor
public class Cadeira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String designation;

    @Column(unique = true)
    private String code;

    @OneToMany(cascade = CascadeType.PERSIST)
    //@JsonManagedReference
    @JsonIgnore
    private Set<Oferta> ofertas = new HashSet<>();

    @OneToMany(cascade = CascadeType.PERSIST)
    //@JsonManagedReference
    private Set<Escala> escalas = new HashSet<>();

    public Cadeira(String designation, String code){
        this.setDesignation(designation);
        this.setCode(code);
    }

    public void changeDesignation(String designation){
        setDesignation(designation);
    }

    public void changeCode(String code){
        setCode(code);
    }

    public void addOferta(Oferta offer){
        if (addCheckOferta(offer))
            ofertas.add(offer);
    }

    public Oferta removeOferta(int id){
        for (Oferta o : ofertas) {
            if (o.getId().equals(id)){
                ofertas.remove(o);
                return o;
            }
        }
        return null;
    }

    private boolean offerBelongsToDiscipline(Oferta offer){
        return offer.getCadeira().getId().equals(id);
    }

    private boolean addCheckOferta(Oferta offer){
        if (offerBelongsToDiscipline(offer)){
            for (Oferta o : ofertas) {
                if(o.getAno().equals(offer.getAno()))
                    return false;
            }
        }
        return true;
    }

    public void addEscala(Escala escala) {
        if (addCheckEscala(escala)){
            escalas.add(escala);
        }
    }

    private boolean addCheckEscala(Escala escala) {
        for (Escala e: escalas) {
            if(e.getDesignation().equals(escala.getDesignation())){
                return false;
            }
        }
        return true;
    }
}
