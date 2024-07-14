package dk.sdu.mmmi.cbse.main;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main extends Application {

    public static void main(String[] args) {
        launch(Main.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        AnnotationConfigApplicationContext annotationConfigApplicationContext
                = new AnnotationConfigApplicationContext(ModuleConfg.class);
        Game game = annotationConfigApplicationContext.getBean(Game.class);
        game.start(stage);
    }
}