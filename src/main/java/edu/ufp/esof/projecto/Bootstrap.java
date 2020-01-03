package edu.ufp.esof.projecto;

import edu.ufp.esof.projecto.models.Aluno;
import edu.ufp.esof.projecto.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
//@Transactional
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {


    private AlunoRepo alunoRepo;
    private CadeiraRepo cadeiraRepo;
    private ComponenteRepo componenteRepo;
    private CriterioRepo criterioRepo;
    private DocenteRepo docenteRepo;
    private MomentoRealizadoRepo momentoRealizadoRepo;
    private MomentoRepo momentoRepo;
    private OfertaRepo ofertaRepo;
    private QuestaoRepo questaoRepo;
    private QuestaoRespondidaRepo questaoRespondidaRepo;
    private ResultadoAprendizagemRepo resultadoAprendizagemRepo;


    @Autowired
    public Bootstrap(AlunoRepo alunoRepo, CadeiraRepo cadeiraRepo,
                     ComponenteRepo componenteRepo, CriterioRepo criterioRepo,
                     DocenteRepo docenteRepo, MomentoRealizadoRepo momentoRealizadoRepo,
                     MomentoRepo momentoRepo, OfertaRepo ofertaRepo,
                     QuestaoRepo questaoRepo, QuestaoRespondidaRepo questaoRespondidaRepo,
                     ResultadoAprendizagemRepo resultadoAprendizagemRepo) {

        this.alunoRepo = alunoRepo;
        this.cadeiraRepo = cadeiraRepo;
        this.componenteRepo = componenteRepo;
        this.criterioRepo = criterioRepo;
        this.docenteRepo = docenteRepo;
        this.momentoRealizadoRepo = momentoRealizadoRepo;
        this.momentoRepo = momentoRepo;
        this.ofertaRepo = ofertaRepo;
        this.questaoRepo = questaoRepo;
        this.questaoRespondidaRepo = questaoRespondidaRepo;
        this.resultadoAprendizagemRepo = resultadoAprendizagemRepo;
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        logger.debug("Startup");

        Aluno aluno1 = new Aluno("João", "37190");
        Aluno aluno2 = new Aluno("Luísa", "37151");
        Aluno aluno3 = new Aluno("Rodrigo", "36824");
        Aluno aluno4 = new Aluno("Quim Zé", "36824");

        alunoRepo.save(aluno1);
        alunoRepo.save(aluno2);
        alunoRepo.save(aluno3);
        /*
        try {
            alunoRepo.save(aluno4);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        */
        aluno1.setCode("18");
        alunoRepo.save(aluno1);
    }
}