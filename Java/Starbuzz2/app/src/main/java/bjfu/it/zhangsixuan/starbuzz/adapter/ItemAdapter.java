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


public class ItemAdapter extends BaseAdapter {

    static final class ItemViewHolder {
        ImageView iv_item;
        TextView tv_item;
        TextView tv_item_id;
    }


    private LayoutInflater mInflater;
    private List<Map<String, Object>> mData;
    private FragmentTransaction transaction;


    public ItemAdapter(Context context, List<Map<String, Object>> mData,
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
            // 加载一个item的布局item_layout
            convertView = mInflater.inflate(R.layout.item_layout, null);
            // 获取引用
            holder.iv_item = convertView.findViewById(R.id.iv_item);
            holder.tv_item = convertView.findViewById(R.id.tv_item);
            holder.tv_item_id = convertView.findViewById(R.id.tv_item_id);
            // convertView为空时候，需要setTag
            convertView.setTag(holder);
        } else {
            // 如果convertView不为空，直接通过getTag重新使用convertView
            holder = (ItemViewHolder) convertView.getTag();
        }

        // 给position位置的holder设置数据
        holder.iv_item.setImageResource((int) mData.get(position).get("favorite_image"));
        holder.tv_item.setText((String) mData.get(position).get("favorite_name"));
        // 使用setTag(postion)，以便后面在OnClick事件中获取到点击的位置
        holder.iv_item.setTag(position);
        holder.tv_item.setTag(position);

        // 点击图片或者名字可以进入对应商品的详情页
        holder.tv_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("debug", "进入详情页面" + v.getTag());
                // 获取点击商品的id号
                int stuffId = (int) mData.get((Integer) v.getTag()).get("favorite_id");
                // 根据商品id跳转到详情页面
                Utils.toDetailFragment(stuffId, transaction);
            }
        });

        // 与上面的点击方法的实现相同
        holder.iv_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 进入详情页面
                Log.d("debug", "进入详情页面" + v.getTag());
                int stuffId = (int) mData.get((Integer) v.getTag()).get("favorite_id");
                //开启事务跳转
                Utils.toDetailFragment(stuffId, transaction);
            }
        });
        return convertView;
    }
}

