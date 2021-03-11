package network;

public class Tools {
    public static String getMessagePropertyValue(String fullStr, String itemName)
    {
        return getMidString(fullStr, "<" + itemName + ">", "</>", 0);
    }

    public static String getMidString(String fullStr, String former, String behind, int beginFrom)
    {
        int positionA = fullStr.indexOf(former, beginFrom);
        int positionB = fullStr.indexOf(behind, positionA + 1);
        if (positionA < 0 || positionB < 0) return "";
        int len = positionB - positionA - former.length();
        int s = positionA + former.length();
        return fullStr.substring(s, s + len);
    }
}
