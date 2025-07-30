package com.sandymandy.pleasurecraft.util.entity.ai;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.ai.control.MoveControl;

import java.util.EnumSet;

public class StopMovementGoal extends Goal {
    private final PathAwareEntity entity;

    public StopMovementGoal(PathAwareEntity entity) {
        this.entity = entity;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.JUMP));
    }

    @Override
    public boolean canStart() {
        return true; // Always active while added
    }

    @Override
    public void start() {
        haltMovement();
    }

    @Override
    public void tick() {
        haltMovement();
    }

    private void haltMovement() {
        entity.getNavigation().stop();
        entity.setVelocity(0, entity.getVelocity().y > 0 ? 0 : entity.getVelocity().y, 0); // stops lateral motion
        entity.setJumping(false);

        MoveControl control = entity.getMoveControl();
        if (control != null) {
            control.moveTo(entity.getX(), entity.getY(), entity.getZ(), 0); // keep position
        }
    }

    @Override
    public boolean shouldContinue() {
        return true; // Continuous block
    }
}
