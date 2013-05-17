package ayamitsu.dictionary.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import cpw.mods.fml.relauncher.FMLRelauncher;
import cpw.mods.fml.relauncher.IClassTransformer;

public class TransformerPlayerControllerMP implements IClassTransformer, Opcodes
{
	private static String PLAYERCONTROLLERMP_CLASS_NAME = "net.minecraft.client.multiplayer.PlayerControllerMP";

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes)
	{
		if (!FMLRelauncher.side().equals("CLIENT") || !transformedName.equals(PLAYERCONTROLLERMP_CLASS_NAME))
		{
			return bytes;
		}

		System.out.println("Found PlayerControllerMP class.");

		try
		{
			return this.transformPlayerControllerMP(bytes);
		}
		catch (Exception e)
		{
			throw new RuntimeException("Failed to transform PlayerControllerMP.");
		}
	}

	private byte[] transformPlayerControllerMP(byte[] arrayOfByte)
	{
		ClassNode cNode = new ClassNode();
		ClassReader cReader = new ClassReader(arrayOfByte);
		cReader.accept(cNode, 0);

		for (MethodNode mNode : cNode.methods)
		{
			if (("func_78768_b").equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(cNode.name, mNode.name, mNode.desc)) && ("(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/entity/Entity;)Z").equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(mNode.desc)))
			{
				System.out.println("Override PlayerControllerMP func_78768_b");
				InsnList insnList = new InsnList();
				insnList.add(new VarInsnNode(ALOAD, 1));// player
				insnList.add(new VarInsnNode(ALOAD, 2));// target entity
				insnList.add(new MethodInsnNode(INVOKESTATIC, "ayamitsu/dictionary/MobDictionary", "interactWithEntity", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/entity/Entity;)V"));
				mNode.instructions.insert(mNode.instructions.get(0), insnList);
			}
		}

		ClassWriter cWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS)
		{
			@Override
			public String getCommonSuperClass(String type1, String type2)
			{
				return FMLDeobfuscatingRemapper.INSTANCE.map(type1);// important ... ?
			}
		};

		cNode.accept(cWriter);
		arrayOfByte = cWriter.toByteArray();
		return arrayOfByte;
	}
}
