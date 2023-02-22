package io.takamaka.core.fluxnode;

import io.takamaka.core.fluxnode.exceptions.FNUException;
import io.takamaka.core.fluxnode.utils.FNU;
import io.takamaka.wallet.utils.FileHelper;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class FluxnodeApplication {

    public static void main(String[] args) {
        try {
            FileHelper.initProjectFiles();
            FNU.initFluxFiles();
        } catch (IOException | FNUException ex) {
            log.error("Init faliure ", ex);
            throw new RuntimeException("initialization error", ex);
        }
        SpringApplication.run(FluxnodeApplication.class, args);
    }

}
