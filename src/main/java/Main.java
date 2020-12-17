import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeoutException;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        final SampleService service = new SampleServiceImp();

        port(8080);

        enableCORS("*", "*", "*");

        get("/hello", (request, response) -> {
            return "Hello World";
        });

        post("/sample-post", (request, response) -> {
            return response.status();
        });

        post("/processImage", (request, response) -> {
            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            Part file = request.raw().getPart("file");

            try (InputStream inputStream = file.getInputStream()) {
                byte[] processedImage = service.ProcessImage(inputStream);

                // Set the content type and attachment header.
                response.header("Content-disposition", "attachment;filename=processedImage.jpg");
                response.type("image/jpeg");

                OutputStream outputStream = response.raw().getOutputStream();
                outputStream.write(processedImage);
                outputStream.flush();
                return response;
            }
        });

        get("/process-data", (request, response) -> {
            try (RPCClient fibonacciRpc = new RPCClient()) {
                for (int i = 0; i < 32; i++) {
                    String i_str = Integer.toString(i);
                    System.out.println(" [x] Requesting fib(" + i_str + ")");
                    String res = fibonacciRpc.call(i_str);
                    System.out.println(" [.] Got '" + res + "'");
                }
            } catch (IOException | TimeoutException | InterruptedException e) {
                e.printStackTrace();
            }

            return "Data Processed";
        });
    }

    static void enableCORS(final String origin, final String methods, final String headers) {
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Allow-Methods", methods);
            response.header("Access-Control-Allow-Headers", headers);
        });
    }
}

