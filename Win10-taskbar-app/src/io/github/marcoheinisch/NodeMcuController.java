package io.github.marcoheinisch;

import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class NodeMcuController {
    private String ipAddress;
    private int requestCounter;

    private static final int REQUEST_LIMIT = 10;
    private static final String DEFAULT_IP = "192.168.0.92";

    public NodeMcuController(){
        this.requestCounter =0;
        this.ipAddress = (String) SaveVar.getThis("Ip");
    }

    String sendOrder(String argument, String order) {
        //Check for request-overflow to avoid bugs and erors
        if (this.requestCounter > REQUEST_LIMIT){
            return "overfolw";
        }
        this.requestCounter++;
        //Async task for http request
        return sendHttp(URI.create("http://"+ipAddress+"/do?"+argument+"="+order));
    }

    private static String sendHttp(URI uri){
        var client = HttpClient.newHttpClient();

        // create a request
        var request = HttpRequest.newBuilder(uri)
                .build();

        // use the client to send the request
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
            return response.toString();
        } catch (Exception e) {
            return "request faild";
        }
    }
}
