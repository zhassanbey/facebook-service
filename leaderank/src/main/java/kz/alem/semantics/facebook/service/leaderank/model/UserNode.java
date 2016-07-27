/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.alem.semantics.facebook.service.leaderank.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import kz.alem.semantics.leaderank.leaderank.orm.model.LeaderRank;
import kz.alem.semantics.sql.orm.model.Bot;

/**
 *
 * @author Zhasan
 */
public class UserNode implements Serializable{
    
    LeaderRank user;
    Bot bot;
    int depth;

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
    
    
    public LeaderRank getUser() {
        return user;
    }

    public void setUser(LeaderRank user) {
        this.user = user;
    }

    public Bot getBot() {
        return bot;
    }

    public void setBot(Bot bot) {
        this.bot = bot;
    }
    
    public byte[] toByteArray() throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out  = new ObjectOutputStream(bos);
        out.writeObject(this);
        
        byte [] result = bos.toByteArray();
        out.close();
        bos.close();
        return result;
    }
    
    public static UserNode fromBytes(byte[] arr) throws IOException, ClassNotFoundException{
        ByteArrayInputStream bis = new ByteArrayInputStream(arr);
        
        ObjectInput in = new ObjectInputStream(bis);
        
        Object obj = in.readObject();
        
        in.close();
        bis.close();
        
        return (UserNode)obj;
    }
    
    public String toString(){
        return "{\"user\":\""+user.toString()+"\",\"bot\":\""+bot.getUsername()+"\",\"depth\":"+depth+"}";
    }
}
