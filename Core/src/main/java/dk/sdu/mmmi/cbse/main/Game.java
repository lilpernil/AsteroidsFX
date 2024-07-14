package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Game {

    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();
    private Pane gameWindow = new Pane();
    private Font font = new Font("Sherif", 20);
    private int destroyedAsteroids, destroyedEnemies;
    private Text playerHealthText = new Text();
    private Text enemyHealthText = new Text();
    private Text asteroidsText = new Text();
    private final List<IGamePluginService> gamePluginServices;
    private final List<IEntityProcessingService> entityProcessingServices;
    private final List<IPostEntityProcessingService> postEntityProcessingServices;

    public Game(
            List<IEntityProcessingService> entityProcessingServices,
            List<IPostEntityProcessingService> postEntityProcessingServices,
            List<IGamePluginService> gamePluginService
    ) {
        this.entityProcessingServices = entityProcessingServices;
        this.postEntityProcessingServices = postEntityProcessingServices;
        this.gamePluginServices = gamePluginService;
    }

    public void start(Stage window) throws Exception {
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        asteroidsText.setY((double) gameData.getDisplayHeight() / 20);
        playerHealthText.setX((double) gameData.getDisplayHeight() / 2);
        playerHealthText.setY((double) gameData.getDisplayWidth() / 20);
        enemyHealthText.setX((double) gameData.getDisplayHeight() / 2);
        enemyHealthText.setY((double) gameData.getDisplayWidth() / 10);
        asteroidsText.setFont(font);
        playerHealthText.setFont(font);
        enemyHealthText.setFont(font);
        gameWindow.getChildren().add(asteroidsText);
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

        getAsteroidScore();
        asteroidsText.setText("Destroyed Asteroids: " + destroyedAsteroids);
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

    public void getAsteroidScore() {
        try {
            URL url = new URL("http://localhost:8080/getasteroids");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            destroyedAsteroids = Integer.parseInt(String.valueOf(response));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Collection<? extends IGamePluginService> getPluginServices() {
        return gamePluginServices;
    }

    private List<IEntityProcessingService> getEntityProcessingServices() {
        return entityProcessingServices;
    }

    private Collection<? extends IPostEntityProcessingService> getPostEntityProcessingServices() {
        return postEntityProcessingServices;
    }
}