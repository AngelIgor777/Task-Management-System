package org.task.task_management_system.util;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class KeyUtil {
    @Getter
    private static String accessSecret = "";

    @Getter
    private static String refreshSecret = "";

    private static String accessExpiration = "";

    private static String refreshExpiration = "";

    @Getter
    private static String actuatorKey = "";

    public static void setProperties(Dotenv dotenv) {
        // Прокидываем переменные в System Environment
        dotenv.entries().forEach(entry -> {
            String value = entry.getValue();
            System.setProperty(entry.getKey(), value);

            if (entry.getKey().equals("ACTUATOR_KEY")) {
                actuatorKey = value;
            }
            if (entry.getKey().equals("JWT_ACCESS")) {
                accessSecret = value;
            }

            if (entry.getKey().equals("JWT_REFRESH")) {
                refreshSecret = value;
            }
            if (entry.getKey().equals("JWT_ACCESS_EXPIRATION")) {
                accessExpiration = value;
            }
            if (entry.getKey().equals("JWT_REFRESH_EXPIRATION")) {
                refreshExpiration = value;
            }
        });
    }

}