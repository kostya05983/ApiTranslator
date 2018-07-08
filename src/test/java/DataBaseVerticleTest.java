import JsonObjects.Word;
import com.google.gson.Gson;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import verticles.AddressEventBus;
import verticles.DataBaseVerticle;

@RunWith(VertxUnitRunner.class)
public class DataBaseVerticleTest {

    private Vertx vertx = Vertx.vertx();

    @Before
    public void setUp(TestContext context) {
        vertx.deployVerticle(DataBaseVerticle.class.getName(),context.asyncAssertSuccess());

    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testInsert(TestContext context) {
        var async = context.async();
        vertx.eventBus().send(AddressEventBus.addDictionaryRecord.name(),new Gson().toJson(new Word("cat","кошка")),reply -> {
            if(reply.succeeded())
                async.complete();
        });
    }

    @Test
    public void testGetTranslation(TestContext context) {
        var async = context.async();

        vertx.eventBus().send(AddressEventBus.getTranslation.name(),"word", reply -> {
           if(reply.succeeded()) {
               System.out.println(reply.result().body());
               async.complete();
           }
        });
    }


}
