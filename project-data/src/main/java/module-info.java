module cz.vsb.fei.project.data {
	requires static lombok;

	requires jakarta.persistence;
	requires org.hibernate.orm.core;

	opens cz.vsb.fei.project.data; // to specific pro vetsi bezpecnost
	requires org.apache.logging.log4j;
    requires jdk.jfr;
    requires java.desktop;
	exports cz.vsb.fei.project.data;
}