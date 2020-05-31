package bjfu.it.zhangsixuan.starbuzz.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.util.BannerUtils;

import java.util.List;

import bjfu.it.zhangsixuan.starbuzz.R;
import bjfu.it.zhangsixuan.starbuzz.bean.DataBean;
import bjfu.it.zhangsixuan.starbuzz.viewholder.ImageTitleHolder;

/**
 * https://github.com/youth5201314/banner/
 * 自定义布局，可以看demo，可以自己随意发挥
 */

/**
 * 自定义布局，图片+标题
 */

public class ImageTitleAdapter<D, I extends RecyclerView.ViewHolder>
        extends BannerAdapter<DataBean, ImageTitleHolder> {

    public ImageTitleAdapter(List<DataBean> mDatas) {
        super(mDatas);
    }

    @Override
    public ImageTitleHolder onCreateHolder(ViewGroup parent, int viewType) {
        // 加载banner_image_title
        return new ImageTitleHolder(BannerUtils.getView(parent,R.layout.banner_image_title));
    }

    @Override
    public void onBindView(ImageTitleHolder holder, DataBean data, int position, int size) {
        holder.imageView.setImageResource(data.getImageRes());
        holder.title.setText(data.getName());
    }
}