package com.sandymandy.pleasurecraft.entity.girls;

import com.mojang.authlib.GameProfile;
import com.sandymandy.pleasurecraft.PleasureCraft;
import com.sandymandy.pleasurecraft.entity.ai.goal.ConditionalGoal;
import com.sandymandy.pleasurecraft.entity.ai.goal.GirlAttackGoal;
import com.sandymandy.pleasurecraft.entity.ai.goal.StopMovementGoal;
import com.sandymandy.pleasurecraft.network.girls.AnimationSyncC2SPacket;
import com.sandymandy.pleasurecraft.network.girls.BonePosSyncC2SPacket;
import com.sandymandy.pleasurecraft.network.girls.ClothingArmorVisibilityS2CPacket;
import com.sandymandy.pleasurecraft.network.girls.NextSceneAnimationC2SPacket;
import com.sandymandy.pleasurecraft.scene.SceneStateManager;
import com.sandymandy.pleasurecraft.screen.GirlScreenHandlerFactory;
import com.sandymandy.pleasurecraft.util.Messages;
import com.sandymandy.pleasurecraft.util.inventory.GirlInventory;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;

import java.util.*;


public abstract class AbstractGirlEntity extends TameableEntity implements GeoEntity {
    private static final TrackedData<Boolean> SITTING = DataTracker.registerData(AbstractGirlEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> STRIPPED = DataTracker.registerData(AbstractGirlEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> FOLLOWING = DataTracker.registerData(AbstractGirlEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IN_SCENE = DataTracker.registerData(AbstractGirlEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<String> OVERRIDE_ANIM = DataTracker.registerData(AbstractGirlEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Boolean> OVERRIDE_LOOP = DataTracker.registerData(AbstractGirlEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> OVERRIDE_HOLD = DataTracker.registerData(AbstractGirlEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Float> SCENE_PROGRESS = DataTracker.registerData(AbstractGirlEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final SceneStateManager sceneManager = new SceneStateManager(this);
    public Map<String, Boolean> boneVisibility = new HashMap<>();
    public Map<String, Identifier> boneTextureOverrides = new HashMap<>();
    public Map<String, Identifier> playerTexture = new HashMap<>();
    public Map<String, Vec2f> boneUVOffsets = new HashMap<>();
    public final Map<EquipmentSlot, Boolean> clothingVisibility = new EnumMap<>(EquipmentSlot.class);
    public final Map<EquipmentSlot, Boolean> armorVisibility = new EnumMap<>(EquipmentSlot.class);
    private final GirlInventory inventory = GirlInventory.ofSize();
    private BlockPos basePos;
    private LivingEntity attackTarget;
    private int ticksSinceLastHit;
    private static final int MAX_TICKS_NO_HIT = 20 * 20;
    public float previousYaw = 0;
    public Vec3d previousVelocity = Vec3d.ZERO;
    private boolean freeze = false;
    public Vec3d clientPassengerBonePos = Vec3d.ZERO;
    public Vec3d serverPassengerBonePos = Vec3d.ZERO;
    public boolean showHiddenBones = false;
    public final int maxRelationshipLevel = 3;
    public int currentRelationshipLevel;
    private String currentAnimState = "idle";
    private boolean currentLoopState = false;
    private boolean currentHoldState = false;

    protected Item getTameItem() {
        return Items.DANDELION;
    }

    protected String getGirlDisplayName() {
        return "Null";
    }

    protected String getGirlID() {
        return "null";
    }

    public int getSizeGUI(){return 20;}

    public float getYAxisGUI(){return 0.0625F;}


    protected Map<EquipmentSlot, List<String>> getClothingBones() {
        Map<EquipmentSlot, List<String>> clothing = new HashMap<>();

        clothing.put(EquipmentSlot.HEAD, new ArrayList<>());

        clothing.put(EquipmentSlot.CHEST, new ArrayList<>());

        clothing.put(EquipmentSlot.LEGS, new ArrayList<>());

        clothing.put(EquipmentSlot.FEET, new ArrayList<>());

        return clothing;
    }
    // Armor bones mapped by EquipmentSlot

    protected Map<EquipmentSlot, List<String>> getArmorBones() {
        Map<EquipmentSlot, List<String>> armor = new HashMap<>();

        armor.put(EquipmentSlot.HEAD, new ArrayList<>(List.of(
                "armorHelmet"
        )));

        armor.put(EquipmentSlot.CHEST, new ArrayList<>(List.of(
                "armorBoobs",
                "armorChest",
                "armorShoulderL",
                "armorShoulderR"
        )));

        armor.put(EquipmentSlot.LEGS, new ArrayList<>(List.of(
                "armorHip",
                "armorPantsLowL",
                "armorPantsUpL",
                "armorPantsLowR",
                "armorPantsUpR",
                "armorBootyL",
                "armorBootyR"
        )));

        armor.put(EquipmentSlot.FEET, new ArrayList<>(List.of(
                "armorShoesL",
                "armorShoesR"
        )));

        return armor;
    }


    protected AbstractGirlEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(SITTING, false);
        builder.add(STRIPPED, false);
        builder.add(FOLLOWING, true);
        builder.add(IN_SCENE, false);
        builder.add(OVERRIDE_ANIM,"");
        builder.add(OVERRIDE_LOOP, false);
        builder.add(OVERRIDE_HOLD, false);
        builder.add(SCENE_PROGRESS,0f);
    }


    public GirlInventory getInventory() {
        return inventory;
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return List.of(
                inventory.getArmorStack(EquipmentSlot.FEET),
                inventory.getArmorStack(EquipmentSlot.LEGS),
                inventory.getArmorStack(EquipmentSlot.CHEST),
                inventory.getArmorStack(EquipmentSlot.HEAD)
        );
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return inventory.getArmorStack(slot);
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {
        inventory.setArmorStack(slot, stack);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(-1, new ConditionalGoal(new StopMovementGoal(this), () -> isFrozenInPlace()));
        this.goalSelector.add(0, new SitGoal(this));
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new TameableEscapeDangerGoal(1.5D, DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));
        this.goalSelector.add(3, new GirlAttackGoal(this, 1.5, false));
        this.goalSelector.add(4, new ConditionalGoal(new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F), () -> isFollowing()));
        this.goalSelector.add(5, new TemptGoal(this, 1.25D, Ingredient.ofItems(getTameItem()), false));
        this.goalSelector.add(6, new WanderAroundGoal(this, 1.0D));
        this.goalSelector.add(7, new ConditionalGoal(new LookAtEntityGoal(this, PlayerEntity.class, 6.0F),() -> !isFrozenInPlace()));
        this.goalSelector.add(8, new ConditionalGoal(new LookAroundGoal(this),() -> !isFrozenInPlace()));
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, new RevengeGoal(this, PlayerEntity.class));
    }


    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item itemInHand = itemStack.getItem();
        if (this.isTamed()) {

            if (this.isFoodItem(itemStack) && this.getHealth() < this.getMaxHealth()) {
                this.getNavigation().findPathTo(player, 20);
                this.eat(player, hand, itemStack);
                FoodComponent foodComponent = itemStack.get(DataComponentTypes.FOOD);
                float f = foodComponent != null ? foodComponent.nutrition() : 1.0F;
                this.heal(2.0F * f);
                return ActionResult.SUCCESS;
            }

            if(this.isOwner(player)) {
                ActionResult actionResult = super.interactMob(player, hand);
                if (itemStack.equals(ItemStack.EMPTY)){
                    if (player.isSneaking()) {
                        this.setSit(!this.isSittingdown());
                        this.jumping = false;
                        this.navigation.stop();
                        this.setTarget(null);
                        return ActionResult.SUCCESS.noIncrementStat();
                    }
                }
                else if (!actionResult.isAccepted() && !player.isSneaking()) {
                    player.openHandledScreen(new GirlScreenHandlerFactory(this));
                    return ActionResult.SUCCESS;
                }

                return actionResult;

            }else {
                if ((itemInHand.equals(getTameItem()))) {
                    player.sendMessage(Text.literal("She's Already In A Relationship With Someone"), true);
                    return ActionResult.FAIL;
                }
            }
        } else {

            if (itemStack.isEmpty() && player.isSneaking()) {
                this.getNavigation().findPathTo(player, 20);
                player.openHandledScreen(new GirlScreenHandlerFactory(this));
                return ActionResult.SUCCESS;
            }

            if (!this.getWorld().isClient) {
                if (itemInHand.equals(getTameItem()) && !player.isSneaking()) {
                    itemStack.decrementUnlessCreative(1, player);
                    this.tryTame(player);
                    return ActionResult.SUCCESS;
                } else {
                    // Wrong item OR empty hand (not sneaking)
                    player.sendMessage(Text.literal(
                            "She ignores you. Maybe try giving her a " + getReadableTameItemName() + "."
                    ), true);
                    return ActionResult.FAIL;
                }
            }


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
        return this.dataTracker.get(STRIPPED);
    }

    public void setFreeze(boolean locked) {
        this.freeze = locked;
    }

    public boolean isFrozenInPlace() {
        return this.freeze;
    }

    public void setSceneState(boolean inScene) {
        this.dataTracker.set(IN_SCENE, inScene);
    }

    public boolean isSceneActive() {
        return this.dataTracker.get(IN_SCENE);
    }

    public void setOverrideAnim(String anim){
        this.dataTracker.set(OVERRIDE_ANIM, anim);
    }

    public String getOverrideAnim(){
        return this.dataTracker.get(OVERRIDE_ANIM);
    }

    public void setOverrideLoop(boolean loop){
        this.dataTracker.set(OVERRIDE_LOOP, loop);
    }

    public boolean getOverrideLoop(){
        return this.dataTracker.get(OVERRIDE_LOOP);
    }

    public void setOverrideHold(boolean hold){
        this.dataTracker.set(OVERRIDE_HOLD, hold);
    }

    public boolean getOverrideHold(){
        return this.dataTracker.get(OVERRIDE_HOLD);
    }

    public void setSceneProgress(float progress){
        this.dataTracker.set(SCENE_PROGRESS, progress);
    }

    public float getSceneProgress(){
        return this.dataTracker.get(SCENE_PROGRESS);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    public boolean isFoodItem(ItemStack stack) {
        return stack.isIn(ItemTags.WOLF_FOOD);
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

    public void toggleModelBones(List<String> bones, boolean visible){
        if(!getWorld().isClient){
            return;
        }

        if (this.boneVisibility == null) {
            this.boneVisibility = new HashMap<>();
        }


        for (String boneName : bones) {
            this.boneVisibility.put(boneName, visible);
        }
    }


    public void overrideBoneTexture(String boneName, Identifier texture) {
        if (this.boneTextureOverrides == null) this.boneTextureOverrides = new HashMap<>();
        this.boneTextureOverrides.put(boneName, texture);
    }

    public void overrideBoneUV(List<String> bones, float uOffset, float vOffset) {
        if (this.boneUVOffsets == null) this.boneUVOffsets = new HashMap<>();

        for (String boneName : bones) {
            this.boneUVOffsets.put(boneName, new Vec2f(uOffset, vOffset));
        }
    }

    @Nullable
    public Vec2f getBoneUVOffset(String boneName) {
        if (boneUVOffsets != null && boneUVOffsets.containsKey(boneName)) {
            return boneUVOffsets.get(boneName);
        }
        return null;
    }

    @Nullable
    public Identifier getBoneTexture(String boneName) {
        if (boneTextureOverrides != null && boneTextureOverrides.containsKey(boneName)) {
            return boneTextureOverrides.get(boneName);
        }
        return null;
    }

    public Map<String, Identifier> getBoneTextureOverrides() {
        return this.boneTextureOverrides;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 4, this::handleAnimations));
    }

    private <T extends GeoAnimatable> PlayState handleAnimations(AnimationState<T> state) {
        AnimationController<?> controller = state.getController();

        String defaultAnim = getDefaultAnimation(state);
        String overrideAnim = this.getOverrideAnim();
        boolean overrideLoop = this.getOverrideLoop();
        boolean overrideHold = this.getOverrideHold();

        // 1. Forced animation override
        if (overrideAnim != null && !overrideAnim.isEmpty()) {
            this.currentAnimState = overrideAnim;
            this.currentLoopState = overrideLoop;
            this.currentHoldState = overrideHold;


            // End override if it was one-shot and finished playing
            if (!overrideLoop && (controller.getAnimationState() == AnimationController.State.STOPPED || controller.getAnimationState() == AnimationController.State.PAUSED)) {
                if (isSceneActive()){
                    ClientPlayNetworking.send(new NextSceneAnimationC2SPacket(this.getId(),this.currentAnimState));
                }
                else {
                    stopOverrideAnimations();
                }

            }
        }
        else {
            this.currentAnimState = defaultAnim;
            this.currentLoopState = true;
        }

        Animation.LoopType loopType;

        if (this.currentLoopState) {
            loopType = Animation.LoopType.LOOP;
        } else if (this.currentHoldState) {
            loopType = Animation.LoopType.HOLD_ON_LAST_FRAME;
        } else {
            loopType = Animation.LoopType.PLAY_ONCE;
        }


        controller.setAnimation(RawAnimation.begin().then
                (getAnimationPath(this.currentAnimState), loopType));
        return PlayState.CONTINUE;

    }

    private String getDefaultAnimation(AnimationState<?> state) {
        if (!this.isOnGround() && !isSittingdown()) return "fly";
        if (state.isMoving() && !isSittingdown()) return "walk";
        if (isSittingdown()) return "sit";
        return "idle";
    }



    // Call this to force an animation
    public void playAnimation(String animationName, boolean loop, boolean holdOnLastFrame) {
        if (!this.getWorld().isClient) { // run only on server
            this.setOverrideAnim(animationName != null ? animationName : "");
            this.setOverrideLoop(loop);
            this.setOverrideHold(holdOnLastFrame);
        } else {
            ClientPlayNetworking.send(new AnimationSyncC2SPacket(this.getId(),
                    animationName != null ? animationName : "",
                    loop,
                    holdOnLastFrame));
        }
    }

    public void stopOverrideAnimations() {
        ClientPlayNetworking.send(new AnimationSyncC2SPacket(this.getId(), "",false,false));
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
        this.getSceneManager().tick();
        previousYaw = getYaw();
        previousVelocity = getVelocity();
        updateClothingAndArmor();
        applySkinToBone((PlayerEntity) this.getFirstPassenger());
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


    private void updateClothingAndArmor() {
        if (this.getWorld().isClient()) return;

        boolean stripped = isStripped();

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            clothingVisibility.put(slot, !stripped && this.inventory.getArmorStack(slot).isEmpty());
            armorVisibility.put(slot, !stripped && !this.inventory.getArmorStack(slot).isEmpty());


        }

        // --- send packet to client ---
        List<Boolean> clothingList = Arrays.stream(EquipmentSlot.values())
                .map(s -> clothingVisibility.getOrDefault(s, false))
                .toList();

        List<Boolean> armorList = Arrays.stream(EquipmentSlot.values())
                .map(s -> armorVisibility.getOrDefault(s, false))
                .toList();

        ClothingArmorVisibilityS2CPacket packet =
                new ClothingArmorVisibilityS2CPacket(this.getId(), clothingList, armorList);

        for (ServerPlayerEntity player : Objects.requireNonNull(this.getServer()).getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, packet);
        }

    }

    public void applyClothingAndArmor() {
        if (!this.getWorld().isClient()) return;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            List<String> clothingBones = getClothingBones().get(slot);
            if (clothingBones != null) {
                toggleModelBones(clothingBones, clothingVisibility.getOrDefault(slot, false));
            }

            List<String> armorBones = getArmorBones().get(slot);
            if (armorBones != null) {
                toggleModelBones(armorBones, armorVisibility.getOrDefault(slot, false));
            }

            if (!this.inventory.getArmorStack(slot).isEmpty()) {
                String typeName = this.inventory.getArmorStack(slot).getItem().toString().toLowerCase(); // get item name
                float uv = getArmorU(typeName);
                this.overrideBoneUV(this.getArmorBones().get(slot),uv,0);
            }
        }



        // Naked bits
        toggleModelBones(List.of("vagina"), isStripped());
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

    public void handlePassengerBone(GeoBone bone){
        if (bone != null) {
            Vector3d bonePos = bone.getWorldPosition();
            this.clientPassengerBonePos = new Vec3d(bonePos.x, bonePos.y, bonePos.z);
            ClientPlayNetworking.send(new BonePosSyncC2SPacket(this.getId(), this.clientPassengerBonePos));

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
            return this.getPos().add(0,this.getSceneManager().passengerYOffset,0);
        }
        else {
            return this.getPassengerBone().add(0,this.getSceneManager().passengerYOffset,0);
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
    protected void dropInventory(ServerWorld world) {
        super.dropInventory(world); // calls standard drop logic
        for (ItemStack stack : this.getInventory().getItems()) {
            if (!stack.isEmpty()) {
                this.dropStack(world,stack);
            }
        }
        this.getInventory().clear();
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
            new Messages().GlobleMessage(
                    this.getWorld(),
                    getGirlDisplayName() + " died and respawned at base: " +
                            respawnPos.getX() + ", " +
                            respawnPos.getY() + ", " +
                            respawnPos.getZ()
            );

            // Drops inventory as if she died
            this.dropInventory(world);

            // If basePos was null, make sure to set it now so future hits won’t NPE
            if (this.basePos == null) {
                this.basePos = respawnPos;
            }
            teleportToBase();

            return false;
        }
        else if(isFrozenInPlace() &! damageType.equals("outOfWorld") || damageType.equals("genericKill")){
            if(!this.hasPassengers()){
                new Messages().GlobleMessage(
                        this.getWorld(),getGirlDisplayName() + " is busy at the moment");
            }
            return false;
        }
        else{
            return super.damage(world, source, amount);
        }
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

    public void messageAsEntity(String message){
        this.messageAsEntity(null,message);
    }

    public void messageAsEntity(@Nullable PlayerEntity playerEntity, String message){
        if(this.getWorld().isClient()) return;
        String finalMessage = "<"+getGirlDisplayName()+"> " + message;

        if(playerEntity == null){
            new Messages().GlobleMessage(this.getWorld(), finalMessage);
        }
        else {
            new Messages().PlayerSpecificMessage(playerEntity,finalMessage);
        }

    }

    private String getAnimationPath(String animation){
        return "animation." + this.getGirlID() + "." + animation;
    }

    private float getArmorU(String armorType){
        if (armorType.contains("turtle")) return 0.10546875f;
        if (armorType.contains("leather")) return 0.0703125f;
        if (armorType.contains("iron")) return 0.03515625f;
        if (armorType.contains("chain")) return 0.052734375f;
        if (armorType.contains("gold")) return 0.0176f;
        if (armorType.contains("netherite")) return 0.087890625f;
        return 0f;
    }

    public void applySkinToBone(PlayerEntity player) {
        Identifier texture;

        // If the playerTexture is null then initialize it
        if (this.playerTexture == null) this.playerTexture = new HashMap<>();

        // Set the base of the player model to Steve so if there isn't a player it has a fallback
        this.overrideBoneTexture("steve", Identifier.of(PleasureCraft.MOD_ID, "textures/player/steve.png"));

        if (player != null) {
            MinecraftClient client = MinecraftClient.getInstance();
            PlayerSkinProvider skinProvider = client.getSkinProvider();

            GameProfile profile = player.getGameProfile();

            // Get the skin identifier
            texture = skinProvider.getSkinTextures(profile).texture();

            // if isn't null set the player texture
            if (texture != null) {
                this.playerTexture.put("steve", texture);
            }
        }
    }
}
