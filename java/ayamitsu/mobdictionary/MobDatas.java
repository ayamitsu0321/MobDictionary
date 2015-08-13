package ayamitsu.mobdictionary;

import ayamitsu.mobdictionary.util.EntityUtils;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.*;
import java.util.*;

/**
 * Created by ayamitsu0321 on 2015/07/28.
 * <p/>
 * integrated server
 * loadはプレイヤーがログイン時にserver側で。その後serverからclientnに同期
 * saveは登録時
 *
 * dedicated server
 * loadはプレイヤーがログイン時にserver側で。その後serverからclientに同期
 * saveは登録時、プレイヤーのUUIDに関連付けて
 */
public final class MobDatas {

    // client and integrated server
    private static Set<String> nameList = new HashSet<String>();

    // dedicated server
    private static Map<UUID, Set<String>> nameListMap = new HashMap<UUID, Set<String>>();

    private static int allMobValue;

    /*
        client and integrated server
    */
    public static void addInfo(Object obj) {
        String name = null;

        // addinfo
        if (obj instanceof Class) {
            name = EntityUtils.getNameFromClass((Class)obj);
        } else if (obj instanceof String) {
            name = (String)obj;
        }

        if (name == null) {
            return;
        }

        addInfo(name);
    }

    /*
        dedicated server
     */
    public static void addInfoOnDedicatedServer(Object obj, EntityPlayerMP player) {
        String name = null;

        // addinfo
        if (obj instanceof Class) {
            name = EntityUtils.getNameFromClass((Class)obj);
        } else if (obj instanceof String) {
            name = (String)obj;
        }

        if (name == null) {
            return;
        }

        if (MobDictionary.proxy.isDedicatedServer()) {
            addInfoOnDedicatedServer(name, player);
        }
    }

/*
    public static void addInfoClient(String name) {
        addInfo(name);
    }
*/

    /**
     * if return true, already contains
     */
    private static Boolean addInfo(Class clazz) {
        /*if (EntityUtils.isLivingClass(clazz)) {
            return !nameList.add(EntityUtils.getNameFromClass(clazz));
        }*/

        return !nameList.add(EntityUtils.getNameFromClass(clazz));
    }

    /**
     * if return true, already contains
     */
    private static Boolean addInfo(String name) {
        /*if (EntityUtils.isLivingName(name)) {
            return !nameList.add(name);
        }*/

        return !nameList.add(name);
    }

    private static Boolean addInfoOnDedicatedServer(String name, EntityPlayerMP player) {
        UUID uuid = player.getUniqueID();

        if (!nameListMap.containsKey(uuid)) {
            nameListMap.put(uuid, new HashSet<String>());
        }

        return !nameListMap.get(uuid).add(name);
    }

    public static boolean contains(Class clazz) {
        return contains(EntityUtils.getNameFromClass(clazz));
    }

    public static boolean contains(String name) {
        return nameList.contains(name);
    }

    public static boolean containsOnDedicatedServer(String name, EntityPlayerMP player) {
        UUID uuid = player.getUniqueID();

        if (!nameListMap.containsKey(uuid)) {
            nameListMap.put(uuid, new HashSet<String>());
            return false;
        }

        return nameListMap.get(uuid).contains(name);
    }

    public static void clearNameList() {
        nameList.clear();
    }

    public static void initAllMobValue() {
        allMobValue = EntityUtils.getAllMobValue();
        /*Set set = new HashSet();
        Map classToStringMapping = EntityUtils.getClassToStringMapping();

        for (Object obj : classToStringMapping.keySet()) {
            if (obj instanceof Class && EntityUtils.isLivingClass((Class)obj)) {
                set.add(obj);
            }
        }

        allMobValue = set.size();*/
    }

    public static int getAllMobValue() {
        return allMobValue;
    }

    public static int getRegisteredValue() {
        return nameList.size();
    }

    public static int getRegisteredValueOnDedicatedServer(EntityPlayerMP player) {
        UUID uuid = player.getUniqueID();

        if (!nameListMap.containsKey(uuid)) {
            return 0;
        }

        return nameListMap.get(uuid).size();
    }

    public static String[] toArray() {
        return nameList.toArray(new String[0]);
    }

    public static String[] toArrayOnDedicatedServer(EntityPlayerMP player) {
        UUID uuid = player.getUniqueID();

        if (!nameListMap.containsKey(uuid)) {
            return new String[0];
        }

        Set<String> set = nameListMap.get(uuid);

        return set.toArray(new String[0]);
    }

    public static void load() throws IOException {
        nameList.clear();

        File dir = MobDictionary.proxy.getSaveDirectory();
        File file = (new File(dir, "/mobdic.md")).getAbsoluteFile();

        if (!file.exists()) {
            return;
        }

        if (!file.canRead()) {
            throw new IOException("Can not read dictionary data:" + file.getPath());
        }

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        while ((line = br.readLine()) != null) {
            addInfo(line.trim());
        }

        br.close();
    }

    public static void loadOnDedicatedServer(EntityPlayerMP player) throws IOException {
        nameList.clear();

        File dir = MobDictionary.proxy.getSaveDirectory();
        File file = (new File(dir, "/" + player.getUniqueID().toString())).getAbsoluteFile();

        if (!file.exists()) {
            return;
        }

        if (!file.canRead()) {
            throw new IOException("Can not read dictionary data:" + file.getPath());
        }

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        while ((line = br.readLine()) != null) {
            addInfoOnDedicatedServer(line.trim(), player);
        }

        br.close();
    }

    /*
     * integrated server and dedicated server
     */
    public static void save() throws IOException {
        File dir = MobDictionary.proxy.getSaveDirectory();
        File file = (new File(dir, "/mobdic.md")).getAbsoluteFile();

        //File file = getSaveFile();
        //File dir = file.getParentFile();

        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Can not write dictionary data:" + file.getPath());
        }

        if (!file.exists() && !file.createNewFile()) {
            throw new IOException("Can not write dictionary data:" + file.getPath());
        }

        if (!file.canWrite()) {
            throw new IOException("Can not write dictionary data:" + file.getPath());
        }

        PrintWriter pw = new PrintWriter(new FileOutputStream(file));

        for (String name : nameList) {
            pw.println(name);
        }

        pw.close();
    }

    public static void saveOnDedicatedServer(EntityPlayerMP player) throws IOException {
        File dir = MobDictionary.proxy.getSaveDirectory();
        File file = (new File(dir, "/" + player.getUniqueID().toString())).getAbsoluteFile();

        //File file = getSaveFile();
        //File dir = file.getParentFile();

        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Can not write dictionary data:" + file.getPath());
        }

        if (!file.exists() && !file.createNewFile()) {
            throw new IOException("Can not write dictionary data:" + file.getPath());
        }

        if (!file.canWrite()) {
            throw new IOException("Can not write dictionary data:" + file.getPath());
        }

        PrintWriter pw = new PrintWriter(new FileOutputStream(file));
        UUID uuid = player.getUniqueID();

        if (nameListMap.containsKey(uuid)) {
            for (String name : nameListMap.get(uuid)) {
                pw.println(name);
            }
        }

        pw.close();
    }

    /*public static File getSaveFile() {
        return (new File(Loader.instance().getConfigDir(), "dictionary/mobdic.md")).getAbsoluteFile();
    }*/
}
