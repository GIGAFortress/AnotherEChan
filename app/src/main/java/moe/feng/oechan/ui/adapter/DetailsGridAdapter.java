package moe.feng.oechan.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import moe.feng.oechan.R;
import moe.feng.oechan.model.DetailsResult;
import moe.feng.oechan.ui.callback.OnItemClickListener;

public class DetailsGridAdapter extends RecyclerView.Adapter<DetailsGridAdapter.ItemHolder> {

	private List<DetailsResult.Episode> data;

	private Picasso picasso;
	private OnItemClickListener onItemClickListener;

	private String CLICK_COUNT_FORMAT, EPISODE_FORMAT;

	public DetailsGridAdapter() {
		data = new ArrayList<>();
	}

	public void setData(List<DetailsResult.Episode> data) {
		this.data = data;
	}

	public void addData(Collection<? extends DetailsResult.Episode> data) {
		this.data.addAll(data);
	}

	public void setOnItemClickListener(OnItemClickListener clickListener) {
		this.onItemClickListener = clickListener;
	}

	/*
	* RecyclerView.Adapter implement方法 onCreateViewHolder
	* parent是一个引用，应该是对应的每个CardView
	* picasso初始化
	* onCreateViewHolder会为每一个View执行两遍，5个卡片执行了10次，6个卡片执行了12次
	* */
	@Override
	public DetailsGridAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (picasso == null || CLICK_COUNT_FORMAT == null || EPISODE_FORMAT == null) {
			picasso = Picasso.with(parent.getContext());
			EPISODE_FORMAT = parent.getResources().getString(R.string.item_episode_format);
			CLICK_COUNT_FORMAT = parent.getResources().getString(R.string.item_click_count_format);
		}

		return new ItemHolder(
				LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_details_list, parent, false)
		);
	}

	/*
	* Adapter implement 方法
	* */
	@Override
	public void onBindViewHolder(DetailsGridAdapter.ItemHolder holder, int position) {
		DetailsResult.Episode item = data.get(position);
		holder.title.setText(String.format(EPISODE_FORMAT, item.getNumber()));
		holder.text.setText(String.format(CLICK_COUNT_FORMAT, item.getClickCount()));

		picasso.load(item.getPreviewUrl())
				.into(
						holder.image,
						PicassoPalette.with(item.getPreviewUrl(), holder.image)
								.use(PicassoPalette.Profile.VIBRANT)
								.intoBackground(holder.itemView)
								.intoTextColor(holder.title, PicassoPalette.Swatch.TITLE_TEXT_COLOR)
								.intoTextColor(holder.text, PicassoPalette.Swatch.BODY_TEXT_COLOR)
								.use(PicassoPalette.Profile.MUTED)
								.intoBackground(holder.image)
				);
	}

	/*
	* implement方法
	* */
	@Override
	public int getItemCount() {
		return data != null ? data.size() : 0;
	}

	/*
	* 必有
	* */
	class ItemHolder extends RecyclerView.ViewHolder {

		TextView title, text;
		ImageView image;

		ItemHolder(View itemView) {
			super(itemView);

			image = (ImageView) itemView.findViewById(R.id.image);
			title = (TextView) itemView.findViewById(R.id.title);
			text = (TextView) itemView.findViewById(R.id.text);

			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (onItemClickListener != null) {
						DetailsResult.Episode item = data.get(getAdapterPosition());
						onItemClickListener.onItemClick(ItemHolder.this, getAdapterPosition(), item);
					}
				}
			});
		}

	}

}
