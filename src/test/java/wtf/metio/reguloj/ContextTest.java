package wtf.metio.reguloj;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ContextTest {

    @Test
    void of() {
        assertNotNull(Context.of("test"));
    }

}
