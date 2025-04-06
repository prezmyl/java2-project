module cz.vsb.fei.project.storage {
    requires org.apache.logging.log4j;
	requires static lombok;
	requires java.sql;
	requires cz.vsb.fei.project.data;
	//requires com.h2database;
	requires jakarta.persistence;
	requires org.hibernate.orm.core;
	requires static jakarta.annotation; // pro vygenerované modely


	// Žádné requires na Lombok ani Log4j, protože nejsou modulární
	exports cz.vsb.fei.project.storage;
}
