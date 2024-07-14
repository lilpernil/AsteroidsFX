package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

import java.lang.module.Configuration;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();
    private Pane gameWindow = new Pane();
    private Font font = new Font("Sherif", 20);
    private int destroyedAsteroids, destroyedEnemies;
    private Text playerHealthText = new Text();
    private Text enemyHealthText = new Text();

    private static ModuleLayer moduleLayer;

    public static void main(String[] args) {

        moduleLayer = createModuleLayer();

        launch(Main.class);
    }

    @Override
    public void start(Stage window) throws Exception {
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        Text asteroidText = new Text(10, 20, "Destroyed asteroids: " + destroyedAsteroids);
        Text enemyShipText = new Text(10, 20, "Destroyed enemies: " + destroyedEnemies);
        asteroidText.setY((double) gameData.getDisplayHeight() / 20);
        enemyShipText.setY((double) gameData.getDisplayHeight() / 10);
        playerHealthText.setX((double) gameData.getDisplayHeight() / 2);
        playerHealthText.setY((double) gameData.getDisplayWidth() / 20);
        enemyHealthText.setX((double) gameData.getDisplayHeight() / 2);
        enemyHealthText.setY((double) gameData.getDisplayWidth() / 10);
        asteroidText.setFont(font);
        enemyShipText.setFont(font);
        playerHealthText.setFont(font);
        enemyHealthText.setFont(font);
        gameWindow.getChildren().add(asteroidText);
        gameWindow.getChildren().add(enemyShipText);
        gameWindow.getChildren().add(playerHealthText);
        gameWindow.getChildren().add(enemyHealthText);

        Scene scene = new Scene(gameWindow);
        scene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.LEFT)) {
                gameData.getKeys().setKey(GameKeys.LEFT, true);
            }
            if (event.getCode().equals(KeyCode.RIGHT)) {
                gameData.getKeys().setKey(GameKeys.RIGHT, true);
            }
            if (event.getCode().equals(KeyCode.UP)) {
                gameData.getKeys().setKey(GameKeys.UP, true);
            }
            if (event.getCode().equals(KeyCode.SPACE)) {
                gameData.getKeys().setKey(GameKeys.SPACE, true);
            }
        });
        scene.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.LEFT)) {
                gameData.getKeys().setKey(GameKeys.LEFT, false);
            }
            if (event.getCode().equals(KeyCode.RIGHT)) {
                gameData.getKeys().setKey(GameKeys.RIGHT, false);
            }
            if (event.getCode().equals(KeyCode.UP)) {
                gameData.getKeys().setKey(GameKeys.UP, false);
            }
            if (event.getCode().equals(KeyCode.SPACE)) {
                gameData.getKeys().setKey(GameKeys.SPACE, false);
            }
        });

        // Lookup all Game Plugins using ServiceLoader
        for (IGamePluginService iGamePlugin : getPluginServices()) {
            iGamePlugin.start(gameData, world);
        }
        for (Entity entity : world.getEntities()) {
            Polygon polygon = new Polygon(entity.getPolygonCoordinates());
            polygons.put(entity, polygon);
            gameWindow.getChildren().add(polygon);
        }

        render();

        window.setScene(scene);
        window.setTitle("ASTEROIDS");
        window.show();

    }

    private void render() {
        new AnimationTimer() {
            private long then = 0;

            @Override
            public void handle(long now) {
                update();
                draw();
                gameData.getKeys().update();
            }

        }.start();
    }

    private void update() {

        // Update
        for (IEntityProcessingService entityProcessorService : getEntityProcessingServices()) {
            entityProcessorService.process(gameData, world);
        }
        for (IPostEntityProcessingService postEntityProcessorService : getPostEntityProcessingServices()) {
            postEntityProcessorService.process(gameData, world);
        }
        playerHealthText.setText("Player health: " + gameData.getPlayerLife());
        enemyHealthText.setText("Enemy health: " + gameData.getEnemyLife());
    }

    private void draw() {
        for (Entity entity : world.getEntities()) {
            if (polygons.get(entity) == null) {
                Polygon polygon = new Polygon(entity.getPolygonCoordinates());
                polygons.put(entity, polygon);
                gameWindow.getChildren().add(polygon);
            }
            Polygon polygon = polygons.get(entity);
            polygon.setTranslateX(entity.getX());
            polygon.setTranslateY(entity.getY());
            polygon.setRotate(entity.getRotation());
        }
        polygons.forEach((entity, polygon) -> {
            if (!world.getEntities().contains(entity)) {
                polygons.remove(entity);
                gameWindow.getChildren().remove(polygon);
            }
        });
    }

    private Collection<? extends IGamePluginService> getPluginServices() {
        return ServiceLoader.load(moduleLayer, IGamePluginService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private Collection<? extends IEntityProcessingService> getEntityProcessingServices() {
        return ServiceLoader.load(moduleLayer, IEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private Collection<? extends IPostEntityProcessingService> getPostEntityProcessingServices() {
        return ServiceLoader.load(moduleLayer, IPostEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private static ModuleLayer createModuleLayer() {
        Path pluginDirectory = Paths.get("plugin");

        ModuleFinder moduleFinder = ModuleFinder.of(pluginDirectory);

        List<String> plugin = moduleFinder.findAll().stream()
                .map(ModuleReference::descriptor)
                .map(ModuleDescriptor::name).collect(Collectors.toList());

        Configuration pluginConfiguration = ModuleLayer.boot().configuration().resolve(
                moduleFinder,
                ModuleFinder.of(),
                plugin
        );

        return ModuleLayer.boot().defineModulesWithOneLoader(
                pluginConfiguration,
                ClassLoader.getSystemClassLoader()
        );
    }
}
