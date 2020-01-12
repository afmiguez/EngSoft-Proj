package edu.ufp.esof.projecto.services;

import edu.ufp.esof.projecto.models.Questao;
import edu.ufp.esof.projecto.models.QuestaoRespondida;
import edu.ufp.esof.projecto.repositories.QuestaoRespondidaRepo;
import edu.ufp.esof.projecto.repositories.QuestaoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class QuestaoService {

    private QuestaoRepo questaoRepo;
    private QuestaoRespondidaRepo questaoRespondidaRepo;
    private QuestaoRespondidaService questaoRespondidaService;

    @Autowired
    public QuestaoService(QuestaoRepo questaoRepo, QuestaoRespondidaRepo questaoRespondidaRepo, QuestaoRespondidaService questaoRespondidaService) {
        this.questaoRepo = questaoRepo;
        this.questaoRespondidaRepo = questaoRespondidaRepo;
        this.questaoRespondidaService = questaoRespondidaService;
    }

    public Set<Questao> findAll(){
        Set<Questao> questoes=new HashSet<>();
        for(Questao q:this.questaoRepo.findAll()){
            questoes.add(q);
        }
        return questoes;
    }

    public Optional<Questao> findByDesignation(String designation){
        Optional<Questao> optionalQuestao=Optional.empty();
        for (Questao q:this.questaoRepo.findAll()) {
            if(q.getDesignation().compareTo(designation)==0){
                optionalQuestao=Optional.of(q);
                break;
            }
        }
        return optionalQuestao;
    }

    public Set<QuestaoRespondida> findAllByQuestao(Questao questao){
        Set<QuestaoRespondida> questoesRespondidas=new HashSet<>();
        for (QuestaoRespondida qr:this.questaoRespondidaRepo.findAll()) {
            if(qr.getQuestao().getId().compareTo(questao.getId())==0){
                questoesRespondidas.add(qr);
            }
        }
        return questoesRespondidas;
    }


    public Optional<Questao> createQuestao(Questao questao){
        Optional<Questao> optionalQuestao=this.questaoRepo.findByDesignation(questao.getDesignation());
        if(optionalQuestao.isPresent()){
            return Optional.empty();
        }
        Questao createdQuestao=this.questaoRepo.save(questao);
        return Optional.of(createdQuestao);
    }


    public Optional<Questao> updateQuestao(String designation, Questao questao){
        Optional<Questao> optionalQuestao=this.questaoRepo.findByDesignation(designation);
        if(optionalQuestao.isPresent()){
            questaoRepo.save(questao);
            return optionalQuestao;
        }
        return Optional.empty();
    }

    // falta fazer
    /**
     * Apaga todas as questoes respondidas associadas a uma questão até se apagar a si
     * @param designation designação da questao a apagar
     * @return retorna falso se questao não existir
     */
    public Boolean deleteQuestao(String designation){
        Optional<Questao> optionalQuestao=this.questaoRepo.findByDesignation(designation);
        if(optionalQuestao.isPresent()){
            for (QuestaoRespondida qr:this.findAllByQuestao(optionalQuestao.get())) {
                questaoRespondidaRepo.delete(qr);
            }
            questaoRepo.delete(optionalQuestao.get());
            return true;
        }
        return false;
    }

    // falta fazer
    public void deleteAll(){
        for (Questao q:this.questaoRepo.findAll()) {
            deleteQuestao(q.getDesignation());
        }
    }

    public void delete(Questao q){
        if (q.getMomento() != null){
            q.getMomento().getQuestoes().remove(q);
            q.setMomento(null);
        }
        if (q.getRa() != null){
            q.getRa().getQuestoes().remove(q);
            q.setRa(null);
        }
        Optional<Iterable<QuestaoRespondida>> optionalQuestaoRespondidas = questaoRespondidaRepo.findAllByQuestao(q);
        if (optionalQuestaoRespondidas.isPresent()){
            for (QuestaoRespondida qr : optionalQuestaoRespondidas.get()) {
                questaoRespondidaService.delete(qr);
            }
        }
        questaoRepo.delete(q);
    }
}
