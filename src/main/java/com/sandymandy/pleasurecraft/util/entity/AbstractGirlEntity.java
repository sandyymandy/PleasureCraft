package com.sandymandy.pleasurecraft.util.entity;

import com.sandymandy.pleasurecraft.scene.SceneStateManager;
import com.sandymandy.pleasurecraft.screen.GirlScreenHandlerFactory;
import com.sandymandy.pleasurecraft.util.BonePosSyncPacket;
import com.sandymandy.pleasurecraft.util.GlobleMessages;
import com.sandymandy.pleasurecraft.util.entity.ai.ConditionalGoal;
import com.sandymandy.pleasurecraft.util.entity.ai.StopMovementGoal;
import com.sandymandy.pleasurecraft.util.inventory.GirlInventory;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.cache.object.GeoBone;

import java.util.HashMap;
import java.util.Map;


public abstract class AbstractGirlEntity extends TameableEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final SceneStateManager sceneManager = new SceneStateManager(this);
    public Map<String, Boolean> boneVisibility = new HashMap<>();
    public Map<String, Boolean> boneVisibilityExcludeChildren = new HashMap<>();
    public Map<String, Identifier> boneTextureOverrides = new HashMap<>();
    public boolean showHiddenBones = false;
    public final int maxRelationshipLevel = 3;
    public int currentRelationshipLevel;
    private static final TrackedData<Boolean> SITTING = DataTracker.registerData(AbstractGirlEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> STRIPPED = DataTracker.registerData(AbstractGirlEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> FOLLOWING = DataTracker.registerData(AbstractGirlEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IN_SCENE = DataTracker.registerData(AbstractGirlEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private final GirlInventory inventory = GirlInventory.ofSize();
    private BlockPos basePos;
    private LivingEntity attackTarget;
    private int ticksSinceLastHit;
    private static final int MAX_TICKS_NO_HIT = 20 * 20;
    public float previousYaw = 0;
    public Vec3d previousVelocity = Vec3d.ZERO;
    private boolean freeze = false;
    public Vec3d clientPassengerBonePos = Vec3d.ZERO;;
    public Vec3d serverPassengerBonePos = Vec3d.ZERO;
    protected Item getTameItem() {
        return Items.DANDELION;
    }

    protected String getGirlDisplayName() {
        return "Null";
    }

    protected String getGirlID() {
        return "null";
    }

    protected String getClothingBones() {
        return "bra";
    }

    protected String getArmorBones() {
        return "armorBoobs, armorBootyL, armorBootyR, armorChest, armorHip, armorPantsLowL, armorShoesL, armorPantsUpL, armorPantsLowR, armorShoesR, armorPantsUpR, armorShoulderL, armorShoulderR, armorHelmet";
    }


    protected AbstractGirlEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(SITTING, false);
        builder.add(STRIPPED, false);
        builder.add(FOLLOWING, false);
        builder.add(IN_SCENE, false);
    }


    public GirlInventory getInventory() {
        return inventory;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(-1, new ConditionalGoal(new StopMovementGoal(this), () -> isFrozenInPlace()));
        this.goalSelector.add(0, new SitGoal(this));
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new TameableEscapeDangerGoal(1.5D, DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));
        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.5, true));
        this.goalSelector.add(4, new ConditionalGoal(new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F), () -> isFollowing()));
        this.goalSelector.add(5, new TemptGoal(this, 1.25D, Ingredient.ofItems(getTameItem()), false));
        this.goalSelector.add(6, new WanderAroundGoal(this, 1.0D));
        this.goalSelector.add(7, new ConditionalGoal(new LookAtEntityGoal(this, PlayerEntity.class, 6.0F),() -> !isFrozenInPlace()));
        this.goalSelector.add(8, new ConditionalGoal(new LookAroundGoal(this),() -> !isFrozenInPlace()));
