package com.omi.database;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.litesuits.orm.db.model.ConflictAlgorithm;
import com.omi.OmiApplication;
import com.omi.bean.base.BaseBean;
import com.omi.config.Config;
import com.omi.net.ApiByHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SensYang on 2016/4/8 0008.
 */
public class LiteOrmDBUtil {
    private static LiteOrmDBUtil globalDBUtil;
    private static LiteOrmDBUtil userDBUtil;
    private static LiteOrm liteOrm;
    private String userName;

    /**
     * 获取全局数据库
     */
    public static LiteOrmDBUtil getGlobalDBUtil() {
        if (globalDBUtil == null) {
            globalDBUtil = new LiteOrmDBUtil("global");
        }
        return globalDBUtil;
    }

    /**
     * 获取用户数据库
     */
    public static LiteOrmDBUtil getUserDBUtil() {
        if (userDBUtil == null) {
            userDBUtil = new LiteOrmDBUtil(ApiByHttp.getInstance().getPhone());
        }
        if (userDBUtil.userName != null && !userDBUtil.userName.equalsIgnoreCase(ApiByHttp.getInstance().getPhone())) {
            userDBUtil.userName = ApiByHttp.getInstance().getPhone();
            userDBUtil.createDb(userDBUtil.userName);
        }
        if (userDBUtil.liteOrm == null) {
            userDBUtil = null;
            return getGlobalDBUtil();
        }
        return userDBUtil;
    }

    private LiteOrmDBUtil(String userName) {
        this.userName = userName;
        createDb(userName);
    }

    /**
     * 创建数据库
     */
    private void createDb(String userName) {
        if (liteOrm != null) {
            liteOrm.close();
        }
        liteOrm = LiteOrm.newCascadeInstance(OmiApplication.getInstance(), "omi_" + userName);
        liteOrm.setDebugged(Config.ISDEBUG);
    }

    /**
     * 获取数据库管理对象
     */
    public LiteOrm getLiteOrm() {
        return liteOrm;
    }

    /**
     * 插入一条记录
     */
    public <T extends BaseBean> void save(T t) {
        if (liteOrm == null || t == null) return;
        liteOrm.save(t);
    }

    /**
     * 插入所有记录
     */
    public <T extends BaseBean> void save(List<T> list) {
        if (liteOrm == null || list == null) return;
        liteOrm.save(list);
    }


    /**
     * 删除一个
     */
    public <T extends BaseBean> void delete(T t) {
        if (liteOrm == null || t == null) return;
        liteOrm.delete(t);
    }

    /**
     * 删除多个
     */
    public <T extends BaseBean> void delete(List<T> list) {
        if (liteOrm == null || list == null) return;
        liteOrm.delete(list);
    }

    /**
     * 删除所有 某字段等于 Vlaue的值
     */
    public <T extends BaseBean> void deleteByWhere(Class<T> cla, String field, String... value) {
        if (liteOrm == null || cla == null || value == null) return;
        liteOrm.delete(WhereBuilder.create(cla).where(field + "=?", value));
    }

    /**
     * 删除所有
     */
    public <T extends BaseBean> void deleteAll(Class<T> cla) {
        if (liteOrm == null || cla == null) return;
        liteOrm.deleteAll(cla);
    }


    /**
     * 更新数据
     * 仅在存在时更新
     */
    public <T extends BaseBean> void update(T t) {
        if (liteOrm == null || t == null) return;
        liteOrm.update(t, ConflictAlgorithm.Replace);
    }

    /**
     * 更新数据
     * 仅在存在时更新
     */
    public <T extends BaseBean> void update(List<T> list) {
        if (liteOrm == null || list == null) return;
        liteOrm.update(list, ConflictAlgorithm.Replace);
    }

    /**
     * 查询数量
     */
    public <T extends BaseBean> long queryCount(Class<T> cla) {
        if (liteOrm == null || cla == null) return 0;
        return liteOrm.queryCount(cla);
    }

    /**
     * 查询数量
     */
    public <T extends BaseBean> long queryCount(Class<T> cla, String field, String... value) {
        if (liteOrm == null || cla == null || value == null) return 0;
        return liteOrm.queryCount(new QueryBuilder(cla).where(field + "=?", value));
    }

    /**
     * 查询一个
     */
    public <T extends BaseBean> T queryOne(Class<T> cla) {
        if (liteOrm == null || cla == null) return null;
        List<T> list = liteOrm.query(new QueryBuilder(cla).limit(String.valueOf(1)));
        if (list.size() > 0) return list.get(0);
        else return null;
    }

    /**
     * 查询一个
     */
    public <T extends BaseBean> T queryOneByWhere(Class<T> cla, String field, String... value) {
        if (liteOrm == null || cla == null || value == null) return null;
        List<T> list = liteOrm.query(new QueryBuilder(cla).where(field + "=?", value).limit(String.valueOf(1)));
        if (list.size() > 0) return list.get(0);
        else return null;
    }

    /**
     * 查询所有
     */
    public <T extends BaseBean> List<T> query(Class<T> cla) {
        if (liteOrm == null || cla == null) return new ArrayList(0);
        return liteOrm.query(cla);
    }

    /**
     * 查询所有  某字段 等于 Value的值  可以指定从1-20，分页
     */
    public <T extends BaseBean> List<T> query(Class<T> cla, int start, int length) {
        if (liteOrm == null || cla == null) return new ArrayList(0);
        return liteOrm.query(new QueryBuilder(cla).limit(start, length));
    }

    /**
     * 查询  某字段 等于 Value的值
     */
    public <T extends BaseBean> List<T> queryByWhere(Class<T> cla, String field, String... value) {
        if (liteOrm == null || cla == null || value == null) return new ArrayList(0);
        return liteOrm.query(new QueryBuilder(cla).where(field + "=?", value));
    }

    /**
     * 查询  某字段 等于 Value的值  可以指定从1-20，分页
     */
    public <T extends BaseBean> List<T> queryByWhere(Class<T> cla, int start, int length, String field, String... value) {
        if (liteOrm == null || cla == null || value == null) return new ArrayList(0);
        return liteOrm.query(new QueryBuilder(cla).where(field + "=?", value).limit(start, length));
    }
}
