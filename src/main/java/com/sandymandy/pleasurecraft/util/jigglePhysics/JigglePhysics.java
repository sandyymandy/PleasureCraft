package com.sandymandy.pleasurecraft.util.jigglePhysics;

import net.minecraft.util.math.Vec3d;

public class JigglePhysics {
    private Vec3d velocity = Vec3d.ZERO;
    private Vec3d displacement = Vec3d.ZERO;

    private final double stiffness;
    private final double damping;

    public JigglePhysics(double stiffness, double damping) {
        this.stiffness = stiffness;
        this.damping = damping;
    }

    public void update(Vec3d force) {
        // Apply force like player movement or bone inertia
        Vec3d acceleration = force.subtract(displacement.multiply(stiffness)).subtract(velocity.multiply(damping));
        velocity = velocity.add(acceleration);
        displacement = displacement.add(velocity);
    }

    public Vec3d getDisplacement() {
        return displacement;
    }

}
