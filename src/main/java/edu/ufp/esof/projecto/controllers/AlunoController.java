package edu.ufp.esof.projecto.controllers;

import edu.ufp.esof.projecto.models.Aluno;
import edu.ufp.esof.projecto.models.Componente;
import edu.ufp.esof.projecto.models.Escala;
import edu.ufp.esof.projecto.models.NotaRequest;
import edu.ufp.esof.projecto.services.AlunoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/aluno")
public class AlunoController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private AlunoService alunoService;

    @Autowired
    public AlunoController(AlunoService alunoService){
        this.alunoService = alunoService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<Aluno>> getAllAlunos(){
        this.logger.info("Listing Request for alunos");

        return ResponseEntity.ok(this.alunoService.findAll());
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ResponseEntity<Aluno> getAlunoById(@PathVariable("id") String id) throws NoAlunoExcpetion {
        this.logger.info("Listing Request for aluno with id " + id);

        Optional<Aluno> optionalAluno =this.alunoService.findByNumber(id);
        if(optionalAluno.isPresent()) {
            return ResponseEntity.ok(optionalAluno.get());
        }
        throw new NoAlunoExcpetion(id);
    }

    //@PutMapping(value = "{id}")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    //@PutMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    //@PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Aluno>editAluno(@PathVariable("id") String id, @RequestBody Aluno aluno) throws NoAlunoExcpetion{
        this.logger.info("Update Request for aluno with id " + id);

        Optional<Aluno> optionalAluno =this.alunoService.updateAluno(id, aluno);
        if(optionalAluno.isPresent()) {
            return ResponseEntity.ok(optionalAluno.get());
        }
        throw new NoAlunoExcpetion(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAllAlunos(){
        this.logger.info("Delete Request for every aluno");

        this.alunoService.deleteAll();
        return ResponseEntity.ok("Deleted every aluno");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAluno(@PathVariable("id") String id) throws NoAlunoExcpetion{
        this.logger.info("Delete Request for aluno with id " + id);

        Boolean deleted = this.alunoService.deleteAluno(id);
        if(deleted) {
            return ResponseEntity.ok("Delete aluno with id = " + id);
        }
        throw new NoAlunoExcpetion(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Aluno> createAluno(@RequestBody Aluno aluno){
        this.logger.info("Create Aluno Request with name = " + aluno.getName() + " and code = " + aluno.getCode());

        Optional<Aluno> alunoOptional=this.alunoService.createAluno(aluno);
        if(alunoOptional.isPresent()){
            return ResponseEntity.ok(alunoOptional.get());
        }
        throw new AlunoAlreadyExistsExcpetion(aluno.getName());
    }




    //-------------------------------------------//
    //             COMPONENT RELATED             //
    //-------------------------------------------//



    @RequestMapping(value = "/{id}/componentes",method = RequestMethod.GET)
    public ResponseEntity<Set<Componente>> getComponentesFromAluno(@PathVariable("id") String id) throws NoAlunoExcpetion {
        this.logger.info("Listing Request for componentes from aluno with id " + id);

        Optional<Set<Componente>> optionalComponenteSet =this.alunoService.findComponentes(id);
        if(optionalComponenteSet.isPresent()) {
            return ResponseEntity.ok(optionalComponenteSet.get());
        }
        throw new NoAlunoExcpetion(id);
    }

    // necessita do id da componente no body
    @PostMapping(value = "/{id}/associate", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Aluno> associateAluno(@PathVariable("id") String id, @RequestBody Componente c){
        this.logger.info("Associate Aluno with code = " + id);

        Optional<Aluno> alunoOptional=this.alunoService.associateAluno(id, c);
        if(alunoOptional.isPresent()){
            return ResponseEntity.ok(alunoOptional.get());
        }
        throw new NoAlunoExcpetion(id);
    }

    @RequestMapping(value = "/{id}/{componenteId}", method = RequestMethod.DELETE)
    public ResponseEntity<Aluno> desassociateAluno(@PathVariable("id") String id, @PathVariable("componenteId") Long componenteId) throws NoAlunoExcpetion{
        this.logger.info("Desassociate Request for aluno with id " + id);

        Optional<Aluno> alunoOptional=this.alunoService.desassociateAluno(id, componenteId);
        if(alunoOptional.isPresent()){
            return ResponseEntity.ok(alunoOptional.get());
        }
        throw new NoAlunoExcpetion(id);
    }




    //-------------------------------------------//
    //               NOTAS RELATED               //
    //-------------------------------------------//




    @RequestMapping(value = "/notasRA/{cadeira}",method = RequestMethod.GET)
    public ResponseEntity<Set<Escala>> getNotasRaOfComponenteFromAluno(@RequestBody NotaRequest nr, @PathVariable("cadeira") String cadeira) throws NoAlunoExcpetion {
        this.logger.info("Listing Request for notasRA");

        Optional<Set<Escala>> optionalNotas =this.alunoService.notasRaCadeira(nr.getAlunoNumero(),cadeira,nr.getAno(),nr.getComponente(),0);
        if(optionalNotas.isPresent()) {
            return ResponseEntity.ok(optionalNotas.get());
        }
        throw new NoAlunoExcpetion(nr.getAlunoNumero());
    }

    @RequestMapping(value = "/notasRA",method = RequestMethod.GET)
    public ResponseEntity<Set<Set<Escala>>> getNotasRaFromAluno(@RequestBody NotaRequest nr) throws NoAlunoExcpetion {
        this.logger.info("Listing Request for notasRA");

        Optional<Set<Set<Escala>>> optionalNotas =this.alunoService.notasRa(nr.getAlunoNumero());
        if(optionalNotas.isPresent()) {
            return ResponseEntity.ok(optionalNotas.get());
        }
        throw new NoAlunoExcpetion(nr.getAlunoNumero());
    }

    @RequestMapping(value = "notas/{cadeira}",method = RequestMethod.GET)
    public ResponseEntity<Set<Escala>> getNotasOfComponenteFromAluno(@RequestBody NotaRequest nr, @PathVariable("cadeira") String cadeira) throws NoAlunoExcpetion {
        this.logger.info("Listing Request for notas");

        Optional<Set<Escala>> optionalNotas =this.alunoService.notasCadeira(nr.getAlunoNumero(),cadeira,nr.getAno(),nr.getComponente(),0);
        if(optionalNotas.isPresent()) {
            return ResponseEntity.ok(optionalNotas.get());
        }
        throw new NoAlunoExcpetion(nr.getAlunoNumero());
    }

    @RequestMapping(value = "/notas",method = RequestMethod.GET)
    public ResponseEntity<Set<Set<Escala>>> getNotasFromAluno(@RequestBody NotaRequest nr) throws NoAlunoExcpetion {
        this.logger.info("Listing Request for notas");

        Optional<Set<Set<Escala>>> optionalNotas =this.alunoService.notas(nr.getAlunoNumero());
        if(optionalNotas.isPresent()) {
            return ResponseEntity.ok(optionalNotas.get());
        }
        throw new NoAlunoExcpetion(nr.getAlunoNumero());
    }

    @RequestMapping(value = "/notasFinais",method = RequestMethod.GET)
    public ResponseEntity<Set<Escala>> getNotasFinaisFromAluno(@RequestBody NotaRequest nr) throws NoAlunoExcpetion {
        this.logger.info("Listing Request for notas");

        Optional<Set<Escala>> optionalNotas =this.alunoService.notasFinais(nr.getAlunoNumero());
        if(optionalNotas.isPresent()) {
            return ResponseEntity.ok(optionalNotas.get());
        }
        throw new NoAlunoExcpetion(nr.getAlunoNumero());
    }




    //-------------------------------------------//
    //                 EXCEPTIONS                //
    //-------------------------------------------//




    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Aluno não existente")
    private static class NoAlunoExcpetion extends RuntimeException {

        private NoAlunoExcpetion(String id) {
            super("Aluno com id " + id + " nao existente");
        }
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Aluno já existente")
    private static class AlunoAlreadyExistsExcpetion extends RuntimeException {

        public AlunoAlreadyExistsExcpetion(String name) {
            super("Aluno com nome: " + name + " já existe");
        }
    }
}