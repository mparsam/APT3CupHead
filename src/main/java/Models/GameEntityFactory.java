package Models;

import Controllers.*;
import Views.BasicGameApp;
import com.almasb.fxgl.animation.AnimationBuilder;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.*;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IntegerComponent;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.box2d.common.Rotation;
import com.sun.javafx.scene.traversal.TopMostTraversalEngine;
import enums.EntityType;
import enums.Variables;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.awt.*;

import static enums.Variables.*;

public class GameEntityFactory implements EntityFactory {

    @Spawns("player")
    public Entity newEnemy(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(EntityType.PLAYER)
                .viewWithBBox("red.png")
                .with(new KeepOnScreenComponent())
                .with(new HealthIntComponent(PLAYER_MAX_HEALTH))
                .with(new CollidableComponent(true))
                .with(new BlinkComponent())
                .with(new PlayerResetComponent())
                .build();
    }

    @Spawns("bullet")
    public Entity newBullet(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(EntityType.BULLET)
                .viewWithBBox("bullet.png")
                .with(new ProjectileComponent(new Point2D(1,0),BULLET_SPEED ))
                .with(new CollidableComponent(true))
                .with(new SoundComponent("bullet.wav"))
                .build();
    }
    @Spawns("bc")
    public Entity newBC(SpawnData data){
        return FXGL.entityBuilder(data)
                .viewWithBBox("img.png")
                .with(new BCMoveComponent())
                .build();
    }

    @Spawns("egg")
    public Entity newEgg(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(EntityType.Egg)
                .viewWithBBox("egg.png")
                .scale(0.3,0.3)
                .with(new RotationComponent())
                .with(new ProjectileComponent(new Point2D(-1,0),EGG_SPEED ))
                .with(new CollidableComponent(true))
                .with(new SoundComponent("egg.wav"))
                .build();
    }

    @Spawns("miniBoss")
    public Entity newMiniBoss(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(EntityType.MINI_BOSS)
                .bbox(new HitBox(BoundingShape.box(79, 54)))
                .rotate(50)
                .with(new CollidableComponent(true))
                .with(new ProjectileComponent(new Point2D(-1, 0), MINI_BOSS_SPEED))
                .with(new OffscreenCleanComponent(new Viewport(BasicGameApp.WIDTH+MINI_BOSS_OFFSCREEN_OFFSET, BasicGameApp.HEIGHT)))
                .with(new HealthIntComponent(MINI_BOSS_MAX_HEALTH))
                .with(new AnimationComponent("miniFly.png", 4, 79, 55, Duration.seconds(1), 0, 3))
                .build();
    }

    @Spawns("bomb")
    public Entity newBomb(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(EntityType.BOMB)
                .viewWithBBox(new Circle(7,7,14, Color.BLACK))
                .with(new ProjectileWithAccelerationComponent(new Point2D(BOMB_X_SPEED,BOMB_Y_SPEED),BOSS_SPEED , new Point2D(0, BOMB_Y_ACCELERATION)))
                .with(new CollidableComponent(true))
                .with(new SoundComponent("bomb.wav"))
                .build();
    }

    @Spawns("shootIcon")
    public Entity newShootState(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(EntityType.SHOOT_ICON)
                .view("bulletIcon.png")
                .build();
    }

    @Spawns("boss")
    public Entity newBoss(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(EntityType.BOSS)
                .with(new CollidableComponent(true))
                .bbox(new HitBox(BoundingShape.box(BOSS_WIDTH, BOSS_HEIGHT)))
                .with(new HealthIntComponent(BOSS_MAX_HEALTH))
                .with(new KeepOnScreenComponent())
                .with(new BossShootComponent())
                .with(new AnimationComponent("bossFly1.png", 12, BOSS_WIDTH, BOSS_HEIGHT, Duration.seconds(3), 0, 11))
                .build();
    }

    @Spawns("boss2")
    public Entity newBoss2(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(EntityType.BOSS2)
                .with(new CollidableComponent(true))
                .view("boss2.png")
                .bbox(new HitBox(BoundingShape.box(BOSS_WIDTH, BOSS_HEIGHT)))
                .with(new HealthIntComponent(BOSS_MAX_HEALTH/2))
                .with(new KeepOnScreenComponent())
                .with(new BossShootComponent())
                .with(new BossMoveComponent())
                .build();
    }



}
