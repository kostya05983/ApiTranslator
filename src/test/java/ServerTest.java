import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.WebClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import verticles.DataBaseVerticle;
import verticles.HttpVerticle;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


@RunWith(VertxUnitRunner.class)
public class ServerTest {
    private Vertx vertx;

    private Logger logger = LogManager.getLogger(ServerTest.class);


    @Before
    public void setUp(TestContext context) {
        vertx = Vertx.vertx();
        vertx.deployVerticle(DataBaseVerticle.class.getName(), context.asyncAssertSuccess());
        vertx.deployVerticle(HttpVerticle.class.getName(), context.asyncAssertSuccess());

    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testGetTranslation(TestContext context) {
        final Async async = context.async();

        WebClient client = WebClient.create(vertx);

        client.get(9090, "localhost", "/translate?word=word&key=j").send(ar -> {
            if (ar.succeeded()) {
                System.out.println("got http response");
                async.complete();

            } else {
                System.out.println("I don't get http response");
            }
        });
    }


}
