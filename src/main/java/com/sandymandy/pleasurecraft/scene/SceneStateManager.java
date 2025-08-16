///*
    package com.sandymandy.pleasurecraft.scene;

    import com.sandymandy.pleasurecraft.entity.girls.AbstractGirlEntity;
    import net.minecraft.entity.player.PlayerEntity;

    public class SceneStateManager {
        private final AbstractGirlEntity entity;
        public String passengerBoneName = "Torso2";
        public float passengerYOffset = 0f;
        public SceneStateManager(AbstractGirlEntity entity) {
            this.entity = entity;
        }
        private boolean inScene = false;

        public void startScene(PlayerEntity rider) {
            if (inScene) return;

            if (entity.isSittingdown()) {
                entity.setSit(false);
            }

            if (!entity.isStripped()) {
                entity.setStripped(true);
            }

            entity.setFreeze(true);
            inScene = true;
            entity.setSceneState(true); // <--- SYNCED TO CLIENT
            onSceneStart(rider);
            rider.startRiding(entity, false);
        }


        public void stopScene() {
            if (!inScene) return;
            onSceneStop();
            inScene = false;
            entity.setFreeze(false);
            entity.setSceneState(false); // <--- SYNCED TO CLIENT
            if(entity.hasPassengers()){
                entity.removeAllPassengers();
            }
        }

        public boolean isInScene() {
            return entity.isSceneActive();
        }

        public void onSceneStart(PlayerEntity player) {
            player.setInvisible(true);
            // Play animation, music, lock inventory, etc.
        }

        public void onSceneStop() {
            for (PlayerEntity player : entity.getWorld().getPlayers()) {
                if (!player.hasVehicle()) {
                    player.setInvisible(false);
                }
            }
        }

        public void tick() {
            if (!inScene ) {
                entity.setFreeze(false);
            }

            if (inScene && !entity.hasPassengers()) {
                stopScene();
            }

            entity.toggleModelBones("RightLeg, LeftLeg, Torso2", entity.isSceneActive());

            if (inScene) {
                PlayerEntity player = (PlayerEntity) entity.getFirstPassenger();
                if (player != null) player.setInvisible(true);
            }

        }

    }
//*/

/*
package com.sandymandy.pleasurecraft.scene;

import com.sandymandy.pleasurecraft.entity.girls.AbstractGirlEntity;
import net.minecraft.entity.player.PlayerEntity;

public class SceneStateManager {
    private final AbstractGirlEntity entity;
    public String passengerBoneName = "Torso2";
    public float passengerYOffset = 0f;

    private boolean inScene = false;
    private ScenePhase currentPhase = ScenePhase.NONE;
    private float sceneProgress = 0f;
    private boolean isKeyHeld = false;

    // Animations for this scene
    private String animIntro;
    private String animSlow;
    private String animFast;
    private String animCum;

    // Progress speeds
    private static final float SLOW_SPEED = 0.002f;
    private static final float FAST_SPEED = 0.01f;

    public SceneStateManager(AbstractGirlEntity entity) {
        this.entity = entity;
    }

    public enum ScenePhase {
        NONE, INTRO, SLOW, FAST, CUM
    }
    

    public void startScene(PlayerEntity rider,
                           String introAnim,
                           String slowAnim,
                           String fastAnim,
                           String cumAnim) {
        if (inScene) return;

        // Store animations
        this.animIntro = introAnim;
        this.animSlow = slowAnim;
        this.animFast = fastAnim;
        this.animCum = cumAnim;

        if (entity.isSittingdown()) {
            entity.setSit(false);
        }
        if (!entity.isStripped()) {
            entity.setStripped(true);
        }

        entity.setFreeze(true);
        inScene = true;
        entity.setSceneState(true);
        sceneProgress = 0f;
        isKeyHeld = false;

        onSceneStart(rider);
        rider.startRiding(entity, false);

        // Start intro
        playPhase(ScenePhase.INTRO, animIntro, false);
    }

    public void stopScene() {
        if (!inScene) return;
        onSceneStop();
        inScene = false;
        entity.setFreeze(false);
        entity.setSceneState(false);
        currentPhase = ScenePhase.NONE;

        if (entity.hasPassengers()) {
            entity.removeAllPassengers();
        }
    }

    public boolean isInScene() {
        return entity.isSceneActive();
    }

    public void onSceneStart(PlayerEntity player) {
        player.setInvisible(true);
    }

    public void onSceneStop() {
        for (PlayerEntity player : entity.getWorld().getPlayers()) {
            if (!player.hasVehicle()) {
                player.setInvisible(false);
            }
        }
        entity.stopOverrideAnimations();
    }

    public void setKeyHeld(boolean held) {
        this.isKeyHeld = held;
    }

    private void playPhase(ScenePhase phase, String animation, boolean loop) {
        currentPhase = phase;
        entity.playAnimation(animation, loop); // <- now just uses entity system
    }

    public void tick() {
        if (!inScene) {
            entity.setFreeze(false);
            return;
        }

        // Handle scene exit
        if (inScene && !entity.hasPassengers()) {
            stopScene();
            return;
        }

        // Keep hiding passenger model
        entity.toggleModelBones("RightLeg, LeftLeg, Torso2", entity.isSceneActive());

        PlayerEntity player = (PlayerEntity) entity.getFirstPassenger();
        if (player != null) player.setInvisible(true);

        // Phase handling
        switch (currentPhase) {
            case INTRO -> {
                // When intro stops naturally (one-shot), entity's playAnimation system
                // will reset overrideAnim -> "", so we can check it here
                if (!entity.hasActiveOverrideAnimation()) {
                    playPhase(ScenePhase.SLOW, animSlow, true);
                }
            }
            case SLOW -> {
                sceneProgress += SLOW_SPEED;
                if (isKeyHeld) {
                    playPhase(ScenePhase.FAST, animFast, true);
                }
            }
            case FAST -> {
                sceneProgress += FAST_SPEED;
                if (!isKeyHeld) {
                    playPhase(ScenePhase.SLOW, animSlow, true);
                }
            }
            case CUM -> {
                // When cum animation finishes, stop scene
                if (!entity.hasActiveOverrideAnimation()) {
                    stopScene();
                }
            }
        }

        // Handle cum trigger
        if (sceneProgress >= 1.0f && currentPhase != ScenePhase.CUM) {
            if (player != null && player.jumping) {
                playPhase(ScenePhase.CUM, animCum, false);
            }
        }
    }
}
*/
