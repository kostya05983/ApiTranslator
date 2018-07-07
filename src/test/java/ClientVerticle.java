import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.client.WebClient;

public class ClientVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {


        startFuture.complete();


    }
}
