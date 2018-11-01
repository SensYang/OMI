package com.omi.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.omi.OmiApplication;
import com.omi.bean.account.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cxd on 2015/1/22.
 */


public class ContactsUtil {
    /**
     * 判断输入字符串有没有可能是手机号
     */
    public static boolean mabyMobileNO(String mobiles) {
        if (mobiles == null || mobiles.length() == 0) return false;
        Pattern p = Pattern.compile("^[1][34578][0-9]{0,9}$"); // 验证手机号
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断输入字符串是否为密码
     * 密码规则 6到16之间 必须包含字母数字
     */
    public static boolean isPassword(String password) {
        if (password == null || password.length() == 0) return false;
        Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$"); // 验证密码
        Matcher m = p.matcher(password);
        return m.matches();
    }

    /**
     * 判断输入字符串的是否为手机号
     */
    public static boolean isMobileNO(String mobiles) {
        if (mobiles == null || mobiles.length() == 0) return false;
        Pattern p = Pattern.compile("^[1][34578][0-9]{9}$"); // 验证手机号
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断输入的字符串是否为邮箱地址
     */
    public static boolean isEmail(String email) {
        if (email == null || email.length() == 0) return false;
        Pattern p = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 判断输入的字符串是否为邮政编码
     */
    public static boolean isPost(String post) {
        if (post == null || post.length() == 0) return false;
        Pattern p = Pattern.compile("[0-9]\\d{5}(?!\\d)");
        Matcher m = p.matcher(post);
        return m.matches();
    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId 银行卡卡号
     */
    public static boolean isBankCard(String cardId) {
        if (cardId == null || cardId.length() == 0) return false;
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId 不含校验位的银行卡卡号
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0 || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if ((j & 1) == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 是否是身份证号
     */
    public static boolean isIdCard(String idCard) {
        return IDCard.getInstance().verify(idCard);
    }

    private static List<Contact> listMembers;

    public static List<Contact> getContact() {
        if (listMembers != null) return listMembers;
        listMembers = new ArrayList<>();
        Cursor cursor = null;
        try {
            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            // 这里是获取联系人表的电话里的信息  包括：名字，名字拼音，联系人id,电话号码；
            // 然后在根据"sort-key"排序
            cursor = OmiApplication.getInstance().getContentResolver().query(uri, new String[]{//
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY, //
                            ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY, //
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID, //
                            ContactsContract.CommonDataKinds.Phone.NUMBER},//
                    null, null, ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY);
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();

                    String contact_phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String name = cursor.getString(0);
                    String sortKey = cursor.getString(1);
                    if (sortKey.length() > 0) {
                        char c = sortKey.charAt(0);
                        if ((c >= '0' && c <= '9') || c == '+') {
                            sortKey = "#";
                        }
                    } else {
                        sortKey = "#";
                    }
                    int contact_id = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));

                    contact.setContact_id(contact_id);
                    contact.setContact_name(name);
                    contact.setPhone(contact_phone);
                    contact.setCompareString(sortKey.toUpperCase());

                    if (name != null) listMembers.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return listMembers;
    }

    public static String getContactName(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
    }

    //获取联系人电话
    public static String getContactPhone(Context context, Cursor cursor) {
        int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = cursor.getInt(phoneColumn);
        String phoneResult = "";
        if (phoneNum > 0) {
            // 获得联系人的ID号
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            // 获得联系人的电话号码的cursor;
            Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
            if (phones != null ? phones.moveToFirst() : false) {
                // 遍历所有的电话号码
                for (; !phones.isAfterLast(); phones.moveToNext()) {
                    int index = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    phoneResult = phones.getString(index);
                }
                if (!phones.isClosed()) {
                    phones.close();
                }
            }
        }
        return phoneResult;
    }
}