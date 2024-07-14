import dk.sdu.mmmi.cbse.common.services.entityprocessing.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.gameplugin.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.postentityprocessing.IPostEntityProcessingService;

module Core {
    requires Common;
    requires CommonBullet;    
    requires javafx.graphics;    
    opens dk.sdu.mmmi.cbse.main to javafx.graphics;
    uses IGamePluginService;
    uses IEntityProcessingService;
    uses IPostEntityProcessingService;
}


