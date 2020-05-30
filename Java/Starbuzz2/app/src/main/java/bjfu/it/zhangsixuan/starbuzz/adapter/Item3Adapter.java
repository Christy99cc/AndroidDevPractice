package bjfu.it.zhangsixuan.starbuzz.adapter;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import java.util.List;
import java.util.Map;

import bjfu.it.zhangsixuan.starbuzz.R;
import bjfu.it.zhangsixuan.starbuzz.utils.Utils;


public class Item3Adapter extends BaseAdapter {

    static final class ItemViewHolder {
        ImageView iv_item_category;
        TextView tv_item_category;
    }

    private LayoutInflater mInflater;
    private List<Map<String, Object>> mData;
    private FragmentTransaction transaction;

    public Item3Adapter(Context context, List<Map<String, Object>> mData,
                        FragmentTransaction transaction) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        this.transaction = transaction;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 定义的ViewHolder
        ItemViewHolder holder;
        // 如果convertView为空，加载数据
        if (convertView == null) {
            // 实例化ViewHolder
            holder = new ItemViewHolder();
            // 加载一个item的布局item_category_layout
            convertView = mInflater.inflate(R.layout.item_category_layout, null);
            // 获取引用
            holder.iv_item_category = convertView.findViewById(R.id.iv_item_category);
            holder.tv_item_category = convertView.findViewById(R.id.tv_item_category);
            // convertView为空时候，需要setTag
            convertView.setTag(holder);
        } else { // 如果convertView不为空，直接通过getTag重新使用convertView
            holder = (ItemViewHolder) convertView.getTag();
        }

        // 给position位置的holder设置数据
        holder.iv_item_category.setImageResource((int) mData.get(position).get("image"));
        holder.tv_item_category.setText((String) mData.get(position).get("name"));
        // 使用setTag(postion)，以便后面在OnClick事件中获取到点击的位置
        holder.iv_item_category.setTag(position);
        holder.tv_item_category.setTag(position);

        // 点击list_options中的一项的名字
        holder.tv_item_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("debug", "进入list页面" + v.getTag());
                //开启事务跳转
                // 进入一个分类下的list页面，参数是点击的postion和transction
                Utils.toStuffCategoryFragment((Integer) v.getTag(), transaction);
            }
        });
        // 点击list_options中的一项的图片
        holder.iv_item_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("debug", "进入list页面" + v.getTag());
                //开启事务跳转
                // 进入一个分类下的list页面，参数是点击的postion和transction
                Utils.toStuffCategoryFragment((Integer) v.getTag(), transaction);
            }
        });
        return convertView;
    }
}

