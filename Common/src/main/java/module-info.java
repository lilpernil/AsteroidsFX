module Common {
    uses dk.sdu.mmmi.cbse.common.services.entityprocessing.IEntityProcessingService;
    uses dk.sdu.mmmi.cbse.common.services.gameplugin.IGamePluginService;
    uses dk.sdu.mmmi.cbse.common.services.postentityprocessing.IPostEntityProcessingService;
    exports dk.sdu.mmmi.cbse.common.data;
    exports dk.sdu.mmmi.cbse.common.services.entityprocessing;
    exports dk.sdu.mmmi.cbse.common.services.gameplugin;
    exports dk.sdu.mmmi.cbse.common.services.postentityprocessing;
}