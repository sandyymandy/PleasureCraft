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
