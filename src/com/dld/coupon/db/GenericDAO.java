package com.dld.coupon.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.coupon.activity.ActivityManager;
import com.dld.coupon.util.StringUtils;
import com.dld.protocol.json.Detail;
import com.dld.protocol.json.DetailDelete;
import com.dld.protocol.json.DetailRef;
import com.dld.coupon.activity.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GenericDAO extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 4;
    public static final String KEY_ID = "id";
    private static SQLiteDatabase db;
    private static volatile GenericDAO instance;
    public static ArrayList<CityBean> list;
    private static Object lock = new Object();
    private Context context;

    static {
        list = new ArrayList();
    }

    private GenericDAO(Context paramContext) {
        super(paramContext, "dldclient", null, 4);
        this.context = paramContext;
    }

    public static GenericDAO getInstance() {
        return getInstance(ActivityManager.getCurrent());
    }

    public static GenericDAO getInstance(Context paramContext) {
        try {
            if (instance == null)
                ;
            synchronized (lock) {
                if (instance == null) {
                    instance = new GenericDAO(ActivityManager.getCurrent());
                    db = instance.getWritableDatabase();
                }
                return instance;
            }
        } catch (Exception localException) {
            while (true)
                LogUtils.e("test", localException);
        }
    }

    public static ArrayList<CityBean> initCityList() {
        String[] arrayOfString = new String[5];
        arrayOfString[0] = "id";
        arrayOfString[1] = "cityid";
        arrayOfString[2] = "type";
        arrayOfString[3] = "cityname";
        arrayOfString[4] = "topid";
        GenericDAO localGenericDAO = getInstance(ActivityManager.getCurrent());
        if (localGenericDAO != null) {
            Cursor localCursor1 = localGenericDAO.getProvice("citylist",
                    arrayOfString);
            int m = localCursor1.getColumnIndex("id");
            int i = localCursor1.getColumnIndex("cityid");
            int i1 = localCursor1.getColumnIndex("cityname");
            int n = localCursor1.getColumnIndex("topid");
            if ((localCursor1 != null) && (localCursor1.moveToFirst())) {
                int j = localCursor1.getCount();
                for (int k = 0; k < j; k++) {
                    int i3 = localCursor1.getInt(m);
                    String str2 = localCursor1.getString(i);
                    Object localObject = localCursor1.getString(n);
                    String str1 = localCursor1.getString(i1);
                    CityBean localCityBean1 = new CityBean();
                    localCityBean1.id = Integer.valueOf(i3);
                    localCityBean1.cityId = str2;
                    localCityBean1.cityName = str1;
                    localCityBean1.topId = Integer.valueOf(Integer
                            .parseInt((String) localObject));
                    Cursor localCursor2 = localGenericDAO.getCityByTopId(
                            "citylist", arrayOfString, str2);
                    localObject = new ArrayList();
                    for (int i2 = 0;; i2++) {
                        int i4 = localCursor2.getCount();
                        if (i2 >= i4)
                            break;
                        int i5 = localCursor1.getColumnIndex("cityid");
                        int i7 = localCursor1.getColumnIndex("cityname");
                        int i6 = localCursor1.getColumnIndex("topid");
                        i4 = localCursor2.getInt(m);
                        String str3 = localCursor2.getString(i5);
                        String str4 = localCursor2.getString(i7);
                        String str5 = localCursor2.getString(i6);
                        CityBean localCityBean2 = new CityBean();
                        localCityBean2.id = Integer.valueOf(i4);
                        localCityBean2.cityId = str3;
                        localCityBean2.cityName = str4;
                        localCityBean2.topId = Integer.valueOf(Integer
                                .parseInt(str5));
                        ((ArrayList) localObject).add(localCityBean2);
                        localCursor2.moveToNext();
                    }
                    localCursor2.close();
                    localCityBean1.chirdrenCityList = ((ArrayList) localObject);
                    list.add(localCityBean1);
                    localCursor1.moveToNext();
                }
            }
            localCursor1.close();
        }
        return (ArrayList<CityBean>) list;
    }

    private void load(SQLiteDatabase paramSQLiteDatabase, int paramInt)
            throws UnsupportedEncodingException, IOException {
        BufferedReader localBufferedReader = new BufferedReader(
                new InputStreamReader(this.context.getResources()
                        .openRawResource(paramInt), "utf-8"));
        while (true) {
            String str = localBufferedReader.readLine();
            if (str == null)
                break;
            paramSQLiteDatabase.execSQL(str);
        }
    }

    public static void removeFirstSave(String paramString) {
        getInstance(ActivityManager.getCurrent());
        db.delete("FavoriteCoupon", "bizcode=" + paramString, null);
    }

    public void SaveCustomBankInfo(CustomerBank paramCustomerBank) {
        CustomerBank.saveUserBankInfo(db, paramCustomerBank);
    }

    public boolean containsFav(int paramInt, Detail paramDetail) {
        Iterator localIterator = listFavs(paramInt).iterator();
        while (localIterator.hasNext()) {
            if (!((DetailRef) localIterator.next()).detail.equals(paramDetail))
                continue;
            boolean i = true;
            // break label46;
            return i;
        }
        boolean i = false;
        label46: return i;
    }

    public void deleteAndInsertAddress(MailingAddress paramMailingAddress) {
        MailingAddress.deleteAndInsert(db, paramMailingAddress);
    }

    public void deleteAndInsertBank(CustomerBank paramCustomerBank) {
        CustomerBank.deleteAndInsertBank(db, paramCustomerBank);
    }

    public void deleteAndUpdate(MailingAddress paramMailingAddress) {
        MailingAddress.deleteAndUpdate(db, paramMailingAddress);
    }

    public void deleteAndUpdateBank(CustomerBank paramCustomerBank) {
        CustomerBank.deleteAndUpdate(db, paramCustomerBank);
    }

    public void deleteFav(int paramInt) {
        Fav.delete(db, paramInt);
    }

    public void deleteFav(int paramInt1, int paramInt2) {
        FriendFav.delete(db, paramInt1, paramInt2);
    }

    public void deleteSyn(List<DetailDelete> paramList) {
        Fav.deleteSyn(db, paramList);
    }

    public List<String> getAddresses(String paramString1, String paramString2) {
        List localList;
        if (!StringUtils.equals(paramString2, "热门商圈"))
            localList = Address.getAddresses(db, paramString1, paramString2);
        else
            localList = Address.getHotAddresses(db, paramString1);
        return localList;
    }

    public ArrayList<CustomerBank> getAllCustomBanks() {
        return CustomerBank.getAllUserBankInfo(db);
    }

    public Map<String, DetailRef> getAllDetailMap() {
        return Fav.listAsMap(db, "");
    }

    public Map<String, DetailRef> getAllDetailMap(int paramInt) {
        return FriendFav.listAsMap(db, "where userId = " + paramInt);
    }

    public String getAllStores() {
        return Fav.getAllStores(db, "");
    }

    public String getAllStores(int paramInt) {
        return FriendFav.getAllStores(db, "where user_id = " + paramInt);
    }

    public String getCanByGroupString(int paramInt) {
        return Fav.getCanByGroupString(db, "where type = " + paramInt);
    }

    public String getCanByGroupString(int paramInt1, int paramInt2) {
        return FriendFav.getCanByGroupString(db, "where type = " + paramInt1
                + " and user_id = " + paramInt2);
    }

    public Category getCategory(int paramInt) {
        return Category.get(db, paramInt);
    }

    public Cursor getCityByTopId(String paramString1,
            String[] paramArrayOfString, String paramString2) {
        Cursor localCursor = db.query(true, paramString1, paramArrayOfString,
                "topid=" + paramString2, null, null, null, null, null);
        if (localCursor != null)
            localCursor.moveToFirst();
        return localCursor;
    }

    public String getCityIdByName(String paramString) {
        Object localObject2 = db;
        String[] arrayOfString = new String[1];
        arrayOfString[0] = "cityid";
        Object[] localObject1 = new String[1];
        localObject1[0] = paramString;
        localObject2 = ((SQLiteDatabase) localObject2).query(true, "citylist",
                arrayOfString, "cityname= ?", (String[]) localObject1, null,
                null, null, null);
        if (localObject2 != null)
            ((Cursor) localObject2).moveToFirst();
        localObject1[0] = ((Cursor) localObject2).getString(0);
        ((Cursor) localObject2).close();
        return (String) localObject1[0];
    }

    public String getCouponIdString() {
        return Fav.getStroeIdString(db, "where type = 1");
    }

    public String getCouponIdString(int paramInt) {
        return FriendFav.getStroeIdString(db, "where type = 1 and user_id = "
                + paramInt);
    }

    public int getCustomBankS() {
        return CustomerBank.getUserBankInfoCount(db);
    }

    public String getDistrict(String paramString1, String paramString2) {
        return Address.getDistrict(db, paramString1, paramString2);
    }

    public List<String> getDistricts(String paramString) {
        return Address.getDistricts(db, paramString);
    }

    public int getFavCountByType(int paramInt) {
        return Fav.getFavCountByType(db, "where type = " + paramInt);
    }

    public int getFavCountByType(int paramInt1, int paramInt2) {
        return FriendFav.getFavCountByType(db, "where type = " + paramInt1
                + " and user_id = " + paramInt2);
    }

    public long getFavsCount() {
        return Fav.getCount(db);
    }

    public String getIdStringByType(int paramInt) {
        return Fav.getIdString(db, "where type = " + paramInt);
    }

    public String getIdStringByType(int paramInt1, int paramInt2) {
        return FriendFav.getIdString(db, "where type = " + paramInt1
                + " and user_id = " + paramInt2);
    }

    public int getMailAddCount() {
        return MailingAddress.getMailAddCount(db);
    }

    public ArrayList<MailingAddress> getMailingAddressList() {
        return MailingAddress.getAllMailAddress(db);
    }

    public Cursor getProvice(String paramString, String[] paramArrayOfString) {
        Cursor localCursor = db.query(true, paramString, paramArrayOfString,
                "type=2", null, null, null, null, null);
        if (localCursor != null)
            localCursor.moveToFirst();
        return localCursor;
    }

    public int getStoreCount() {
        return Fav.getFavCountByType(db, "where type = 0 or type=1");
    }

    public int getStoreCount(int paramInt) {
        return FriendFav.getFavCountByType(db,
                "where (type = 0 or type= 1) and user_id = " + paramInt);
    }

    public String getStoreIdString() {
        return Fav.getStroeIdString(db, "where type = 0");
    }

    public String getStoreIdString(int paramInt) {
        return FriendFav.getStroeIdString(db, "where type = 0 and user_id = "
                + paramInt);
    }

    public long insert(String paramString, ContentValues paramContentValues) {
        return db.insert(paramString, null, paramContentValues);
    }

    public Map<String, DetailRef> listAsMap() {
        return Fav.listAsMap(db, "");
    }

    public Map<String, DetailRef> listAsMap(int paramInt) {
        return FriendFav.listAsMap(db, "where user_id = " + paramInt);
    }

    public List<Category> listCategories() {
        return Category.list(db);
    }

    public List<Category> listCategories(Category paramCategory) {
        return Category.list(db, paramCategory);
    }

    public List<DetailRef> listFavs() {
        return Fav.list(db, "");
    }

    public List<DetailRef> listFavs(int paramInt) {
        return Fav.list(db, "where type = " + paramInt);
    }

    public List<DetailRef> listFavs(int paramInt1, int paramInt2) {
        return FriendFav.list(db, "where type = " + paramInt1
                + " and user_id = " + paramInt2);
    }

    public List<DetailRef> listFavsOfStore() {
        return Fav.list(db, "where type = 0 or type=1");
    }

    public List<DetailRef> listFavsOfStore(int paramInt) {
        return FriendFav.list(db, "where (type = 0 or type= 1) and user_id = "
                + paramInt);
    }

    public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
        paramSQLiteDatabase.beginTransaction();
        try {
            SharePersistent.getInstance().savePerference(this.context,
                    "first_use_db", "1");
            load(paramSQLiteDatabase, R.raw.citylist);
            load(paramSQLiteDatabase, R.raw.category);
            Address.init(this.context, paramSQLiteDatabase);
            Fav.init(paramSQLiteDatabase);
            FriendFav.init(paramSQLiteDatabase);
            MailingAddress.initMailAddress(paramSQLiteDatabase);
            CustomerBank.initUserBankTable(paramSQLiteDatabase);
            MailingAddress.initMailAddress(paramSQLiteDatabase);
            CustomerBank.initUserBankTable(paramSQLiteDatabase);
            paramSQLiteDatabase.setTransactionSuccessful();
            return;
        } catch (Exception localException) {
            // while (true)
            {
                LogUtils.e("test", "", localException);
                paramSQLiteDatabase.endTransaction();
            }
        } finally {
            paramSQLiteDatabase.endTransaction();
        }
        // throw localObject;
    }

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1,
            int paramInt2) {
        onCreate(paramSQLiteDatabase);
    }

    public void saveFav(int paramInt, Object paramObject) {
        Fav.save(db, paramInt, paramObject);
    }

    public void saveFav(int paramInt1, Object paramObject, int paramInt2) {
        FriendFav.save(db, paramInt1, paramObject, paramInt2);
    }

    public void saveMailAddress(MailingAddress paramMailingAddress) {
        MailingAddress.saveMailAddress(db, paramMailingAddress);
    }
}
