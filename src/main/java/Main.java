import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.InputStream;
import java.io.OutputStream;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        final SampleService service = new SampleServiceImp();

        port(8080);

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
    }
}

