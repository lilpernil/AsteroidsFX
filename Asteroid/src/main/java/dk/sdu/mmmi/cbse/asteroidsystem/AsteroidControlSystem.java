package dk.sdu.mmmi.cbse.asteroidsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.entityprocessing.IEntityProcessingService;

import java.util.Random;

public class AsteroidControlSystem implements IEntityProcessingService {

    private Random random = new Random();

    private AsteroidPlugin asteroidPlugin = new AsteroidPlugin();

    @Override
    public void process(GameData gameData, World world) {
        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            if (random.nextInt(100) > 52) {
                asteroid.setRotation(asteroid.getRotation() - 5);
            } else {
                asteroid.setRotation(asteroid.getRotation() + 5);
            }

            double changeX = Math.cos(Math.toRadians(asteroid.getRotation()));
            double changeY = Math.sin(Math.toRadians(asteroid.getRotation()));
            asteroid.setX(asteroid.getX() + changeX / 2);
            asteroid.setY(asteroid.getY() + changeY / 2);

            if (asteroid.getX() < 0) {
                asteroid.setX(1);
                asteroid.setRotation(asteroid.getRotation() - 5);
            }
            if (asteroid.getX() > gameData.getDisplayWidth()) {
                asteroid.setX(gameData.getDisplayWidth() - 1);
                asteroid.setRotation(asteroid.getRotation() - 5);
            }
            if (asteroid.getY() < 0) {
                asteroid.setY(1);
                asteroid.setRotation(asteroid.getRotation() + 5);
            }
            if (asteroid.getY() > gameData.getDisplayHeight()) {
                asteroid.setY(gameData.getDisplayHeight() - 1);
                asteroid.setRotation(asteroid.getRotation() + 5);
            }
            if (asteroid.isHit() && asteroid.getLife() > 0) {
                double[] coordinates = asteroid.getPolygonCoordinates();

                for (int i = 0; i < 2; i++) {
                    double[] updatedCoordinates = new double[coordinates.length];
                    for (int j = 0; j < coordinates.length; j++) {
                        updatedCoordinates[j] = coordinates[j] / random.nextDouble(1.5,2);
                    }
                    Entity newAsteroid = asteroidPlugin.createAsteroid(gameData);
                    newAsteroid.setPolygonCoordinates(updatedCoordinates);
                    newAsteroid.setX(asteroid.getX());
                    newAsteroid.setY(asteroid.getY());
                    newAsteroid.setLife(asteroid.getLife() - 1);
                    world.addEntity(newAsteroid);
                }
                world.removeEntity(asteroid);
            }
            if (asteroid.getLife() == 0) {
                world.removeEntity(asteroid);
            }
        }
    }
}
