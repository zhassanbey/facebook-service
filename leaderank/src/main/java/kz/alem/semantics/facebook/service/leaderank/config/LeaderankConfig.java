/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.leaderank.config;

import java.io.InputStream;
import java.util.Properties;
import javax.sql.DataSource;
import kz.alem.semantics.facebook.service.leaderank.DynamicLeaderRank;
import kz.alem.semantics.leaderank.leaderank.orm.dao.LeaderRankDao;
import kz.alem.semantics.leaderank.leaderank.orm.dao.impl.LeaderRankDaoImpl;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Zhasan
 */
public class LeaderankConfig {

    @Bean
    public LeaderRankDao getLeaderRankDao() {
        return new LeaderRankDaoImpl();
    }

    @Bean
    public DataSource getDataSource() {
        PGPoolingDataSource ds = new PGPoolingDataSource();
        String serverName = null;
        try {
            Properties props = new Properties();
            InputStream is = DynamicLeaderRank.class.getClassLoader().getResourceAsStream("datasource.properties");
            props.load(is);
            serverName = props.getProperty("leaderank.db.server");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ds.setServerName(serverName);
        ds.setDatabaseName("leaderankdb");
        ds.setUser("postgres");
        ds.setPassword("1234567890");
        ds.setPortNumber(5432);
        return ds;
    }

    @Bean
    public JdbcTemplate getJdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        return jdbcTemplate;
    }

}
