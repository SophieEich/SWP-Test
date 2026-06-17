package org.example.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.entity.Order;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Exports the loaded {@link Order} records to a JSON file using Jackson.
 *
 * <p>The resulting {@code output/orders.json} is intended as an exchange format
 * for neighbouring applications and systems.</p>
 */
public final class JsonExporter {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            // serialise LocalDate as ISO-8601 ("2025-01-31") instead of a numeric array
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.INDENT_OUTPUT);

    private JsonExporter() {
    }

    /**
     * Writes all orders to {@code output/orders.json}.
     *
     * @return the path of the written JSON file
     */
    public static Path export(List<Order> orders) throws IOException {
        Path target = OutputDirectory.resolve("orders.json");
        MAPPER.writeValue(target.toFile(), orders);
        return target;
    }
}
