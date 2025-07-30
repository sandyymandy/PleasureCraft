package com.sandymandy.pleasurecraft.entity.bia.client;

import com.sandymandy.pleasurecraft.util.entity.client.AbstractGirlRenderer;
import com.sandymandy.pleasurecraft.entity.bia.BiaEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class BiaRenderer extends AbstractGirlRenderer<BiaEntity> {
    public BiaRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BiaModel());
    }
}
