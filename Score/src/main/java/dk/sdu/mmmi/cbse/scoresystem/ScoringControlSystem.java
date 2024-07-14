package dk.sdu.mmmi.cbse.scoresystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ScoringControlSystem {

    private Long asteroids = 0L;

    public static void main(String[] args) {
        SpringApplication.run(ScoringControlSystem.class, args);
    }

    @GetMapping("/asteroids")
    public Long destroyedAsteroids(@RequestParam(value = "score") Long score) {
        this.asteroids += score;
        return this.asteroids;
    }

    @GetMapping("/getasteroids")
    public Long getAsteroids() {
        return this.asteroids;
    }
}
