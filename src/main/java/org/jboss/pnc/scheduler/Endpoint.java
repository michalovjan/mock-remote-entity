package org.jboss.pnc.scheduler;

import io.vertx.axle.core.Vertx;
import io.vertx.axle.ext.web.client.WebClient;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;
import org.eclipse.microprofile.context.ManagedExecutor;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

@Path("/endpoint")
public class Endpoint {

    @Inject
    Vertx vertx;

    @Inject
    ManagedExecutor executor;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/accept")
    public Response acceptExecution(Request request) throws InterruptedException, URISyntaxException {
        System.out.println("Accepted request: " + request.getCallback() + " with payload: " + request.getPayload());

        URI uri = new URI(request.getCallback());
        WebClient client = WebClient.create(vertx,
                new WebClientOptions().setDefaultHost(uri.getHost()).setDefaultPort(uri.getPort()).setKeepAlive(false));
        executor.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            client.post(request.getCallback())
                    .putHeader("Content-Type", MediaType.APPLICATION_JSON)
                    .sendJsonObject(new JsonObject()
                            .put("status", true))
                    .thenApply(resp -> {
                        System.out.println(resp.statusCode());
                        return resp;
                    });
        });
        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/acceptWithoutCallback")
    public Response accept(Request request) throws InterruptedException, URISyntaxException {
        System.out.println("Accepted request: " + request.getCallback() + " with payload: " + request.getPayload());
        return Response.ok().build();
    }
}