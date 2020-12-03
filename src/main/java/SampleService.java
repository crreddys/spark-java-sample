import java.io.IOException;
import java.io.InputStream;

public interface SampleService {
    byte[] ProcessImage(InputStream file) throws InterruptedException, IOException;
}
