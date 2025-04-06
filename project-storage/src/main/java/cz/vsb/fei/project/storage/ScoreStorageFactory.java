package cz.vsb.fei.project.storage;

public class ScoreStorageFactory {

    private static final BackendType BACKEND_TYPE = BackendType.JPA;

    public static ScoreStorageInterface createStorage() {
        return switch (BACKEND_TYPE) {
            case JPA -> new JpaConnector();
            case DB -> new DbConnector();
            case FILE -> new FileManager();
        };
    }
}
// TODO: V budoucnu nacitat typ z konfigurace nebo predat jako parametr
// switch enum db jpa file
