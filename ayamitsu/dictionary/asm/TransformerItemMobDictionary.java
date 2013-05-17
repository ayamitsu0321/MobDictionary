package ayamitsu.dictionary.asm;

import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import ayamitsu.util.asm.ASMDebugUtils;
import ayamitsu.util.reflect.Reflector;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import cpw.mods.fml.relauncher.FMLRelauncher;
import cpw.mods.fml.relauncher.IClassTransformer;

public class TransformerItemMobDictionary implements IClassTransformer, Opcodes
{
	private static final String ITEMMOBDICTIONARY_CLASS_NAME = "ayamitsu.dictionary.item.ItemMobDictionary";

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes)
	{
		if (FMLRelauncher.side().equals("CLIENT") || !transformedName.equals(ITEMMOBDICTIONARY_CLASS_NAME))
		{
			return bytes;
		}

		try
		{
			return this.transformItemMobDictionary(bytes);
		}
		catch (Exception e)
		{
			throw new RuntimeException("Failed to transform ItemMobDictionary class.");
		}
	}

	private byte[] transformItemMobDictionary(byte[] arrayOfByte)
	{
		ClassNode cNode = new ClassNode();
		ClassReader cReader = new ClassReader(arrayOfByte);
		cReader.accept(cNode, 0);

		for (MethodNode mNode : cNode.methods)
		{
			if (("displayDictionary").equals(mNode.name) && ("(Lnet/minecraft/entity/player/EntityPlayer;)V").equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(mNode.desc)))
			{
				ASMDebugUtils.logAll(mNode);

				for (AbstractInsnNode aiNode : mNode.instructions.toArray())
				{
					if (aiNode instanceof MethodInsnNode)
					{
						mNode.instructions.remove(aiNode.getNext().getNext().getNext().getNext().getNext());
						mNode.instructions.remove(aiNode.getNext().getNext().getNext().getNext());
						mNode.instructions.remove(aiNode.getNext().getNext().getNext());
						mNode.instructions.remove(aiNode.getNext().getNext());
						mNode.instructions.remove(aiNode.getNext());
						mNode.instructions.remove(aiNode);
						break;
					}
				}

				break;
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
