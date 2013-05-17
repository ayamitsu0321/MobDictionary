package ayamitsu.dictionary.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class MobDictionaryPlugin implements IFMLLoadingPlugin
{

	@Override
	public String[] getLibraryRequestClass()
	{
		return null;
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[] {
			"ayamitsu.dictionary.asm.TransformerPlayerControllerMP",
			"ayamitsu.dictionary.asm.TransformerItemMobDictionary"
		};
	}

	@Override
	public String getModContainerClass()
	{
		return "ayamitsu.dictionary.asm.ModContainer";
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {}

}
