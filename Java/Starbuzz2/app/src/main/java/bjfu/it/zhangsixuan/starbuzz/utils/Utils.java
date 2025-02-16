package bjfu.it.zhangsixuan.starbuzz.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import bjfu.it.zhangsixuan.starbuzz.R;
import bjfu.it.zhangsixuan.starbuzz.db.StarbuzzDatabaseHelper;
import bjfu.it.zhangsixuan.starbuzz.ui.home.StuffCategoryFragment;
import bjfu.it.zhangsixuan.starbuzz.ui.home.StuffFragment;

import static bjfu.it.zhangsixuan.starbuzz.MainActivity.CART_TABLE;

public class Utils {
    /**
     * 跳转到详情页面的方法
     *
     * @param stuffId
     * @param transaction
     */
    public static void toDetailFragment(int stuffId, FragmentTransaction transaction) {
        //开启事务跳转
//        assert getFragmentManager() != null;
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        StuffFragment stuffFragment = new StuffFragment();
        // 传递stuffId参数
        Bundle bundle = new Bundle();
        bundle.putInt(StuffFragment.EXTRA_STUFFID, stuffId);
        stuffFragment.setArguments(bundle);

        transaction
                .addToBackStack(null)  //将当前fragment加入到返回栈中
                .replace(R.id.nav_host_fragment, stuffFragment)
                .show(stuffFragment)
                .commit();   //提交
    }

    /**
     * 跳转到单个类别下的商品列表下
     *
     * @param categoryId
     * @param transaction
     */
    public static void toStuffCategoryFragment(int categoryId, FragmentTransaction transaction) {
        //开启事务跳转
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        StuffCategoryFragment stuffCategoryFragment = new StuffCategoryFragment();
        // 传递categoryId参数
        Bundle bundle = new Bundle();
        bundle.putInt(StuffCategoryFragment.EXTRA_CATEGORY_ID, categoryId);
        stuffCategoryFragment.setArguments(bundle);

        transaction
                .addToBackStack(null)  //将当前fragment加入到返回栈中
                .replace(R.id.nav_host_fragment, stuffCategoryFragment)
                .show(stuffCategoryFragment)
                .commit();   // 提交
    }

    // 计算mData的总价
    private static double getTotalPrice(List<Map<String, Object>> mData) {
        double tol = 0;
        for (Map<String, Object> mDatum : mData) {
            double perPrice = (double) mDatum.get("stuffPrice");
            int number = (int) mDatum.get("stuffNumber");
            tol += perPrice * number;
        }
        return tol;
    }

    // 刷新购物车的总价
    public static void refreshTotalPrice(TextView tv_total_price, List<Map<String, Object>> mData) {
        double tol = getTotalPrice(mData);
        tv_total_price.setText(String.valueOf(tol));
    }


    // 购物车右上角数字的刷新显示
    public static void refreshCartNum(TextView tv_num, Context context) {
//        TextView tv_num = Objects.requireNonNull(getActivity().findViewById(R.id.tv_num);
        // 获取购物车中的商品数量
        int num = getCartItemNum(context);
        // 设置商品数量
        tv_num.setText(String.valueOf(num));
        // 设置右上角数字是否可见，若购物车没有商品则不可见
        tv_num.setVisibility(num == 0 ? View.INVISIBLE : View.VISIBLE);
    }


