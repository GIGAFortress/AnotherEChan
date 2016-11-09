package moe.feng.oechan.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import moe.feng.oechan.R;
import moe.feng.oechan.api.DetailsApi;
import moe.feng.oechan.model.BaseMessage;
import moe.feng.oechan.model.DetailsResult;
import moe.feng.oechan.model.PageListResult;
import moe.feng.oechan.model.VideoUrl;
import moe.feng.oechan.support.GsonUtils;
import moe.feng.oechan.ui.adapter.DetailsGridAdapter;
import moe.feng.oechan.ui.callback.OnItemClickListener;
import moe.feng.oechan.ui.common.AbsActivity;
import moe.feng.oechan.ui.video.PlayerActivity;
import moe.feng.oechan.view.GridRecyclerView;

public class DetailsActivity extends AbsActivity
		implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

	private PageListResult.Item data;

	private GridRecyclerView mListView;
	private SwipeRefreshLayout mRefreshLayout;

	private GridLayoutManager mLayoutManager;
	private DetailsGridAdapter mAdapter;

	private boolean hasPlayedAnimation = false;

	private static final String EXTRA_DATA_JSON = "extra_data_json", EXTRA_POSITION = "extra_position";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);

		/** Get data from intent */
		Intent intent = getIntent();
		data = GsonUtils.fromJson(intent.getStringExtra(EXTRA_DATA_JSON), PageListResult.Item.class);

		setTitle(data.getName());
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mListView = $(R.id.recycler_view);
		mRefreshLayout = $(R.id.refresh_layout);

		setUpRecyclerView();
		setUpRefreshLayout();

		new GetDetailsTask().execute();
	}

	private void setUpRefreshLayout() {
		mRefreshLayout.setColorSchemeResources(R.color.teal_a700);
		mRefreshLayout.setOnRefreshListener(this);
	}

	private void setUpRecyclerView() {
		mAdapter = new DetailsGridAdapter();
		mAdapter.setOnItemClickListener(this);
		mLayoutManager = new GridLayoutManager(this, 2);
		mListView.setHasFixedSize(true);
		mListView.setLayoutManager(mLayoutManager);
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void onEnterAnimationComplete() {
		super.onEnterAnimationComplete();
		if (mListView.getAdapter() == null) {
			mListView.setAdapter(mAdapter);
		}
		if (mAdapter != null && mAdapter.getItemCount() != 0) {
			playListAnimation();
		}
	}

	private void playListAnimation() {
		if (!hasPlayedAnimation) {
			hasPlayedAnimation = true;
			mListView.scheduleLayoutAnimation();
		}
	}

	public static void launch(AbsActivity activity, PageListResult.Item data, int position) {
		Intent intent = new Intent(activity, DetailsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		/*
		* GesonUtils.toJson(data)： {"formattedUpdatedAt":"11 月 3 日 11:51","id":230,"name":"一課一練 ONE LESSON ONE FIGHT","updated_At":"2016-11-03T03:25:22.5333983"}
		* */
		intent.putExtra(EXTRA_DATA_JSON, GsonUtils.toJson(data));
		intent.putExtra(EXTRA_POSITION, position);
		activity.startActivity(intent);
	}

	@Override
	public void onRefresh() {
		if (!mRefreshLayout.isRefreshing()) {
			mRefreshLayout.setRefreshing(true);
		}

		new GetDetailsTask().execute();
	}

	@Override
	public void onItemClick(RecyclerView.ViewHolder holder, int position, Object data) {
		new GetVideoUrlTask().execute((DetailsResult.Episode) data);
	}

	private class GetDetailsTask extends AsyncTask<Void, Void, BaseMessage<DetailsResult>> {

		@Override
		protected BaseMessage<DetailsResult> doInBackground(Void... voids) {
			return DetailsApi.getDetails("zh-TW", data.getId());
		}

		@Override
		protected void onPostExecute(BaseMessage<DetailsResult> result) {
			if (isFinishing() || isDestroyed()) return;
			mRefreshLayout.setRefreshing(false);
			if (result.getCode() == BaseMessage.CODE_OKAY) {
				mAdapter.setData(result.getData().getList());
				mAdapter.notifyDataSetChanged();
				playListAnimation();
			}
		}

	}

	private class GetVideoUrlTask extends AsyncTask<DetailsResult.Episode, Void, BaseMessage<VideoUrl>> {

		private DetailsResult.Episode target;

		@Override
		protected BaseMessage<VideoUrl> doInBackground(DetailsResult.Episode... episodes) {
			target = episodes[0];
			return DetailsApi.getVideoUrl("zh-TW", data.getId(), target.getNumber());
		}

		@Override
		protected void onPostExecute(BaseMessage<VideoUrl> result) {
			if (isFinishing() || isDestroyed()) return;
			if (result.getCode() == BaseMessage.CODE_OKAY) {
				PlayerActivity.launch(
						DetailsActivity.this,
						target.getNumber() + " - " + data.getName(),
						result.getData()
				);
			}
		}

	}

}
