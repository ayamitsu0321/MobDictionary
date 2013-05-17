package ayamitsu.dictionary.asm;

import java.util.Arrays;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.ModMetadata;

public class ModContainer extends DummyModContainer
{

	public ModContainer()
	{
		super(new ModMetadata());
		ModMetadata meta = this.getMetadata();
		meta.modId       = "ayamitsu.dictionaryplugin";
		meta.name        = "DictionaryPlugin";
		meta.version     = "2.0.0";
		meta.authorList  = Arrays.asList("ayamitsu");
		meta.description = "";
		meta.url         = "";
		meta.credits     = "";
	}

}
