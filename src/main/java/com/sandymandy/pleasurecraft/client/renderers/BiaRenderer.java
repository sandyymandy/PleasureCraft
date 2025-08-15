package com.sandymandy.pleasurecraft.client.renderers;

import com.sandymandy.pleasurecraft.client.models.BiaModel;
import com.sandymandy.pleasurecraft.entity.girls.BiaEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class BiaRenderer extends AbstractGirlRenderer<BiaEntity> {
    public BiaRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BiaModel());
    }
}
