package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.views.View;

import java.util.HashMap;
import java.util.Map;

@ExecuteOn(TaskExecutors.IO)
@Controller("/")
class HomeController {
  @View("home")
  @Get("/")
  public Map<String, Object> create() {
    final Map<String, Object> model = new HashMap<>();
    return model;
  }
}
