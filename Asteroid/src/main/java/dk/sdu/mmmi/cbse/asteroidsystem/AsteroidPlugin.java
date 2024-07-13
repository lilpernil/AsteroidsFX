package dk.sdu.mmmi.cbse.asteroidsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

import java.util.Random;

public class AsteroidPlugin implements IGamePluginService {

    private Entity asteroid;
    private Entity asteroid2;
    private Entity asteroid3;
    private Entity asteroid4;
    private Entity asteroid5;

    private Random random = new Random();

    @Override
    public void start(GameData gameData, World world) {
        asteroid = createAsteroid(gameData);
        world.addEntity(asteroid);
        asteroid2 = createAsteroid(gameData);
        world.addEntity(asteroid2);
        asteroid3 = createAsteroid(gameData);
        world.addEntity(asteroid3);
        asteroid4 = createAsteroid(gameData);
        world.addEntity(asteroid4);
        asteroid5 = createAsteroid(gameData);
        world.addEntity(asteroid5);
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(asteroid);
    }

    protected Entity createAsteroid(GameData gameData) {
        Entity asteroid = new Asteroid();
        asteroid.setPolygonCoordinates(
                -(random.nextInt(2, 25)), -(random.nextInt(2, 25)),
                (random.nextInt(2, 25)), -(random.nextInt(2, 25)),
                (random.nextInt(2, 25)), (random.nextInt(2, 25)),
                -(random.nextInt(2, 25)), (random.nextInt(2, 25))
        );
        asteroid.setX((double) gameData.getDisplayHeight() / 4);
        asteroid.setY((double) gameData.getDisplayWidth() / 4);
        asteroid.setLife(3);
        return asteroid;
    }
}