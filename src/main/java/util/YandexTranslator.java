package util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class YandexTranslator implements AutoCloseable {
    private HttpURLConnection connection;
    private InputStream response;
    private final String request = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20180401T165610Z.f598ccd9057c7087.721768a02c598f729c32b1b5956ebc7167ed6e70";


    public String translate(String word, String lang) {
        if (word == null || lang == null) {
            return null;
        }

        try {
            var url = new URL(request);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            var dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes("text=" + URLEncoder.encode(word, "UTF-8") + "&lang=" + lang);
            response = connection.getInputStream();
            var scanner = new Scanner(response);
            var json = scanner.nextLine();
            return json.substring(json.indexOf("[") + 2, json.indexOf("]") - 1);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void close() throws Exception {
        if (response != null)
            response.close();
    }
}
