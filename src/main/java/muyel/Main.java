package muyel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.IOException;

/*
 * @Mu Ye Liu - Jan 2025
 * 
 * The main starter class that starts the application
 */
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    // Listen for when the application is fully started and ready
    @EventListener(ApplicationReadyEvent.class)
    public void openBrowser() {
        try {
            // Execute the appropriate command based on the OS to open the browser
            String os = System.getProperty("os.name").toLowerCase();

            String url = "http://localhost:8080";

            if (os.contains("win")) {
                // Windows
                Runtime.getRuntime().exec("cmd /c start " + url);
            } else if (os.contains("mac")) {
                // MacOS
                Runtime.getRuntime().exec("open " + url);
            } else if (os.contains("nix") || os.contains("nux")) {
                // Linux
                Runtime.getRuntime().exec("xdg-open " + url);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
