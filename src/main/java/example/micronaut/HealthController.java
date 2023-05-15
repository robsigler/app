package example.micronaut;

import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

@ExecuteOn(TaskExecutors.IO)
@Controller("/health")
class HealthController {

    @Get("/")
    String get() {
        return "200";
    }
}
