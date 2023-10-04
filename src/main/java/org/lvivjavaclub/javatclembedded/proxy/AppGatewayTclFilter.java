package org.lvivjavaclub.javatclembedded.proxy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import tcl.lang.Interp;
import tcl.lang.TCL;
import tcl.lang.TclException;

import java.util.Iterator;

@Slf4j
@RestController
@SpringBootApplication
public class AppGatewayTclFilter implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(AppGatewayTclFilter.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("TCL Interpreter started.");
    }

    @PostMapping(path = "/", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public ResponseEntity<?> gateway(NativeWebRequest rst, ProxyExchange<?> proxy) {

        Interp interp = new Interp();

        try {


            Iterator<String> headers = rst.getHeaderNames();

            while(headers.hasNext()) {
                String headerName = headers.next();
                interp.setVar("headers", headerName, rst.getHeader(headerName), TCL.GLOBAL_ONLY);
            }

            interp.evalFile("./tcl/filter.tcl");

            if (interp.getVar("result", TCL.GLOBAL_ONLY).toString().equals("pass")) {
                return proxy.uri("http://localhost:8081/").post();
            }

        } catch (TclException e) {
            switch (e.getCompletionCode()) {
                case TCL.ERROR -> System.out.printf("%d: %s", interp.getErrorLine(), e.getMessage());
                default -> System.out.println("I don't know what's going on.");
            }
        } finally {
            interp.dispose();
        }

        return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("authorization required");
    }
}
