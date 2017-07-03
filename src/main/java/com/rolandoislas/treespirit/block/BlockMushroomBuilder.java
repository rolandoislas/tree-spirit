package com.rolandoislas.treespirit.block;

import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.registry.ModBlocks;
import com.rolandoislas.treespirit.registry.ModCreativeTabs;
import net.minecraft.block.*;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Rolando on 3/11/2017.
 */
public class BlockMushroomBuilder extends BlockBush implements IGrowable {
	private static final PropertyEnum<EnumMushroomStructure> TYPE = PropertyEnum.create("type", EnumMushroomStructure.class);
	private static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);

	public BlockMushroomBuilder() {
		super();
		this.setUnlocalizedName(TreeSpirit.MODID + ".mushroom_builder");
		this.setRegistryName(TreeSpirit.MODID, "mushroom_builder");
		this.setCreativeTab(ModCreativeTabs.MAIN);
		this.setHardness(1);
		this.setSoundType(SoundType.PLANT);
		this.setTickRandomly(true);
	}

	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE, STAGE);
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
		for (EnumMushroomStructure type : EnumMushroomStructure.values())
			list.add(new ItemStack(itemIn, 1, type.getMeta()));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(TYPE, EnumMushroomStructure.MOB_SPAWNER.getFromMeta(meta));
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(TYPE).getMeta();
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TYPE).getMeta();
	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return true;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		if (worldIn.isRemote)
			return;
		if (state.getValue(STAGE) == 0)
			worldIn.setBlockState(pos, state.cycleProperty(STAGE), 4);
		else {
			switch (state.getValue(TYPE)) {
				case MOB_SPAWNER:
					generateMobSpawner(worldIn, pos, state, rand);
					break;
			}
		}
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.down()).isFullCube();
	}

	@Override
	protected boolean canSustainBush(IBlockState state) {
		return state.isFullCube();
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (worldIn.isRemote)
			return;
		if (!canPlaceBlockAt(worldIn, pos)) {
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (worldIn.isRemote)
			return;
		if (rand.nextInt(7) == 0)
			this.grow(worldIn, rand, pos, state);
	}

	private void generateMobSpawner(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		int chuteHeight = 24 + rand.nextInt(30); // 24 is the drop height for vanilla mobs
		int chuteWidth = 5; // expects uneven
		int headHeight = 2; // no endermen
		int headWidth = 10 + rand.nextInt(20);
		if (pos.getY() + chuteHeight > worldIn.getHeight())
			return;
		// Set mushroom to air
		worldIn.setBlockToAir(pos);
		// Create the steam
		IBlockState stem = Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState()
				.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.ALL_STEM);
		createChute(chuteHeight, chuteWidth, pos, worldIn, stem);
		// Create air at the top of the stem
		createPlatform(pos.up(chuteHeight - 1), chuteWidth - 2, worldIn, ModBlocks.AIR.getDefaultState());
		// Create the head
		IBlockState head = Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState();
		if (rand.nextInt(2) == 0)
			head = Blocks.RED_MUSHROOM_BLOCK.getDefaultState();
		head = head.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.ALL_OUTSIDE);
		IBlockState floor = Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT,
				BlockHugeMushroom.EnumType.ALL_INSIDE);
		createChute(headHeight + 4, headWidth, pos.up(chuteHeight - 4), worldIn, head); // walls
		createPlatform(pos.up(chuteHeight - 1), headWidth - 2, worldIn, floor); // floor
		createPlatform(pos.up(chuteHeight + headHeight), headWidth - 2, worldIn, head); // ceiling
	}

	private void createPlatform(BlockPos center, int size, World worldIn, IBlockState state) {
		int offset = size / 2;
		BlockPos corner = center.west(offset).north(offset);
		for (int z = 0; z < size; z++)
			for (int x = 0; x < size; x++) {
				BlockPos pos = corner.east(x).south(z);
				if (worldIn.isAirBlock(pos))
					worldIn.setBlockState(pos, state);
			}
	}

	/**
	 *
	 * @param height height of the chute
	 * @param width width of the chute
	 * @param center bottom center
	 * @param worldIn world
	 * @param state block state used to create the chute
	 */
	private void createChute(int height, int width, BlockPos center, World worldIn, IBlockState state) {
		int offset = width % 2 == 0 ? width / 2 : (width - 1) / 2;
		BlockPos corner = center.west(offset).north(offset);
		for (int y = 0; y < height; y++)
			for (int z = 0; z < width; z++)
				for (int x = 0; x < width; x++) {
					BlockPos p = corner.east(x).south(z).up(y);
					boolean isEdge = z == 0 || z == width - 1 || x == 0 || x == width - 1;
					boolean isCorner = (z == 0 && x == 0) || (z == width - 1 && x == width - 1) ||
							(z == width - 1 && x == 0) || (z == 0 && x == width - 1);
					if (worldIn.isAirBlock(p) && isEdge && !isCorner)
						worldIn.setBlockState(p, state);
				}
	}
}
