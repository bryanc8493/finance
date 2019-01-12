package utilities.exceptions;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class AppExceptionTest {

    private Exception generateException() {
        try {
            int x = 2 / 0;
        } catch (Exception e) {
            return e;
        }

        return null;
    }

    @Test
    public void testBasicException() {
        Exception e = generateException();

        AppException appException = new AppException(e);

        assertNotNull(e);
        assertNotNull(appException);
    }
}
