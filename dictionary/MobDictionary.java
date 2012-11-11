package net.minecraft.src.dictionary;

import net.minecraft.src.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collections;
import java.lang.ClassCastException;
import java.lang.reflect.Modifier;

public class MobDictionary
{
	private static MobDictionary instance = new MobDictionary();
	private static List nameList = new ArrayList<String>();
	public static int mobValue;
	
	private MobDictionary()
	{
	}
	
	//get nameList
	public List getList()
	{
		return nameList;
	}
	
	//for file
	public static String[] getNames_file()
	{
		String[] names = (String[])nameList.toArray(new String[0]);
		Arrays.sort(names);
		return names;
	}
	
	//for dictionary
	public static String[] getNames_dic()
	{
		String[] names = getNames_file();
		
		if (names.length > 0)
		{
			List list = new ArrayList();
			
			for (int i = 0; i < names.length; i++)
			{
				if (MobDictionary.isContains(names[i]))
				{
					list.add(names[i]);
				}
			}
			
			names = (String[])list.toArray(new String[0]);
		}
		
		return names;
	}
	
	//register by class
	public static boolean addInfo(Class clazz)
	{
		if (clazz != null)
		{
			String name = getNameFromClass(clazz);
			
			if (addInfo(name))
			{
				return true;
			}
		}
		
		return false;
	}
	
	//register by name
	public static boolean addInfo(String name)
	{
		if (name != null && !name.equals("") && !nameList.contains(name))
		{
			nameList.add(name);
			return true;
		}
		
		return false;
	}
	
	//register by id
	public static boolean addInfo(int id)
	{
		if (id > 0)
		{
			String name = getNameFromID(id);
			
			if (addInfo(name))
			{
				return true;
			}
		}
		
		return false;
	}

//entity
	//get class from id
	public static Class getClassFromID(int id)
	{
		return isContains(id) ? (Class)getEntityMap(2).get(id) : null;
	}
	
	//get class from name
	public static Class getClassFromName(String name)
	{
		return isContains(name) ? (Class)getEntityMap(0).get(name) : null;
	}
	
	//get name from class
	public static String getNameFromClass(Class clazz)
	{
		return isContains(clazz) ? (String)getEntityMap(1).get(clazz) : null;
	}
	
	//get name from id
	public static String getNameFromID(int id)
	{
		return isContains(id) ? (String)getNameFromClass((Class)getEntityMap(2).get(id)) : null;
	}
	
	//get id from class
	public static int getIDFromClass(Class clazz)
	{
		return isContains(clazz) ? ((Integer)getEntityMap(3).get(clazz)).intValue() : -1;
	}
	
	//get id from name
	public static int getIDFromName(String name)
	{
		return isContains(name) ? ((Integer)getEntityMap(4).get(name)).intValue() : -1;
	}
	
	//get EntityList.class' field<Map>
	protected static Map getEntityMap(int i)
	{
		try
		{
			return (Map)ModLoader.getPrivateValue(EntityList.class, null, i);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	//use on modsLoaded method
	public static void setEntityValueOfTypes()
	{
		List list = new ArrayList();
		Map map = getEntityMap(2);
		Iterator iterator = map.values().iterator();
		
		while (iterator.hasNext())
		{
			Class clazz = (Class)iterator.next();
			
			if (clazz != null && clazz != EntityLiving.class && (EntityLiving.class).isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers()))
			{
				list.add(clazz);
			}
		}
		
		mobValue = list.size();
	}
	
	/**
	 * getDicRegisteredValue() / getEntityValueOfTypes()
	 */
	
	//kind of entity value
	public static int getEntityValueOfTypes()
	{
		return mobValue;
	}
	
	//registered entity value
	public static int getDicRegisteredValue()
	{
		return nameList.size();
	}
	
	//EntityList's map jadge contains
	protected static boolean isContains(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		
		if (obj instanceof Class)
		{
			Map classToStringMapping = getEntityMap(1);
			return classToStringMapping != null && classToStringMapping.containsKey((Class)obj);
		}
		
		if (obj instanceof String)
		{
			Map stringToClassMapping = getEntityMap(0);
			return stringToClassMapping != null && stringToClassMapping.containsKey((String)obj);
		}
		
		String str = obj.toString();
		int id = new Integer(str).intValue();
		Map IDtoClassMapping = getEntityMap(2); 
		return IDtoClassMapping != null && IDtoClassMapping.containsKey(id);
	}
}