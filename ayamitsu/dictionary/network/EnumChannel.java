package ayamitsu.dictionary.network;

public enum EnumChannel
{
	ADDINFO_TO_SERVER("mobdic.addinfo.0"),
	ADDINFO_TO_CLIENT("mobdic.addinfo.1");

	public final String channel;

	private EnumChannel(String channel)
	{
		this.channel = channel;
	}

	public String getChannel()
	{
		return this.channel;
	}
}