//        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, new RevengeGoal(this, PlayerEntity.class));
    }



    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item itemInHand = itemStack.getItem();


        if (!this.getWorld().isClient && !this.isTamed() && itemInHand.equals(getTameItem()) && !player.isSneaking()) {
            itemStack.decrementUnlessCreative(1, player);
            this.tryTame(player);
            return ActionResult.SUCCESS;
        }
        else if (!this.getWorld().isClient && !this.isTamed() && !itemInHand.equals(getTameItem()) && !player.isSneaking()) {
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_VILLAGER_ANGRY_PARTICLES);
            player.sendMessage(Text.literal("She ignores you. Maybe try giving her a " + getReadableTameItemName() + "."), true);
            return ActionResult.FAIL;
        }

        if (!this.getWorld().isClient && !this.isTamed() && itemStack.equals(ItemStack.EMPTY) && player.isSneaking()) {
            player.openHandledScreen(new GirlScreenHandlerFactory(this));
            return ActionResult.SUCCESS;
        }

        if(!this.getWorld().isClient && this.isTamed() &! isOwner(player) && (hand.equals(Hand.MAIN_HAND) || itemInHand.equals(getTameItem()))){
            player.sendMessage(Text.literal("She's Already In A Relationship With Someone" ), true);
            return ActionResult.FAIL;
        }

        if (!this.getWorld().isClient && isOwner(player) && this.isTamed()){
            if (player.isSneaking()) {
                player.openHandledScreen(new GirlScreenHandlerFactory(this));
                this.getNavigation().stop();
                return ActionResult.SUCCESS;
            }else if(!player.isSneaking()){
                this.setSit(!isSittingdown());
                return ActionResult.SUCCESS;
            }
        }

        if (!this.getWorld().isClient && isOwner(player) && this.isTamed() && itemInHand.equals(Items.COAL)) {
            this.breakUp(player);
            return ActionResult.SUCCESS;
        }

        if (itemInHand.equals(getTameItem()) && this.isTamed()) {
            return ActionResult.PASS;
        }

        return super.interactMob(player, hand);
    }

    private void tryTame(PlayerEntity player) {
        if (this.random.nextInt(3) == 0) {
            this.setOwner(player);
            this.navigation.stop();
            setTarget(null);
            setSit(true);
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
            player.sendMessage(Text.literal("You Asked " + getGirlDisplayName() + " Out And She Said §aYes" ), true);
            this.setBasePosHere();
        } else {
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_NEGATIVE_PLAYER_REACTION_PARTICLES);
            player.sendMessage(Text.literal("You Asked " + getGirlDisplayName() + " Out And She Said §cNo"), true);
        }
    }

    public void setSit(boolean sitting) {
        this.dataTracker.set(SITTING, sitting);
        this.setTarget(null);
        this.calculateDimensions();
        super.setSitting(sitting);
    }

    public boolean isSittingdown() {
        return this.dataTracker.get(SITTING);
    }

    public void setFollowing(boolean follow) {
        this.dataTracker.set(FOLLOWING, follow);
    }

    public boolean isFollowing() {
        return this.dataTracker.get(FOLLOWING);
    }

    public void setStripped(boolean stripped) {
        this.dataTracker.set(STRIPPED, stripped);
    }

    public boolean isStripped() {
        return dataTracker.get(STRIPPED);
    }

    public void setFreeze(boolean locked) {
        this.freeze = locked;
    }

    public boolean isFrozenInPlace() {
        return this.freeze;
    }

    public void setSceneState(boolean inScene) {
        dataTracker.set(IN_SCENE, inScene);
    }

    public boolean isSceneActive() {
        return dataTracker.get(IN_SCENE);
    }


    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public boolean shouldRenderName() {
        this.setCustomName(Text.of(getGirlDisplayName()));
        this.setCustomNameVisible(true);
        return true;
    }

    @Override
    protected void dropInventory(ServerWorld world) {
        super.dropInventory(world); // calls standard drop logic

        for (ItemStack stack : this.getInventory().getItems()) {
            if (!stack.isEmpty()) {
                this.dropStack(world,stack);
            }
        }
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        if (this.isInvulnerableTo(world, source)) return false;

        String damageType = source.getName();
        // If killed by /kill or void, allow normal death
        if (damageType.equals("outOfWorld") || damageType.equals("genericKill")) {
            return super.damage(world, source, amount);
        }

        if(this.isTamed() && (this.getHealth() - amount <= 0.0F) &! (damageType.equals("outOfWorld") || damageType.equals("genericKill") || isFrozenInPlace())) {
            this.setHealth(getMaxHealth());
            // If basePos is still null, fall back to current position
            BlockPos respawnPos = (this.basePos != null)
                    ? this.basePos
                    : this.getBlockPos();

            // Send a message referencing whichever Pos we have
            new GlobleMessages().GlobleMessage(
                    this.getWorld(),
                    getGirlDisplayName() + " died and respawned at base: " +
                            respawnPos.getX() + ", " +
                            respawnPos.getY() + ", " +
                            respawnPos.getZ()
            );

            // If basePos was null, make sure to set it now so future hits won’t NPE
            if (this.basePos == null) {
                this.basePos = respawnPos;
            }
            teleportToBase();

            return false;
        }
        else if(isFrozenInPlace() &! damageType.equals("outOfWorld") || damageType.equals("genericKill")){
            if(!this.hasPassengers()){
                new GlobleMessages().GlobleMessage(
                        this.getWorld(),getGirlDisplayName() + " is busy at the moment");
            }
            return false;
        }
        else{
            return super.damage(world, source, amount);
        }
    }

    public void breakUp(PlayerEntity player) {
        if(!player.getWorld().isClient){
            this.setTamed(false,true); // Mark the entity as untamed
            this.setOwnerUuid(null); // Remove the owner UUID
            this.setSit(false); // Ensure the entity is not sitting
            this.setStripped(false);
            if(!isTamed() && !isOwner(player)){
                player.sendMessage(Text.literal("§cYou Broke Up With " + getGirlDisplayName()), true);
            }
        }
    }

    public void setBasePosHere(){
        this.basePos = this.getBlockPos();
    }

    public void setBasePos(BlockPos pos) {
        this.basePos = pos;
    }

    public BlockPos getBasePos(){
        return basePos;
    }

    public void teleportToBase() {
        if (this.basePos != null && this.getWorld() != null) {
            setSit(true);
            this.teleport(this.basePos.getX(), this.basePos.getY(), this.basePos.getZ(), false);
        }
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        if (attackTarget != null) {
            ticksSinceLastHit++;

            if (ticksSinceLastHit >= MAX_TICKS_NO_HIT) {
                // Lost interest — stop attacking
                this.setTarget(null);
                attackTarget = null;
                ticksSinceLastHit = 0;
            }
        }
    }


    private void clothingLogic(){
        toggleModelBones(getClothingBones(), !isStripped() && isBoneVisible(getClothingBones()));
        toggleModelBones(getArmorBones(), !isStripped());
        toggleModelBones("vagina", isStripped());
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);

        if (target != null) {
            attackTarget = target;
            ticksSinceLastHit = 0; // reset countdown on new target
        } else {
            attackTarget = null;
            ticksSinceLastHit = 0;
        }
    }

    @Override
    public boolean tryAttack(ServerWorld world, Entity target) {
        boolean success = super.tryAttack(world, target);

        if (success && target == attackTarget) {
            ticksSinceLastHit = 0; // reset timer on successful hit
        }

        return success;
    }

    public void toggleModelBones(String bones, boolean visible) {
        toggleModelBones(bones, visible, false);
    }

    public void toggleModelBones(String bones, boolean visible, @Nullable boolean excludeChildren){
        if(!getWorld().isClient){
            return;
        }

        String[] boneArray = bones.replaceAll("\\s+", "").split(",");

        if (this.boneVisibility == null) {
            this.boneVisibility = new HashMap<>();
        }

        if (this.boneVisibilityExcludeChildren == null){
            this.boneVisibilityExcludeChildren = new HashMap<>();
        }

        for (String boneName : boneArray) {
            if(excludeChildren){
                this.boneVisibilityExcludeChildren.put(boneName, visible);
            }else {
                this.boneVisibility.put(boneName, visible);
            }
        }
    }

    public void overrideBoneTextures(String bones, Identifier texture) {
        if (!this.getWorld().isClient) return;

        String[] boneArray = bones.replaceAll("\\s+", "").split(",");
        for (String boneName : boneArray) {
            boneTextureOverrides.put(boneName, texture);
        }
    }

    public Map<String, Identifier> getBoneTextureOverrides() {
        return this.boneTextureOverrides;
    }

    public boolean isBoneVisible(String boneName) {
        return boneVisibility.getOrDefault(boneName, true); // default to visible
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 3, this::predicate)); // sets the transition length to the next animation as 3 in game ticks
    }

    private <girlEntity extends GeoAnimatable> PlayState predicate(AnimationState<girlEntity> girlEntityAnimationState) {

        AnimationController<?> controller = girlEntityAnimationState.getController();


        // Handle override animation
        if (overrideAnimation != null) {
            controller.setAnimation(RawAnimation.begin().then(overrideAnimation, overrideLoop ? Animation.LoopType.LOOP : Animation.LoopType.PLAY_ONCE));

            // If it's not looping, clear it after one play
            if (!overrideLoop && controller.getAnimationState() == AnimationController.State.STOPPED) {
                overrideAnimation = null;
            }

            return PlayState.CONTINUE;
        }


        if(!sceneManager.isInScene()){
            if (!this.isOnGround() & !isSittingdown()) {
                girlEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation." + getGirlID() + ".fly", Animation.LoopType.LOOP));
                return PlayState.CONTINUE;
            }

            if (girlEntityAnimationState.isMoving() & !isSittingdown()) {
                girlEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation." + getGirlID() + ".walk", Animation.LoopType.LOOP));
                return PlayState.CONTINUE;
            }

            if (isSittingdown()) {
                girlEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation." + getGirlID() + ".sit", Animation.LoopType.LOOP));
                return PlayState.CONTINUE;
            }

            girlEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation."+ getGirlID() +".idle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        else{
            if(this.getGirlID().equals("bia")){
                girlEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation." + getGirlID() + ".prone_doggy_soft", Animation.LoopType.LOOP));
                return PlayState.CONTINUE;
            }else {
                girlEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("animation." + getGirlID() + ".paizuri_fast", Animation.LoopType.LOOP));
                return PlayState.CONTINUE;
            }
        }





    }

    public String overrideAnimation = null;
    public boolean overrideLoop = false; // Used for forced one-shot animations (e.g., strip)


    public void playAnimation(String animationName, boolean loop) {
        this.overrideAnimation = animationName;
        this.overrideLoop = loop;

/*        if (!this.getWorld().isClient) {
            GirlAnimationSyncS2CPacket.send(this, animationName, loop);
      }*/
    }



    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        RegistryWrapper.WrapperLookup registryLookup = this.getWorld().getRegistryManager();
        Inventories.writeNbt(nbt, this.inventory.getItems(), registryLookup);
        nbt.putBoolean("SitSate", this.isSittingdown());
        nbt.putBoolean("StripState", this.isStripped());
        nbt.putBoolean("SceneState", this.isSceneActive());
        nbt.putBoolean("FrozenState", this.isFrozenInPlace());


        if (this.basePos != null) {
            nbt.putInt("BaseX", this.basePos.getX());
            nbt.putInt("BaseY", this.basePos.getY());
            nbt.putInt("BaseZ", this.basePos.getZ());
        }

    }


    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        RegistryWrapper.WrapperLookup registryLookup = this.getWorld().getRegistryManager();
        Inventories.readNbt(nbt, this.inventory.getItems(), registryLookup);
        this.setSit(nbt.getBoolean("SitSate"));
        this.setStripped(nbt.getBoolean("StripState"));
        if (nbt.contains("BaseX") && nbt.contains("BaseY") && nbt.contains("BaseZ")) {
            int x = nbt.getInt("BaseX");
            int y = nbt.getInt("BaseY");
            int z = nbt.getInt("BaseZ");
            this.basePos = new BlockPos(x, y, z);
        }
    }


    public void tick() {
        super.tick();
        sceneManager.tick();
        previousYaw = getYaw();
        previousVelocity = getVelocity();
        clothingLogic();


    }

    public void handlePassengerBone(GeoBone bone){
        if (bone != null) {
            Vector3d bonePos = bone.getWorldPosition();
            this.clientPassengerBonePos = new Vec3d(bonePos.x, bonePos.y, bonePos.z);
            ClientPlayNetworking.send(new BonePosSyncPacket(this.getId(), this.clientPassengerBonePos));

        }


    }

    public void cachePassengerBone(boolean valid,Vec3d pos){
        if (valid) {
            this.serverPassengerBonePos = pos;

        }
    }

    public Vec3d getPassengerBone(){
        if(this.getWorld().isClient()){
            return clientPassengerBonePos;
        }
        else {
            return serverPassengerBonePos;
        }
    }



    @Override
    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        return this.getPassengerBone().add(0,1,0);
    }

    @Override
    public Vec3d getPassengerRidingPos(Entity passenger) {
        /*This is not based on the local coordinates from the entity. it is the global coordinates based on the world. It also has a base offset of -0.6 on the Y Axis
        * This is also based on the client not the server*/
        if(this.getPassengerBone() == Vec3d.ZERO){
            return this.getPos().add(0,sceneManager.passengerYOffset,0);
        }
        else {
            return this.getPassengerBone().add(0,sceneManager.passengerYOffset,0);
        }
    }

    public SceneStateManager getSceneManager() {
        return sceneManager;
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    @Override
    public void pushAwayFrom(Entity entity) {
        if (!freeze) {
            super.pushAwayFrom(entity);
        }
    }

    @Override
    public void takeKnockback(double strength, double x, double z) {
        if (!this.freeze) {
            super.takeKnockback(strength, x, z);
        }
    }

    @Override
    public boolean isPushable() {
        return !freeze;
    }


    @Override
    public void addVelocity(double dx, double dy, double dz) {
        if (!freeze) {
            super.addVelocity(dx, dy, dz);
        }
    }


    public String getReadableTameItemName() {
        Item tameItem = this.getTameItem();
        Identifier id = Registries.ITEM.getId(tameItem);

        if (id != null) {
            String path = id.getPath(); // e.g., "blue_allium"

            // Capitalize each word split by underscores
            String[] words = path.split("_");
            StringBuilder formatted = new StringBuilder();

            for (String word : words) {
                if (!word.isEmpty()) {
                    formatted.append(Character.toUpperCase(word.charAt(0)))
                            .append(word.substring(1))
                            .append(" ");
                }
            }

            return formatted.toString().trim(); // "Blue Allium"
        } else {
            return "Unknown Item";
        }
    }


}
