    package com.sandymandy.pleasurecraft.scene;

    import com.sandymandy.pleasurecraft.entity.girls.AbstractGirlEntity;
    import net.minecraft.entity.player.PlayerEntity;

    public class SceneStateManager {
/*
        private final AbstractGirlEntity entity;
        public String passengerBoneName = "Torso2";
        public float passengerYOffset = 0f;
        public SceneStateManager(AbstractGirlEntity entity) {
            this.entity = entity;
        }
        private boolean inScene = false;

        public void startScene(PlayerEntity rider,
                               String introAnim,
                               String slowAnim,
                               String fastAnim,
                               String cumAnim ) {
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

        public void onAnimationFinished(String finishedAnim) {
        }


    }
*/

///*

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

    // Called by the entity when an animation finishes
    public void onAnimationFinished(String finishedAnim) {
//        if (!inScene) return;

        entity.messageAsEntity("sex");

        switch (this.currentPhase) {
            case INTRO -> {
                if (finishedAnim.equals(animIntro)) {
                    playPhase(ScenePhase.SLOW, animSlow, true);
                }
            }
            case CUM -> {
                if (finishedAnim.equals(animCum)) {
                    stopScene();
                }
            }
        }
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
        if(held){
            entity.messageAsEntity("thrust");

        }
    }

    private void playPhase(ScenePhase phase, String animation, boolean loop) {
        currentPhase = phase;
        entity.playAnimation(animation, loop);
    }

    public void tryTriggerCum(PlayerEntity player) {
        entity.messageAsEntity("cum");
        if (inScene && sceneProgress >= 1.0f && currentPhase != ScenePhase.CUM) {
            playPhase(ScenePhase.CUM, animCum, false);
        }
    }


        public void tick() {
        entity.toggleModelBones("RightLeg, LeftLeg, Torso2", entity.isSceneActive());

        if (!inScene) {
            entity.setFreeze(false);
            return;
        }

        // Handle scene exit
        if (inScene && !entity.hasPassengers()) {
            stopScene();
            return;
        }


        PlayerEntity player = (PlayerEntity) entity.getFirstPassenger();
        if (player != null) player.setInvisible(true);

        // Phase handling for looping states
        switch (currentPhase) {
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
            default -> {} // INTRO and CUM are handled by onAnimationFinished
        }

    }
}

//*/
