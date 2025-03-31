package cz.vsb.fei.project.file;

public class ScoreStorageFactory {
    public static ScoreStorageInterface createStorage() {
        return new DbConnector();
    }
}
// TODO: V budoucnu nacitat typ z konfigurace nebo predat jako parametr
// switch enum db jpa file
