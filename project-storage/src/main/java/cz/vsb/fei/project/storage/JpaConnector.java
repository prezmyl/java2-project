package cz.vsb.fei.project.storage;

import cz.vsb.fei.project.data.Score;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

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
        tx.begin();
        em.persist(score);
        tx.commit();
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
