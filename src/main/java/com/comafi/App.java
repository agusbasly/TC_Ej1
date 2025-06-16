package com.comafi;

import java.net.URI;
import java.nio.file.Paths;
import java.util.Collections;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

public class App {

    public static final String BASE_URI = "http://localhost:8082/";

    public static HttpServer startServer() {
        final ResourceConfig config = new ResourceConfig()
                .packages("com.comafi.controller")
                .register(JacksonFeature.class)
                .register(io.swagger.v3.jaxrs2.integration.resources.OpenApiResource.class);

        // OpenAPI config
        OpenAPI oas = new OpenAPI().info(new Info()
                .title("Comafi API")
                .version("1.0")
                .description("Documentación de cuentas bancarias"));

        SwaggerConfiguration swaggerConfig = new SwaggerConfiguration()
                .openAPI(oas)
                .prettyPrint(true)
                .resourcePackages(Collections.singleton("com.comafi.controller"));

        try {
            new JaxrsOpenApiContextBuilder<>()
                    .openApiConfiguration(swaggerConfig)
                    .application(config)
                    .buildContext(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);

        // ✅ Ruta absoluta al folder swagger-ui (ajustada a desarrollo)
        String swaggerPath = Paths.get("src", "main", "resources", "swagger-ui").toAbsolutePath().toString();
        System.out.println("📂 Sirviendo Swagger UI desde: " + swaggerPath);

        StaticHttpHandler staticHandler = new StaticHttpHandler(swaggerPath);
        server.getServerConfiguration().addHttpHandler(staticHandler, "/swagger");

        return server;
    }

    public static void main(String[] args) {
        final HttpServer server = startServer();
        System.out.println("🚀 Servidor iniciado en " + BASE_URI);
        System.out.println("🔗 Swagger UI: http://localhost:8082/swagger/");
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdownNow));
    }
}
