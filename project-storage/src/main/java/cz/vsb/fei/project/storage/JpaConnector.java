package cz.vsb.fei.project.storage;

import cz.vsb.fei.project.data.GameSessionEntity;
import cz.vsb.fei.project.data.Score;
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
    public void insertScore(Score score) {
        EntityTransaction tx = em.getTransaction();
        try {
            if (!tx.isActive()) {
                tx.begin();
            }

            GameSessionEntity session = new GameSessionEntity();
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
    public List<Score> getAll() {
        return em.createQuery("SELECT s FROM Score s", Score.class).getResultList();
    }

    @Override
    public List<Score> getTopScores(int number) {
        return em.createQuery("SELECT s FROM Score s ORDER BY s.points DESC", Score.class)
                .setMaxResults(number)
                .getResultList();
    }

    @Override
    public Score getScoreByName(String name) {
        return em.createQuery("SELECT s FROM Score s WHERE s.nick = :name", Score.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}
