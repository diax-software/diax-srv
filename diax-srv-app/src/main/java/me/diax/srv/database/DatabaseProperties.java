package me.diax.srv.database;

import com.knockturnmc.api.util.NamedProperties;
import com.knockturnmc.api.util.Property;
import lombok.Getter;

final class DatabaseProperties extends NamedProperties {

    @Getter
    @Property(
            value = "hikari.dataSource.url",
            defaultvalue = "jdbc:mysql://localhost/diax?useTimeZone=true&noDatetimeStringSync=true&useSSL=false"
    )
    private String datasourceUrl;

    @Getter
    @Property(value = "hikari.dataSource.user", defaultvalue = "diax")
    private String datasourceUser;

    @Getter
    @Property(value = "hikari.dataSource.password", defaultvalue = "diax")
    private String datasourcePassword;

    @Getter
    @Property(value = "hikari.connectionInitSql", defaultvalue = "SET TIME_ZONE = '+00:00';")
    private String connectionInitSql;

}
