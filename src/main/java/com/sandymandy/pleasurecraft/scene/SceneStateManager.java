    package com.sandymandy.pleasurecraft.scene;

    import com.sandymandy.pleasurecraft.PleasureCraft;
    import com.sandymandy.pleasurecraft.entity.girls.AbstractGirlEntity;
    import net.minecraft.entity.player.PlayerEntity;

    import java.util.List;

    public class SceneStateManager {

    private final AbstractGirlEntity entity;
    public String passengerBoneName = "Torso2";
    public float passengerYOffset = 0f;
    private boolean inScene = false;
    private ScenePhase currentPhase = ScenePhase.NONE;
    private float sceneProgress = 0f;
    private final float cumThreshold = 5f;
    private boolean isKeyHeld = false;

    // Animations for this scene
    private String animIntro;
    private List<String> animSlow;
    private List<String> animFast;
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
                           List<String> slowAnim,
                           List<String> fastAnim,
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
        this.sceneProgress = 0f;
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
        entity.setStripped(false);
    }

    public void onSceneStart(PlayerEntity player) {
        player.setInvisible(true);

        playPhase(ScenePhase.INTRO, this.animIntro, false,true);
    }

    public void onAnimationFinished(String finishedAnim) {

        if (finishedAnim.equals(this.animIntro)) {
            playPhase(ScenePhase.SLOW, getRandomFromList(this.animSlow), true,false);
        }
        else if (finishedAnim.equals(this.animCum)) {
            stopScene();
        }
        else {
            PleasureCraft.LOGGER.error(finishedAnim+" is not equal to " + this.animIntro + " or " + getRandomFromList(this.animSlow));
        }

    }

    public void onSceneStop() {
        for (PlayerEntity player : entity.getWorld().getPlayers()) {
            if (!player.hasVehicle()) {
                player.setInvisible(false);
            }
        }

        if (entity.hasPassengers()) {
            entity.removeAllPassengers();
        }

        currentPhase = ScenePhase.NONE;

        entity.stopOverrideAnimations();
    }

    public void setKeyHeld(boolean held) {
        this.isKeyHeld = held;
    }

    private void playPhase(ScenePhase phase, String animation, boolean loop, boolean holdOnLastFrame) {
        currentPhase = phase;
        entity.playAnimation(animation, loop, holdOnLastFrame);
    }

    public void tryTriggerCum() {
        if (entity.isSceneActive() && this.entity.getSceneProgress() >= cumThreshold && currentPhase != ScenePhase.CUM) {
            entity.messageAsEntity("cum");
            playPhase(ScenePhase.CUM, animCum, false,false);
        }
    }

    private void inScene(){
        if (!this.entity.isSceneActive()) return;
        PleasureCraft.LOGGER.info(this.entity.getSceneProgress()+"");

    }

    private String getRandomFromList(List<String> list) {
        if (list.size() == 1) return list.getFirst(); // fallback
        int index = entity.getWorld().getRandom().nextInt(list.size());
        return list.get(index);
    }

    public void tick() {
        inScene();

        entity.setSceneProgress(sceneProgress);

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
                    playPhase(ScenePhase.FAST, getRandomFromList(this.animFast), true,false);
                }
            }
            case FAST -> {
                sceneProgress += FAST_SPEED;
                if (!isKeyHeld) {
                    playPhase(ScenePhase.SLOW, getRandomFromList(this.animSlow), true,false);
                }
            }
            default -> {} // INTRO and CUM are handled by onAnimationFinished
        }

    }
}
