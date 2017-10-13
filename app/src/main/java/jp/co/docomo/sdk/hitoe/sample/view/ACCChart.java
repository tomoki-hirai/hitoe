/**
 * Copyright(c) 2015 NTT DOCOMO, INC. All Rights Reserved.
 */

package jp.co.docomo.sdk.hitoe.sample.view;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.achartengine.GraphicalView;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.Log;

import jp.co.docomo.sdk.hitoe.sample.CommonConsts;

/**
 * 加速度グラフクラス
 */
public class ACCChart {

	// LogCatのTAG
	private final String LOG_TAG = this.getClass().getSimpleName();

	// タイトルサイズ
	public static final int CHART_TITLE_SIZE = 25;
	// ラベルサイズ
	public static final int LABELS_SIZE = 16;
	// 軸タイトル
	public static final int AXIS_TITLE_SIZE = 25;
	// xy軸の色
	public static final int AXIS_COLOR = Color.GRAY;
	// グラフ罫線の色
	public static final int GRID_COLOR = Color.GRAY;
	// タイトルの色
	public static final int TITLE_COLOR = Color.GRAY;
	// ラベルの色
	public static final int XLABEL_COLOR = Color.GRAY;
	public static final int YLABEL_COLOR = Color.GRAY;

	// データ数
	private static final int DATA_COUNT = 3;
	// データタイトル
	private static final String[] TITLES = new String[] { "X", "Y", "Z" };
	// データ色
	private static final int[] COLORS = new int[] { Color.RED, Color.parseColor("#008800"), Color.BLUE };
	// データ数
	private static final long MAX_RANGE = 10000;

	// 更新スケジュール
	private ScheduledExecutorService mExecutor;

	// 加速度データ
	private List<XYSeries> mACCList = null;
	//グラフデータ
	private LineChart mLineChart;
	//グラフ表示用ビュー
	private GraphicalView mGraphicalView;
	// グラフ描画
	private XYMultipleSeriesRenderer mXYMultipleSeriesRenderer;
	// 最小データ
	private long m_minX = 0;
	// 最大データ
	private long m_maxX = m_minX + MAX_RANGE;

	/**
	 * コンストラクタ
	 * @param context メインコンテキスト
	 */
	public ACCChart(Context context) {

		this.mACCList = new ArrayList<XYSeries>();
		this.mACCList.add(new XYSeries(TITLES[0]));
		this.mACCList.add(new XYSeries(TITLES[1]));
		this.mACCList.add(new XYSeries(TITLES[2]));
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.addAllSeries(this.mACCList);

		this.mXYMultipleSeriesRenderer = buildRenderer();
		mLineChart = new LineChart(dataset, mXYMultipleSeriesRenderer);
		mGraphicalView = new GraphicalView(context, mLineChart);
	}

	/**
	 * グラフビュー取得
	 * @return グラフビュー
	 */
	public GraphicalView getGraphicalView() {
		return mGraphicalView;
	}

	/**
	 * 加速度データ登録
	 * @param timestamp タイムスタンプ
	 * @param accList 加速度リスト
	 */
	public void setACC(long timestamp, double[] accList) {

		if (this.mACCList.get(0).getItemCount() == 0) {
			m_minX = timestamp;
			m_maxX = timestamp + MAX_RANGE;
		}

		if (timestamp > m_maxX || this.mACCList.get(0).getItemCount() > MAX_RANGE / 40) {
			this.mACCList.get(0).clear();
			this.mACCList.get(1).clear();
			this.mACCList.get(2).clear();

			m_minX = timestamp;
			m_maxX = timestamp + MAX_RANGE;
		}

		this.mACCList.get(0).add(timestamp, accList[0]);
		this.mACCList.get(1).add(timestamp, accList[1]);
		this.mACCList.get(2).add(timestamp, accList[2]);
	}

	/**
	 * グラフ更新
	 */
	private void updateChart(){
		this.mXYMultipleSeriesRenderer.setXAxisMin(m_minX);
		this.mXYMultipleSeriesRenderer.setXAxisMax(m_maxX);

		mGraphicalView.repaint();
	}

