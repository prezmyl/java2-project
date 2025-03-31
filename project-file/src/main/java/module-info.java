module cz.vsb.fei.project.file {
    requires org.apache.logging.log4j;
	requires static lombok;
	requires java.sql;
	requires cz.vsb.fei.project.data;
	// Žádné requires na Lombok ani Log4j, protože nejsou modulární
	exports cz.vsb.fei.project.file;
}
