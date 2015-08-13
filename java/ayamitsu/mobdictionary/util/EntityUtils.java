package ayamitsu.mobdictionary.util;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by ayamitsu0321 on 2015/04/12.
 */
public class EntityUtils {

    private static int allEntityValue;
    private static int allMobValue;

    public static boolean isLivingClass(Class clazz) {
        return clazz != null && (EntityLiving.class).isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers());
    }

    public static boolean isLivingName(String name) {
        Class clazz = getClassFromName(name);
        return isLivingClass(clazz);
    }

    public static boolean containsClass(Class clazz) {
        return getClassToStringMapping().containsKey(clazz);
    }

    public static boolean containsName(String name) {
        return getStringToClassMapping().containsKey(name);
    }

    public static Class getClassFromName(String name) {
        return (Class)getStringToClassMapping().get(name);
    }

    public static String getNameFromClass(Class clazz) {
        return (String)getClassToStringMapping().get(clazz);
    }

    public static Map getStringToClassMapping() {
        try {
            Field field = EntityList.class.getDeclaredFields()[1];
            field.setAccessible(true);
            return (Map)field.get(null);
        } catch (Exception e) {
            return null;
        }
    }

    public static Map getClassToStringMapping() {
        try {
            Field field = EntityList.class.getDeclaredFields()[2];
            field.setAccessible(true);
            return (Map)field.get(null);
        } catch (Exception e) {
            return null;
        }
    }

    public static int getAllEntityValue() {
        return allEntityValue;
    }

    public static int getAllMobValue() {
        return allMobValue;
    }

    public static void resetAllEntityValue() {
        allEntityValue = getStringToClassMapping().size();
    }

    @SuppressWarnings("unchecked")
    public static void resetAllMobValue() {
        Set set = new HashSet();
        Map classToStringMapping = EntityUtils.getClassToStringMapping();

        for (Object obj : classToStringMapping.keySet()) {
            if (obj instanceof Class && isLivingClass((Class)obj)) {
                set.add(obj);
            }
        }

        allMobValue = set.size();
    }

    static {
        resetAllEntityValue();
        resetAllMobValue();
    }

}
