package com.github.hyacinth.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public final class StringTools {

    /**
     * StringTools.isBlank(null)      = true
     * StringTools.isBlank("")        = true
     * StringTools.isBlank(" ")       = true
     * StringTools.isBlank("bob")     = false
     * StringTools.isBlank("  bob  ") = false
     *
     * @param cs
     * @return
     */
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 首字母变小写
     */
    public static String firstCharToLowerCase(String str) {
        char firstChar = str.charAt(0);
        if (firstChar >= 'A' && firstChar <= 'Z') {
            char[] arr = str.toCharArray();
            arr[0] += ('a' - 'A');
            return new String(arr);
        }
        return str;
    }

    /**
     * 首字母变大写
     */
    public static String firstCharToUpperCase(String str) {
        char firstChar = str.charAt(0);
        if (firstChar >= 'a' && firstChar <= 'z') {
            char[] arr = str.toCharArray();
            arr[0] -= ('a' - 'A');
            return new String(arr);
        }
        return str;
    }

    /**
     * 字符串不为 null 而且不为  "" 时返回 true
     */
    public static boolean notBlank(String str) {
        return str != null && !"".equals(str.trim());
    }

    public static boolean notBlank(String... strings) {
        if (strings == null)
            return false;
        for (String str : strings)
            if (str == null || "".equals(str.trim()))
                return false;
        return true;
    }

    public static String toCamelCase(String stringWithUnderline) {
        if (stringWithUnderline.indexOf('_') == -1)
            return stringWithUnderline;

        stringWithUnderline = stringWithUnderline.toLowerCase();
        char[] fromArray = stringWithUnderline.toCharArray();
        char[] toArray = new char[fromArray.length];
        int j = 0;
        for (int i=0; i<fromArray.length; i++) {
            if (fromArray[i] == '_') {
                // 当前字符为下划线时，将指针后移一位，将紧随下划线后面一个字符转成大写并存放
                i++;
                if (i < fromArray.length)
                    toArray[j++] = Character.toUpperCase(fromArray[i]);
            }
            else {
                toArray[j++] = fromArray[i];
            }
        }
        return new String(toArray, 0, j);
    }

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * 通配符匹配
     *
     * @param pattern 通配符表达式
     * @param str
     */
    public static boolean wildcardMatch(String pattern, String str) {
        int patternLength = pattern.length();
        int strLength = str.length();
        int strIndex = 0;
        char ch;
        for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
            ch = pattern.charAt(patternIndex);
            if (ch == '*') {
                // 通配符星号*表示可以匹配任意多个字符
                while (strIndex < strLength) {
                    if (wildcardMatch(pattern.substring(patternIndex + 1), str.substring(strIndex))) {
                        return true;
                    }
                    strIndex++;
                }
            } else if (ch == '?') {
                // 通配符问号?表示匹配任意一个字符
                strIndex++;
                if (strIndex > strLength) {
                    // 表示str中已经没有字符匹配?了。
                    return false;
                }
            } else {
                if ((strIndex >= strLength) || (ch != str.charAt(strIndex))) {
                    return false;
                }
                strIndex++;
            }
        }
        return strIndex == strLength;
    }


    /**
     * 将多个字符串拼接起来
     *
     * @param parts
     * @return
     */
    public static String join(String... parts) {
        StringBuilder sb = new StringBuilder(parts.length);
        for (String part : parts) {
            sb.append(part);
        }
        return sb.toString();
    }

    /**
     * 将集合里面多个元素按照规定的分隔符拼接起来
     *
     * @param elements
     * @param separator 分隔符
     * @return
     */
    public static String join(Iterator<?> elements, String separator) {
        if (elements == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        while (elements.hasNext()) {
            Object o = elements.next();
            if (sb.length() > 0 && separator != null) {
                sb.append(separator);
            }
            sb.append(o);
        }
        return sb.toString();
    }

    /**
     * 将数组元素按照规定的分隔符拼接起来
     *
     * @param elements
     * @param separator 分隔符
     * @return
     */
    public static String join(Object[] elements, String separator) {
        if (elements == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object o : elements) {
            if (sb.length() > 0 && separator != null) {
                sb.append(separator);
            }
            sb.append(o);
        }
        return sb.toString();
    }

    /**
     * 字符串分割
     *
     * @param str           str
     * @param separatorChar separator
     * @return String[]
     */
    public static String[] split(final String str, final char separatorChar) {
        return split(str, separatorChar, true);
    }

    /**
     * 字符串分割
     *
     * @param str               str
     * @param separatorChar     separator
     * @param preserveAllTokens preserveAllTokens
     * @return String[]
     */
    public static String[] split(final String str, final char separatorChar, final boolean preserveAllTokens) {
        if (str == null) {
            return null;
        }
        final int len = str.length();
        if (len == 0) {
            return new String[0];
        }
        final List<String> list = new ArrayList<String>();
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match || preserveAllTokens) {
                    list.add(str.substring(start, i));
                    match = false;
                    lastMatch = true;
                }
                start = ++i;
                continue;
            }
            lastMatch = false;
            match = true;
            i++;
        }
        if (match || preserveAllTokens && lastMatch) {
            list.add(str.substring(start, i));
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * 首字母大写
     *
     * @param str
     * @return
     */
    public static String firstCharUpperCase(String str) {
        if (StringTools.isBlank(str)) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder(str);
        stringBuilder.setCharAt(0, Character.toUpperCase(stringBuilder.charAt(0)));
        return stringBuilder.toString();
    }

    /**
     * 首字母小写
     *
     * @param str
     * @return
     */
    public static String firstCharLowerCase(String str) {
        if (StringTools.isBlank(str)) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder(str);
        stringBuilder.setCharAt(0, Character.toLowerCase(stringBuilder.charAt(0)));
        return stringBuilder.toString();
    }

}
