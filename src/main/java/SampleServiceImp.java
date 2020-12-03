import spark.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class SampleServiceImp implements SampleService {

    @Override
    public byte[] ProcessImage(InputStream input) throws InterruptedException, IOException {
        if (input == null)
            throw new IllegalArgumentException();

        Thread.sleep(5000);

        byte[] response = IOUtils.toByteArray(input);
        return response;
    }
}
