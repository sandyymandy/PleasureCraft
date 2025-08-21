package com.sandymandy.pleasurecraft.entity.girls;

import com.sandymandy.pleasurecraft.scene.SceneOption;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class LucyEntity extends AbstractGirlEntity{

    public LucyEntity(EntityType<? extends AbstractGirlEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected Item getTameItem() {
        return Items.ALLIUM;
    }

    @Override
    protected String getGirlDisplayName() {
        return "Lucy";
    }

    @Override
    protected String getGirlID() {
        return "lucy";
    }

    @Override
    public int getSizeGUI(){return 29;}

    @Override
    public float getYAxisGUI(){return 0.0525F;}

    @Override
    public List<SceneOption> getSceneOptions() {
        return List.of(
                new SceneOption("Paizuri",
                        "paizuri_start", List.of("paizuri_slow"),List.of("paizuri_fast"),"paizuri_cum"),
                new SceneOption("Blow Job",
                        "blowjob_start",List.of("blowjob_slow"),List.of("blowjob_fast"),"blowjob_cum")
        );
    }

    @Override
    protected Map<EquipmentSlot, List<String>> getClothingBones() {
        return super.getClothingBones();
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 20)
                .add(EntityAttributes.MOVEMENT_SPEED, .20)
                .add(EntityAttributes.TEMPT_RANGE, 5)
                .add(EntityAttributes.ATTACK_DAMAGE, 2);


    }


}