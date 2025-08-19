package com.sandymandy.pleasurecraft.entity.ai.goal;

import com.sandymandy.pleasurecraft.entity.girls.AbstractGirlEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

public class GirlAttackGoal extends MeleeAttackGoal {
    private final AbstractGirlEntity girl;
    private int ticks;

    public GirlAttackGoal(AbstractGirlEntity girl, double speed, boolean pauseWhenMobIdle) {
        super(girl, speed, pauseWhenMobIdle);
        this.girl = girl;
    }

    @Override
    public void start() {
        super.start();
        this.ticks = 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.girl.setAttacking(false);
    }

    @Override
    public void tick() {
        super.tick();
        this.ticks++;
        if (this.ticks >= 5 && this.getCooldown() < this.getMaxCooldown() / 2) {
            this.girl.setAttacking(true);
        } else {
            this.girl.setAttacking(false);
        }
    }
}
