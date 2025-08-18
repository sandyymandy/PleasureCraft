    package com.sandymandy.pleasurecraft.scene;

    import com.sandymandy.pleasurecraft.PleasureCraft;
    import com.sandymandy.pleasurecraft.client.PleasureCraftKeybinds;
    import com.sandymandy.pleasurecraft.entity.girls.AbstractGirlEntity;
    import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
    import net.minecraft.entity.player.PlayerEntity;
    import net.minecraft.util.Identifier;

    import java.util.List;
    import java.util.Objects;

    import static com.sandymandy.pleasurecraft.entity.girls.AbstractGirlEntity.*;

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

//        entity.getLookControl().lookAt(rider, entity.getMaxHeadRotation() + 20, entity.getMaxLookPitchChange());

        // Store animations
        entity.getDataTracker().set(INTRO_ANIM, introAnim);
        entity.getDataTracker().set(SLOW_ANIM, slowAnim);
        entity.getDataTracker().set(FAST_ANIM, fastAnim);
        entity.getDataTracker().set(CUM_ANIM, cumAnim);

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

        String animIntro = entity.getDataTracker().get(INTRO_ANIM);
        playPhase(ScenePhase.INTRO, animIntro, false);
    }

    public void onAnimationFinished(String finishedAnim) {

        if (finishedAnim.equals(this.animIntro)) {
            playPhase(ScenePhase.SLOW, this.animSlow, true);
        }
        else if (finishedAnim.equals(this.animCum)) {
            stopScene();
        }
        else {
            PleasureCraft.LOGGER.error(finishedAnim+" is not equal to " + this.animIntro + " or " + this.animSlow);
        }

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

    public void tryTriggerCum() {
        entity.messageAsEntity("cum");
        if (entity.isSceneActive() && sceneProgress >= 1.0f && currentPhase != ScenePhase.CUM) {
            playPhase(ScenePhase.CUM, animCum, false);
        }
    }

        private void handleKeybinds() {
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (client.player == null) return;

                // Thrust button held
                boolean thrustHeld = PleasureCraftKeybinds.thrustKey.isPressed();
                setKeyHeld(thrustHeld);

                // Cum button (pressed once)
                if (PleasureCraftKeybinds.cumKey.wasPressed()) {
                    tryTriggerCum();
                }
            });
        }



    public void tick() {
        handleKeybinds();

        if(!(Objects.equals(this.animIntro, entity.getDataTracker().get(INTRO_ANIM)) &&
                Objects.equals(this.animSlow, entity.getDataTracker().get(SLOW_ANIM)) &&
                Objects.equals(this.animFast, entity.getDataTracker().get(FAST_ANIM)) &&
                Objects.equals(this.animCum, entity.getDataTracker().get(CUM_ANIM)))){
            this.animIntro = entity.getDataTracker().get(INTRO_ANIM);
            this.animSlow  = entity.getDataTracker().get(SLOW_ANIM);
            this.animFast  = entity.getDataTracker().get(FAST_ANIM);
            this.animCum   = entity.getDataTracker().get(CUM_ANIM);
        }





        entity.toggleModelBones(List.of("RightLeg", "LeftLeg", "Torso2"), entity.isSceneActive());

        if (!entity.isSceneActive()) {
            entity.setFreeze(false);
            return;
        }

        // Handle scene exit
        if (entity.isSceneActive() && !entity.hasPassengers()) {
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
