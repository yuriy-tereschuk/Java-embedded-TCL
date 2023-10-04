package org.lvivjavaclub.javatclembedded.send;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.CharBuffer;

@Slf4j
@SpringBootApplication
public class AppSend {
    public static void main(String[] args) {
        SpringApplication.run(AppSend.class, args);
    }

    @ShellComponent
    public static class ShellCommands {

        @ShellMethod(key = "send ok", value = "Add authorization ok header in request.")
        public String sendOkRequest(@ShellOption(defaultValue = "Ok") String auth) throws Exception {
            return sendRequest(auth);
        }

        @ShellMethod(key = "send fail", value = "Add authorization fail header in request")
        public String sendFailRequest(@ShellOption(defaultValue = "Fail") String auth) throws Exception {
            return sendRequest(auth);
        }
    }

    @Component
    class ControllerPortConfig implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

        @Override
        public void customize(ConfigurableWebServerFactory factory) {
            factory.setPort(8082);
        }
    }

    private static String sendRequest(String key) throws IOException {
        URL url = new URL("http://localhost:8080");
        URLConnection connection = url.openConnection();
        HttpURLConnection http= (HttpURLConnection) connection;

        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setDoInput(true);
        http.addRequestProperty("Content-Type", "text/plain");
        http.addRequestProperty("Authorized", key);
        http.connect();

        try (Writer w = new OutputStreamWriter(http.getOutputStream())) {
            w.write("proxies message");
        }

        CharBuffer buffer = CharBuffer.allocate(1000);

        try (Reader r = new BufferedReader(new InputStreamReader(http.getInputStream()))) {
            int ignore = r.read(buffer);
        }

        http.disconnect();

        return buffer.flip().toString();
    }
}
