package verticles;

import JsonObjects.Word;
import com.google.gson.Gson;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DataBaseVerticle extends AbstractVerticle {

    private final String url = "jdbc:mysql://localhost/server_1?useUnicode=true&characterEncoding=utf-8";
    private final String user = "root";
    private final String password = "KarinaBatalova";
    private final Logger logger = LogManager.getLogger(DataBaseVerticle.class);

    @Override
    public void start(Future<Void> startFuture) throws Exception {


        vertx.eventBus().consumer(AddressEventBus.key.name(), message -> {
           logger.debug(message.body().toString());
           message.reply("lala");
        });

        vertx.eventBus().consumer(AddressEventBus.getTranslation.name(), message -> {
            try {
                final Connection connection = DriverManager.getConnection(url,user,password);
                var statement = connection.createStatement();
//                statement.execute("set character set utf8");
//                statement.execute("set names utf8");
                var resultSet = statement.executeQuery(String.format("SELECT * FROM Dictionary WHERE word = '%s';",message.body().toString()));
                if(resultSet.next())
                message.reply(resultSet.getString("translation"));

                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        vertx.eventBus().consumer(AddressEventBus.addDictionaryRecord.name(), message -> {
            try {
                Gson gson = new Gson();
                var record = gson.fromJson(message.body().toString(),Word.class);

                final Connection connection = DriverManager.getConnection(url,user,password);
                var statement = connection.createStatement();
                var resultSet = statement.executeQuery(String.format("SELECT * FROM Dictionary WHERE word = '%s';",record.getWord()));

                if(!resultSet.next())
                statement.executeUpdate(String.format("INSERT INTO Dictionary (word,translation) VALUES ('%s', '%s');",record.getWord(),record.getTranslation()));

                connection.close();

                message.reply("success");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });



        startFuture.complete();
    }


}