	/**
	 * スケジュール開始
	 */
	public void start() { 
		if(mExecutor == null) {
			mExecutor = Executors.newSingleThreadScheduledExecutor();
			mExecutor.scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					updateChart();
				}
	
			}, CommonConsts.ACC_CHART_UPDATE_CYCLE_TIME, CommonConsts.ACC_CHART_UPDATE_CYCLE_TIME, TimeUnit.MILLISECONDS);
		}

	}

	/**
	 * スケジュール停止
	 */
	public void stop(){
		if(mExecutor == null) {
			return;
		}
		mExecutor.shutdown();
		try {
			mExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		mExecutor = null;
	}

	/**
	 * グラフクリア
	 */
	public void clear() {
		this.mACCList.get(0).clear();
		this.mACCList.get(1).clear();
		this.mACCList.get(2).clear();
		mGraphicalView.repaint();
	}

	/**
	 * グラフ描画作成
	 * @return グラフ描画
	 */
	private XYMultipleSeriesRenderer buildRenderer() {
		Log.i(LOG_TAG, "buildRenderer");

		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

		// 複数チャートを同時に表示する場合は、必要数分 XYSeriesRenderer を生成・設定し
		// XYMultipleSeriesRenderer にaddする
		for (int i = 0; i < DATA_COUNT; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(COLORS[i]); // チャートカラー
			r.setLineWidth(4f); // 線の太さ(pt)
			r.setPointStyle(PointStyle.CIRCLE); // ポイントマーカースタイル
			r.setFillPoints(true);
			r.setPointStrokeWidth(1f); // ポイント間隔
			renderer.addSeriesRenderer(r);
		}

		renderer.setPointSize(1f); // ポイントマーカーサイズ
		renderer.setChartTitle("加速度"); // グラフタイトル
		renderer.setChartTitleTextSize(CHART_TITLE_SIZE); // グラフタイトルサイズ

		renderer.setXTitle("経過時間 [ms]"); // x軸タイトル
		renderer.setYTitle("                [G]"); // y軸タイトル

		renderer.setLabelsTextSize(LABELS_SIZE); // ラベルテキストサイズ
		renderer.setLabelsColor(TITLE_COLOR); // ラベルカラー
		renderer.setXLabelsAlign(Align.LEFT); // x軸ラベル位置
		renderer.setYLabelsAlign(Align.RIGHT); // y軸ラベル位置
		
		renderer.setXLabelsColor(XLABEL_COLOR);     // x軸ラベルカラー
		renderer.setYLabelsColor(0, YLABEL_COLOR); // y軸ラベルカラー

		renderer.setAxisTitleTextSize(AXIS_TITLE_SIZE); // 軸タイトルサイズ
		renderer.setAxesColor(AXIS_COLOR); // 軸カラー(xy基準線)
		renderer.setXAxisMin(this.m_minX); // x軸最小(初期)
		renderer.setXAxisMax(this.m_maxX); // x軸最大(初期)
		renderer.setYAxisMin(-3.0); // y軸最小(初期)
		renderer.setYAxisMax(3.0); // y軸最大(初期)

		//renderer.setShowGrid(false); // グリッド表示
		renderer.setShowGridX(true);
		renderer.setShowGridY(true);
		renderer.setGridColor(GRID_COLOR); // グリッドカラー

		renderer.setApplyBackgroundColor(true); // 背景色有効
		renderer.setBackgroundColor(Color.WHITE); // 背景色

		renderer.setMargins(new int[] { 16, 48, 16, 8 }); // 余白(上, 左, 下, 右)
		renderer.setMarginsColor(Color.argb(0, 255, 255, 255));
		// 余白カラー

		renderer.setPanEnabled(false, false); // スクロール許可(x, y)
		//renderer.setPanLimits(new double[] { 0, mXAxisSize * 60 * 60, 0, 0 });

		renderer.setShowLegend(true); // 凡例表示
		renderer.setLegendTextSize(15); // 凡例テキストサイズ
		renderer.setFitLegend(false); // 凡例サイズ合わせ
		renderer.setLegendHeight(45); // 凡例の位置

		//renderer.setZoomButtonsVisible(false);				// ズームボタン表示
		renderer.setZoomButtonsVisible(false); // ズームボタン表示

		//renderer.setZoomEnabled(false,false);				// ズーム許可(x, y)
		renderer.setZoomEnabled(false, false); // ズーム許可(x, y)

		return renderer;
	}

}
