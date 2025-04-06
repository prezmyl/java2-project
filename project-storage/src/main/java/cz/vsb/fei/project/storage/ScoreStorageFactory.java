package cz.vsb.fei.project.storage;

public class ScoreStorageFactory {
    public static DbConnector createStorage() {
        return new DbConnector();
    }
}
// TODO: V budoucnu nacitat typ z konfigurace nebo predat jako parametr
// switch enum db jpa file
