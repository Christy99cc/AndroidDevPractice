package bjfu.it.zhangsixuan.starbuzz.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import java.util.List;
import java.util.Map;

import bjfu.it.zhangsixuan.starbuzz.R;
import bjfu.it.zhangsixuan.starbuzz.utils.Utils;


public class Item2Adapter extends BaseAdapter {
    static final class ViewHolder {
        TextView tv_stuffId;
        TextView tv_stuffName;
        TextView tv_add_to_cart;
    }

    private List<Map<String, Object>> mData;
    private LayoutInflater mInflater;
    private FragmentTransaction transaction;
    private Context context;

    public Item2Adapter(Context context, List<Map<String, Object>> mData, FragmentTransaction transaction) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        this.transaction = transaction;
        this.context = context;
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
        ViewHolder holder;
        // 如果convertView为空，加载数据
        if (convertView == null) {
            // 实例化ViewHolder
            holder = new ViewHolder();
            // 加载布局stuff_list_item_layout
            convertView = mInflater.inflate(R.layout.stuff_list_item_layout, null);
            // 获取引用
            holder.tv_stuffId = convertView.findViewById(R.id.stuff_id);
            holder.tv_stuffName = convertView.findViewById(R.id.stuff_name);
            holder.tv_add_to_cart = convertView.findViewById(R.id.tv_add_to_cart);
            // convertView为空时候，需要setTag
            convertView.setTag(holder);
        } else {
            // 如果convertView不为空，直接通过getTag重新使用convertView
            holder = (ViewHolder) convertView.getTag();
        }

        // 给position位置的holder设置数据
        holder.tv_stuffName.setText((String) mData.get(position).get("stuffName"));
        holder.tv_stuffId.setText(String.valueOf(mData.get(position).get("stuffId")));
        // 使用setTag(postion)，以便后面在OnClick事件中获取到点击的位置
        holder.tv_stuffName.setTag(position);
        holder.tv_add_to_cart.setTag(position);

        // 点击+号，将商品加入购物车
        holder.tv_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("debug", "加入购物车" + v.getTag());
                // 获取stuffId
                int stuffId = (int) mData.get((Integer) v.getTag()).get("stuffId");
                // 加入购物车
                Utils.addOneToCart(stuffId, context);
            }
        });
        // 点击商品名称，进入详情页面
        holder.tv_stuffName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("debug", "进入详情页面" + v.getTag());
                // 获取stuffId
                int stuffId = (int) mData.get((Integer) v.getTag()).get("stuffId");
                // 进入详情页面
                Utils.toDetailFragment(stuffId, transaction);
            }
        });
        return convertView;
    }
}