package verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpVerticle extends AbstractVerticle {

    private final Logger logger = LogManager.getLogger(HttpVerticle.class);
    private final int PORT = 9090;



    @Override
    public void start(Future<Void> startFuture) throws Exception {
        var options = new HttpServerOptions().setPort(PORT).setHost("localhost");

        HttpServer server = vertx.createHttpServer(options);

        Router router = Router.router(vertx);
        router.get("/translate").handler(this::getTranslation);

        server.requestHandler(router::accept)
                .listen(PORT, result -> {
                    if (result.succeeded()) {
                        logger.debug("server listen port");
                        startFuture.complete();
                    } else {
                        logger.debug("server failed");
                        startFuture.fail(result.cause());
                    }
                });
    }

    private void getTranslation(RoutingContext context) {
        final var response = context.response();
        final var key = context.request().getParam("key");

        if(key!=null) {
            //check key in database
            logger.debug(AddressEventBus.key.name()+"lfsd"+AddressEventBus.key.toString());
            vertx.eventBus().send(AddressEventBus.key.name(),key, reply -> {
               if(reply.succeeded()) {
                   logger.debug("key verification success {''}",key);

                   final var word = context.request().getParam("word");
                   vertx.eventBus().send(AddressEventBus.addDictionaryRecord.name(),word, translation -> {
                        if(translation.succeeded()) {
                            response.putHeader("content-type","application/json").end(translation.result().body().toString());
                        } else {
                            logger.debug("Don't get translation for word {''}",word);
                        }
                   });
               } else {
                   logger.debug("key verification failed key is incorrect");
                   response.reset();
               }
            });
        } else {
            logger.debug("key is null");
            response.reset();
        }
    }
}
