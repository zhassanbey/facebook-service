/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.GetResponse;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import kz.alem.semantics.facebook.service.leaderank.model.UserNode;
import kz.alem.semantics.facebook.service.leaderank.queue.RabbitQueue;
import kz.alem.semantics.leaderank.leaderank.orm.model.LeaderRank;
import kz.alem.semantics.sql.orm.model.Bot;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;

/**
 *
 * @author Zhasan
 */
@Ignore
@RunWith(MockitoJUnitRunner.class)
public class RabbitQueueTest {

    RabbitQueue queue;
    UserNode node;

    public RabbitQueueTest() {
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        queue = new RabbitQueue();

        node = new UserNode();
        Bot bot = new Bot();
        bot.setUsername("sefsefsef");
        bot.setPassword("sefsefsefs");
        bot.setLogin("sefsefsef");
        bot.setDomain("efsefsef");
        node.setBot(bot);
        node.setDepth(2);

        LeaderRank user = new LeaderRank();
        user.setDomain("sefsefsefsef");
        user.setRank(3);
        user.setUsername("sefsefsefsef");
        node.setUser(user);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void addTest() throws IOException, TimeoutException {
        byte[] arr = "test message".getBytes();

        Channel channel = Mockito.mock(Channel.class);

        queue.setChannel(channel);

        queue.add(node);

        Mockito.verify(channel).basicPublish("nodes", "", null, node.toByteArray());

    }

    @Test
    public void removeTest() throws IOException, InterruptedException, TimeoutException, ClassNotFoundException {
        Channel channel = Mockito.mock(Channel.class);

        byte[] arr = node.toByteArray();

        queue.setChannel(channel);

        GetResponse response = Mockito.mock(GetResponse.class);
        Envelope env = Mockito.mock(Envelope.class);
        
        Mockito.when(channel.basicGet(Mockito.any(String.class), Mockito.any(Boolean.class))).thenReturn(response);

        Mockito.when(response.getEnvelope()).thenReturn(env);
        
        Mockito.when(env.getDeliveryTag()).thenReturn(0L);
        
        Mockito.when(response.getBody()).thenReturn(node.toByteArray());
       
        UserNode x = queue.remove();
        
        Mockito.verify(channel).basicAck(0L, false);
        
        assertEquals(x.toByteArray().length, arr.length);

    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
