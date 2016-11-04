package moe.feng.oechan.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import moe.feng.oechan.support.DateTimeUtils;
import moe.feng.oechan.support.GsonUtils;

public class PageListResult {

    /*
    * 自定义数据类型，PageListResult， 使用get来获取4种数据类型， */
    private int currentPage, maxPage, itemCount, maxCount;
    /*
    * 内部自定义的Item列表*/
    private List<Item> list;

    /*————————————————————————————*/
    public int getCurrentPage() {
        return currentPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public int getItemCount() {
        return itemCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public List<Item> getList() {
        return list;
    }

    /*————————————————————————————*/
    public String toJsonString() {
        return GsonUtils.toJson(this);
    }

    /*
    * 成功时对
    * */
    public void buildFormattedText(Context context) {
        for (Item item : list) {
            item.buildFormattedUpdatedAt(context);
        }
    }

    public static PageListResult fromJson(String json) {
        return GsonUtils.fromJson(json, PageListResult.class);
    }

    public class Item {


        /*
        * 用于记录名字 ：雙星之陰陽師
        * */
        private String name;

        /*
        * 例如：2016-11-03T03:51:32.689648
        * */
        @SerializedName("updated_At")
        private String updatedAt;
        private int id;

        /*
        * 用于记录什么时候更新的时间 一条数据例子： ”昨天 11:51“
        * */
        private String formattedUpdatedAt;

        public String getName() {
            return name;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public String getFormattedUpdatedAt() {
            return formattedUpdatedAt;
        }

        @SuppressLint("SimpleDateFormat")
        public void buildFormattedUpdatedAt(Context context) {
            String time = updatedAt.substring(0, 19);    //substring：截取字符串 2016-11-03T03:51:32.689648
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Calendar c = GregorianCalendar.getInstance();
            try {

                c.setTime(formatter.parse(time));
                c.setTimeZone(TimeZone.getDefault());
                /*
                * 2016-11-03T03:51:32.689648传入后
                * */
                formattedUpdatedAt = DateTimeUtils.formatTimeStampString(context, c.getTimeInMillis(), DateTimeUtils.FORMAT_TYPE_PERSONAL_FOOTPRINT);
            } catch (ParseException e) {
                e.printStackTrace();
                formattedUpdatedAt = time.replace("T", " ");
            }
        }

        public int getId() {
            return id;
        }

    }

}
