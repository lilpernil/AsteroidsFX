package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class PlayerControlSystemTest {

    private PlayerControlSystem playerControlSystem;
    @Mock
    private GameData gameData;
    @Mock
    private World world;
    @Mock
    private Entity player;
    @Mock
    private GameKeys keys;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        playerControlSystem = new PlayerControlSystem();

        when(gameData.getKeys()).thenReturn(keys);
        when(world.getEntities(Player.class)).thenReturn(Collections.singletonList(player));
    }

    @Test
    public void testPlayerMoveLeft() {
        when(keys.isDown(GameKeys.LEFT)).thenReturn(true);
        when(player.getRotation()).thenReturn(0.0);
        playerControlSystem.process(gameData, world);
        verify(player).setRotation(-5);
    }

    @Test
    public void testPlayerMoveRight() {
        when(keys.isDown(GameKeys.RIGHT)).thenReturn(true);
        when(player.getRotation()).thenReturn(0.0);
        playerControlSystem.process(gameData, world);
        verify(player).setRotation(5);
    }

    @Test
    public void testPlayerMoveForward() {
        when(keys.isDown(GameKeys.UP)).thenReturn(true);
        when(player.getRotation()).thenReturn(0.0);
        when(player.getX()).thenReturn(0.0);
        when(player.getY()).thenReturn(0.0);
        playerControlSystem.process(gameData, world);
        verify(player).setX(anyDouble());
        verify(player).setY(anyDouble());
    }

    @Test
    public void testPlayerPositionConstraints() {
        when(player.getX()).thenReturn(-1.0);
        playerControlSystem.process(gameData, world);
        verify(player).setX(1.0);

        when(player.getX()).thenReturn(1001.0);
        when(gameData.getDisplayWidth()).thenReturn(1000);
        playerControlSystem.process(gameData, world);
        verify(player).setX(999.0);

        when(player.getY()).thenReturn(-1.0);
        playerControlSystem.process(gameData, world);
        verify(player).setY(1.0);

        when(player.getY()).thenReturn(1001.0);
        when(gameData.getDisplayHeight()).thenReturn(1000);
        playerControlSystem.process(gameData, world);
        verify(player).setY(999.0);
    }

    @Test
    public void testPlayerLifeReductionWhenHit() {
        when(player.isHit()).thenReturn(true);
        when(player.getLife()).thenReturn(5);
        playerControlSystem.process(gameData, world);
        verify(player).setLife(4);
        verify(player).setHit(false);
        verify(gameData).setPlayerLife(4);
    }

    @Test
    public void testPlayerRemovalWhenLifeZero() {
        when(player.getLife()).thenReturn(0);
        playerControlSystem.process(gameData, world);
        verify(world).removeEntity(player);
        verify(gameData).setPlayerLife(0);
    }
}
