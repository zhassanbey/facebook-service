/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.alem.semantics.facebook.service.leaderank.service;

import java.io.Serializable;
import java.util.List;
import javax.transaction.Transactional;
import kz.alem.semantics.sql.orm.dao.BotDao;
import kz.alem.semantics.sql.orm.model.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Zhasan
 */
@Service
public class BotService implements Serializable{
    
    @Autowired
    BotDao botDao;
    
    @Transactional
    public List<Bot> getBotList(){
        return botDao.getAll();
    }
    
    @Transactional
    public void saveBot(Bot bot){
        botDao.save(bot);
    }
    
}
