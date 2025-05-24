package cz.vsb.fei.project.storage;

import cz.vsb.fei.project.data.GameSessionDTO;
import cz.vsb.fei.project.data.ScoreDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class JpaConnector implements ScoreStorageInterface {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("java2");
    private final EntityManager em = emf.createEntityManager();

    public JpaConnector() {}

    @Override
    public void init() {

    }

    @Override
    public void insertScore(ScoreDTO score) {
        EntityTransaction tx = em.getTransaction();
        try {
            if (!tx.isActive()) {
                tx.begin();
            }

            GameSessionDTO session = new GameSessionDTO();
            session.setName(randomSessionName()); //pak random jmeno pres nejakou metodu

            score.setGameSession(session);
            session.getScores().add(score);
            em.persist(session);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            log.error("Error inserting score into database: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
    private String randomSessionName() {
        return "Session_" + System.currentTimeMillis();
    }


    @Override
    public List<ScoreDTO> getAll() {
        return em.createQuery("SELECT s FROM ScoreDTO s", ScoreDTO.class).getResultList();
    }

    @Override
    public List<ScoreDTO> getTopScores(int number) {
        return em.createQuery("SELECT s FROM ScoreDTO s ORDER BY s.points DESC", ScoreDTO.class)
                .setMaxResults(number)
                .getResultList();
    }

    @Override
    public ScoreDTO getScoreByName(String name) {
        return em.createQuery("SELECT s FROM ScoreDTO s WHERE s.nick = :name", ScoreDTO.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}