    // 购物车中的商品信息，包含cartId、stuffId、stuffNumber
    private static List<Map<String, Object>> getCartNData(Context context) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Cursor cursor1;
        // map数据准备
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(context);
        try (SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase()) {
            // 全部取出来
            cursor1 = db.query(CART_TABLE, new String[]{"ID", "STUFF_ID", "NUMBER"}, null,
                    null, null, null, null, null);
            while (cursor1.moveToNext()) {
                // 添加
                // 需要的是：cartId, stuffId, stuffNumber
                // 移动光标到下一行
                Map<String, Object> map = new HashMap<String, Object>();
                int cartId = cursor1.getInt(cursor1.getColumnIndex("ID"));
                int stuffId = cursor1.getInt(cursor1.getColumnIndex("STUFF_ID"));
                int stuffNumber = cursor1.getInt(cursor1.getColumnIndex("NUMBER"));
                map.put("cartId", cartId);
                map.put("stuffId", stuffId);
                map.put("stuffNumber", stuffNumber);
                list.add(map);
            }
        } catch (
                SQLiteException e) {
            Log.e("debug", Objects.requireNonNull(e.getMessage()));
        }
        return list;
    }

    // 购物车的商品总数
    private static int getCartItemNum(Context context) {
        int numTol = 0;
        // 购物车中的商品信息，包含cartId、stuffId、stuffNumber
        List<Map<String, Object>> list = getCartNData(context);
        // 取购物车中每一种商品的数量并进行累加
        for (Map<String, Object> map : list) {
            numTol += (int) map.get("stuffNumber");
        }
        return numTol;
    }


    // 添加一件商品到购物车，不含refresh RecycleView，但包括刷新购物车内的商品总数
    public static void addOneToCart(int stuffId, Context context) {
        Cursor cursor;
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(context);
        try (SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase()) {
            int oldNumber = 0;
            int cartId = 0;
            // 1. 查现在的number
            cursor = db.query(CART_TABLE, new String[]{"ID", "NUMBER"}, "STUFF_ID=?",
                    new String[]{String.valueOf(stuffId)},
                    null, null, null, null);
            if (cursor.moveToFirst()) { // 如果已经存在记录
                oldNumber = cursor.getInt(cursor.getColumnIndex("NUMBER"));
                cartId = cursor.getInt(cursor.getColumnIndex("ID"));
            }
            // 2. 准备数据
            ContentValues contentValues = new ContentValues();
            contentValues.put("STUFF_ID", stuffId);
            contentValues.put("NUMBER", oldNumber + 1);
            // 3.更新CART表
            if (oldNumber == 0) {// insert
                long result = db.insert(CART_TABLE, null, contentValues);
                Log.d("debug", "insert " + CART_TABLE + " " + result);
            } else {// update
                long result = db.update(CART_TABLE, contentValues, "ID=?",
                        new String[]{String.valueOf(cartId)});
                Log.d("debug", "update " + CART_TABLE + " " + result);
            }
            // 刷新购物车内的商品总数
            refreshCartNum(((FragmentActivity) context).findViewById(R.id.tv_num), context);
        } catch (SQLiteException e) {
            Log.e("sqlite", Objects.requireNonNull(e.getMessage()));
            Toast.makeText(context, "database unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    // 从购物车中减少一件商品，最小为1，不能删除，不含refresh RecycleView，但包括刷新购物车内的商品总数
    public static void removeOneFromCart(int stuffId, Context context) {
        Cursor cursor;
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(context);
        try (SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase()) {
            int oldNumber = 0;
            int cartId = 0;
            // 1. 查现在的number
            cursor = db.query(CART_TABLE, new String[]{"ID", "NUMBER"}, "STUFF_ID=?",
                    new String[]{String.valueOf(stuffId)},
                    null, null, null, null);
            if (cursor.moveToFirst()) { // 如果已经存在记录
                oldNumber = cursor.getInt(cursor.getColumnIndex("NUMBER"));
                cartId = cursor.getInt(cursor.getColumnIndex("ID"));
                if (oldNumber > 1) {
                    // 2. 准备数据
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("STUFF_ID", stuffId);
                    contentValues.put("NUMBER", oldNumber - 1);
                    // 3.更新CART表
                    // update
                    long result = db.update(CART_TABLE, contentValues, "ID=?",
                            new String[]{String.valueOf(cartId)});
                    Log.d("debug", "update " + CART_TABLE + " " + result);
                }
            }
            // 刷新购物车内的商品总数
            refreshCartNum(((FragmentActivity) context).findViewById(R.id.tv_num), context);
        } catch (SQLiteException e) {
            Log.e("sqlite", Objects.requireNonNull(e.getMessage()));
            Toast.makeText(context, "database unavailable", Toast.LENGTH_SHORT).show();
        }
    }

}
