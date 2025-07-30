package com.sandymandy.pleasurecraft.util.entity.ai;

import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.function.BooleanSupplier;

public class ConditionalGoal extends Goal {
    private final Goal wrappedGoal;
    private final BooleanSupplier condition;

    public ConditionalGoal(Goal wrappedGoal, BooleanSupplier condition) {
        this.wrappedGoal = wrappedGoal;
        this.condition = condition;
    }

    @Override
    public boolean canStart() {
        return condition.getAsBoolean() && wrappedGoal.canStart();
    }

    @Override
    public boolean shouldContinue() {
        return condition.getAsBoolean() && wrappedGoal.shouldContinue();
    }

    @Override
    public void start() {
        wrappedGoal.start();
    }

    @Override
    public void stop() {
        wrappedGoal.stop();
    }

    @Override
    public void tick() {
        wrappedGoal.tick();
    }

    @Override
    public boolean canStop() {
        return wrappedGoal.canStop();
    }

    @Override
    public boolean shouldRunEveryTick() {
        return wrappedGoal.shouldRunEveryTick();
    }

    @Override
    public EnumSet<Control> getControls() {
        return wrappedGoal.getControls();
    }
}
