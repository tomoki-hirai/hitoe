/**
 * Copyright(c) 2015 NTT DOCOMO, INC. All Rights Reserved.
 */

package jp.co.docomo.sdk.hitoe.sample;

import java.util.ArrayList;

/**
 * 定数クラス
 */
public class CommonConsts {

	// デバッグフラグ
	public static final boolean DEBUG = true;

	// データの出力先ディレクトリ
	public static final String DEBUG_DIR = "hitoe_sdk";
	// データファイルの拡張子
	public static final String LOG_FILE_SUFFIX = ".csv";

	// preference名
	public static final String PINCODE_PREFERENCE = "hitoe_pincode";
	// preferenceキー
	public static final String PREFERENCE_KEY_FILEOUT = "key_file_out";
	public static final String PREFERENCE_KEY_DISCONNECT_RETRY_TIME = "disconnect_retry_time";
	public static final String PREFERENCE_KEY_DISCONNECT_RETRY_COUNT = "disconnect_retry_count";
	// preference初期値
	public static final boolean PREFERENCE_DEFAULT_FILEOUT = true;
	public static final String PREFERENCE_DEFAULT_DISCONNECT_RETRY_TIME = "10000";
	public static final String PREFERENCE_DEFAULT_DISCONNECT_RETRY_COUNT = "0";

	// State保存キー
	public static final String OUTSTATE_SERVICE_STARTED = "service_started";
	public static final String OUTSTATE_MEASURE_STARTERD = "measure_started";
	public static final String OUTSTATE_FINISHED = "finished";

	// 分離文字
	public static final String BR = System.getProperty("line.separator");
	public static final String VB = "\\|";
	public static final String COMMA = ",";
	public static final String COLON = ":";

	// データキーの先頭文字列
	public static final String RAW_DATA_PREFFIX = "raw.";
	public static final String SAVED_DATA_PREFFIX = "raw.saved";
	public static final String BA_DATA_PREFFIX = "ba.";
	public static final String EX_DATA_PREFFIX = "ex.";

	// コネクションIDの先頭文字列
	public static final String RAW_CONNECTION_PREFFIX = "R";
	public static final String BA_CONNECTION_PREFFIX = "B";
	public static final String EX_CONNECTION_PREFFIX = "E";

	// 取得する分析機能
	public static final String AVAILABLE_RAW_DATA_STR = "raw.ecg\nraw.acc\nraw.rri\nraw.hr\nraw.bat\nraw.leadoff";
	public static final String AVAILABLE_BA_DATA_STR = "ba.extracted_rri\nba.cleaned_rri\nba.interpolated_rri\nba.freq_domain\nba.time_domain";
	public static final String AVAILABLE_EX_DATA_STR = "ex.stress\nex.posture\nex.walk\nex.lr_balance";

	// 描画間隔
	public static final int ECG_CHART_UPDATE_CYCLE_TIME = 40;
	public static final int ACC_CHART_UPDATE_CYCLE_TIME = 40;
	public static final int HR_TEXT_UPDATE_CYCLE_TIME = 1000;
	public static final int LFHF_TEXT_UPDATE_CYCLE_TIME = 1000;
	public static final int POSTURE_STATE_UPDATE_CYCLE_TIME = 1000;

	// 拡張解析の実行単位
	public static final int EX_POSTURE_UNIT_NUM = 25;
	public static final int EX_WALK_UNIT_NUM = 100;
	public static final int EX_LR_BALANCE_UNIT_NUM = 250;

	// 応答コード
	public static final int API_ID_GET_AVAILABLE_SENSOR		= 0x1010;	// getAvailableSensor
	public static final int API_ID_CONNECT					= 0x1020;	// connect
	public static final int API_ID_DISCONNECT				= 0x1021;	// disconnect
	public static final int API_ID_GET_AVAILABLE_DATA		= 0x1030;	// getAvailableData
	public static final int API_ID_ADD_RECIVER				= 0x1040;	// addReciever
	public static final int API_ID_REMOVE_RECEIVER			= 0x1041;	// removeReciever
	public static final int API_ID_GET_STATUS				= 0x1090;	// getStatus

	public static final int	RES_ID_SUCCESS					= 0x00;
	public static final int	RES_ID_FAILURE 					= 0x01;
	public static final int	RES_ID_CONTINUE					= 0x05;
	public static final int RES_ID_API_BUSY					= 0x09;
	public static final int	RES_ID_INVALID_ARG 				= 0x10;
	public static final int	RES_ID_INVALID_PARAM 			= 0x30;
	public static final int	RES_ID_PERMISSION_NOT_GRANTED 	= 0x50;
	public static final int	RES_ID_SENSOR_CONNECT			= 0x60;
	public static final int	RES_ID_SENSOR_CONNECT_FAILURE	= 0x61;
	public static final int	RES_ID_SENSOR_CONNECT_NOTICE	= 0x62;
	public static final int RES_ID_SENSOR_UNAUTHORIZED		= 0x63;
	public static final int	RES_ID_SENSOR_DISCONECT			= 0x65;
	public static final int	RES_ID_SENSOR_DISCONECT_NOTICE	= 0x66;
	public static final int	RES_ID_SENSOR_TEMPORARILY_DISCONNECT	= 0x67;
	public static final int	RES_ID_SENSOR_RECONNECT_FAILURE	= 0x68;
	public static final int	RES_ID_SENSOR_RECONNECT	= 0x69;

	// 利用可能センサ一覧設定値
	public static final String GET_AVAILABLE_SENSOR_DEVICE_TYPE = "hitoe D01";
	public static final int GET_AVAILABLE_SENSOR_PARAM_SEARCH_TIME = 5000;

	// 接続設定値
	public static final int CONNECT_NOPACKET_RETRY_TIME = 5000;

	// Rawデータレシーバ登録設定値
	public static final int ADD_RECEIVER_PARAM_ECG_SAMPLING_INTERVAL = 40;
	public static final int ADD_RECEIVER_PARAM_ACC_SAMPLING_INTERVAL = 40;
	public static final int ADD_RECEIVER_PARAM_RRI_SAMPLING_INTERVAL = 1000;
	public static final int ADD_RECEIVER_PARAM_HR_SAMPLING_INTERVAL = 1000;
	public static final int ADD_RECEIVER_PARAM_BAT_SAMPLING_INTERVAL = 10000;
	private static final int ADD_RECEIVER_PARAM_LEADOFF_SAMPLING_INTERVAL = 1000;

	// 基本分析レシーバ登録設定値
	public static final int ADD_RECEIVER_PARAM_BA_SAMPLING_INTERVAL = 4000;
	public static final int ADD_RECEIVER_PARAM_BA_ECG_THRESHHOLD = 250;
	public static final int ADD_RECEIVER_PARAM_BA_SKIP_COUNT = 50;
	public static final int ADD_RECEIVER_PARAM_BA_RRI_MIN = 240;
	public static final int ADD_RECEIVER_PARAM_BA_RRI_MAX = 3999;
	public static final int ADD_RECEIVER_PARAM_BA_SAMPLE_COUNT = 20;
	public static final String ADD_RECEIVER_PARAM_BA_RRI_INPUT = "extracted_rri";
	public static final int ADD_RECEIVER_PARAM_BA_FREQ_SAMPLING_INTERVAL = 4000;
	public static final int ADD_RECEIVER_PARAM_BA_FREQ_SAMPLING_WINDOW = 60;
	public static final int ADD_RECEIVER_PARAM_BA_RRI_SAMPLING_RATE = 8;
	public static final int ADD_RECEIVER_PARAM_BA_TIME_SAMPLING_INTERVAL = 4000;
	public static final int ADD_RECEIVER_PARAM_BA_TIME_SAMPLING_WINDOW = 60;

	public static final String getRawParam(ArrayList<String> keyList) {

		StringBuilder paramStringBuilder = new StringBuilder();
		for (int i = 0; i < keyList.size(); i++) {

			if (keyList.get(i).equals("raw.ecg")) {

				if (paramStringBuilder.lastIndexOf(CommonConsts.BR) != paramStringBuilder.length() - 1) {

					paramStringBuilder.append(CommonConsts.BR);
				}
				if (CommonConsts.ADD_RECEIVER_PARAM_ECG_SAMPLING_INTERVAL != -1) {
					paramStringBuilder.append("raw.ecg_interval=" + String.valueOf(CommonConsts.ADD_RECEIVER_PARAM_ECG_SAMPLING_INTERVAL));
				}
			} else if (keyList.get(i).equals("raw.acc")) {

				if (paramStringBuilder.lastIndexOf(CommonConsts.BR) != paramStringBuilder.length() - 1) {

					paramStringBuilder.append(CommonConsts.BR);
				}
				if (CommonConsts.ADD_RECEIVER_PARAM_ACC_SAMPLING_INTERVAL != -1) {
					paramStringBuilder.append("raw.acc_interval=" + String.valueOf(CommonConsts.ADD_RECEIVER_PARAM_ACC_SAMPLING_INTERVAL));
				}
			} else if (keyList.get(i).equals("raw.rri")) {

				if (paramStringBuilder.lastIndexOf(CommonConsts.BR) != paramStringBuilder.length() - 1) {

					paramStringBuilder.append(CommonConsts.BR);
				}
				if (CommonConsts.ADD_RECEIVER_PARAM_RRI_SAMPLING_INTERVAL != -1) {
					paramStringBuilder.append("raw.rri_interval=" + String.valueOf(CommonConsts.ADD_RECEIVER_PARAM_RRI_SAMPLING_INTERVAL));
				}
			} else if (keyList.get(i).equals("raw.hr")) {

				if (paramStringBuilder.lastIndexOf(CommonConsts.BR) != paramStringBuilder.length() - 1) {

					paramStringBuilder.append(CommonConsts.BR);
				}
				if (CommonConsts.ADD_RECEIVER_PARAM_HR_SAMPLING_INTERVAL != -1) {
					paramStringBuilder.append("raw.hr_interval=" + String.valueOf(CommonConsts.ADD_RECEIVER_PARAM_HR_SAMPLING_INTERVAL));
				}
			} else if (keyList.get(i).equals("raw.bat")) {

				if (paramStringBuilder.lastIndexOf(CommonConsts.BR) != paramStringBuilder.length() - 1) {

					paramStringBuilder.append(CommonConsts.BR);
				}
				if (CommonConsts.ADD_RECEIVER_PARAM_BAT_SAMPLING_INTERVAL != -1) {
					paramStringBuilder.append("raw.bat_interval=" + String.valueOf(CommonConsts.ADD_RECEIVER_PARAM_BAT_SAMPLING_INTERVAL));
				}
			} else if (keyList.get(i).equals("raw.leadoff")) {

				if (paramStringBuilder.lastIndexOf(CommonConsts.BR) != paramStringBuilder.length() - 1) {

					paramStringBuilder.append(CommonConsts.BR);
				}
				if (CommonConsts.ADD_RECEIVER_PARAM_LEADOFF_SAMPLING_INTERVAL != -1) {
					paramStringBuilder.append("raw.leadoff_interval=" + String.valueOf(CommonConsts.ADD_RECEIVER_PARAM_LEADOFF_SAMPLING_INTERVAL));
				}
			} else if (keyList.get(i).equals("raw.saved_hr") || keyList.get(i).equals("raw.saved_rri")) {

			} else {

			}
		}

		String paramString = paramStringBuilder.toString();

		return paramString;
	}

	public static final String getBaParam(ArrayList<String> keyList) {

		StringBuilder paramStringBuilder = new StringBuilder();
		for(int i = 0; i < keyList.size(); i++) {

			if(keyList.get(i).equals("ba.extracted_rri")) {

				if(CommonConsts.ADD_RECEIVER_PARAM_BA_SAMPLING_INTERVAL != -1 && paramStringBuilder.indexOf("ba.sampling_interval") == -1) {
					if(paramStringBuilder.length() > 0 && paramStringBuilder.lastIndexOf(CommonConsts.BR) != paramStringBuilder.length() - 1) {

						paramStringBuilder.append(CommonConsts.BR);
					}
					paramStringBuilder.append("ba.sampling_interval="+String.valueOf(CommonConsts.ADD_RECEIVER_PARAM_BA_SAMPLING_INTERVAL));
				}
				if(CommonConsts.ADD_RECEIVER_PARAM_BA_ECG_THRESHHOLD != -1) {
					if(paramStringBuilder.length() > 0 && paramStringBuilder.lastIndexOf(CommonConsts.BR) != paramStringBuilder.length() - 1) {

						paramStringBuilder.append(CommonConsts.BR);
					}
					paramStringBuilder.append("ba.ecg_threshhold="+String.valueOf(CommonConsts.ADD_RECEIVER_PARAM_BA_ECG_THRESHHOLD));
				}
				if(CommonConsts.ADD_RECEIVER_PARAM_BA_SKIP_COUNT != -1) {
					if(paramStringBuilder.length() > 0 && paramStringBuilder.lastIndexOf(CommonConsts.BR) != paramStringBuilder.length() - 1) {

						paramStringBuilder.append(CommonConsts.BR);
					}
					paramStringBuilder.append("ba.ecg_skip_count="+String.valueOf(CommonConsts.ADD_RECEIVER_PARAM_BA_SKIP_COUNT));
				}

			} else if(keyList.get(i).equals("ba.cleaned_rri")) {

				if(CommonConsts.ADD_RECEIVER_PARAM_BA_SAMPLING_INTERVAL != -1 && paramStringBuilder.indexOf("ba.sampling_interval") == -1) {
					if(paramStringBuilder.length() > 0 && paramStringBuilder.lastIndexOf(CommonConsts.BR) != paramStringBuilder.length() - 1) {

						paramStringBuilder.append(CommonConsts.BR);
					}
					paramStringBuilder.append("ba.sampling_interval="+String.valueOf(CommonConsts.ADD_RECEIVER_PARAM_BA_SAMPLING_INTERVAL));
				}
				if(CommonConsts.ADD_RECEIVER_PARAM_BA_RRI_MIN != -1) {
					if(paramStringBuilder.length() > 0 && paramStringBuilder.lastIndexOf(CommonConsts.BR) != paramStringBuilder.length() - 1) {

						paramStringBuilder.append(CommonConsts.BR);
					}
					paramStringBuilder.append("ba.rri_min="+String.valueOf(CommonConsts.ADD_RECEIVER_PARAM_BA_RRI_MIN));
				}
				if(CommonConsts.ADD_RECEIVER_PARAM_BA_RRI_MAX != -1) {
					if(paramStringBuilder.length() > 0 && paramStringBuilder.lastIndexOf(CommonConsts.BR) != paramStringBuilder.length() - 1) {

						paramStringBuilder.append(CommonConsts.BR);
					}
					paramStringBuilder.append("ba.rri_max="+String.valueOf(CommonConsts.ADD_RECEIVER_PARAM_BA_RRI_MAX));
				}
				if(CommonConsts.ADD_RECEIVER_PARAM_BA_SAMPLE_COUNT != -1) {
					if(paramStringBuilder.length() > 0 && paramStringBuilder.lastIndexOf(CommonConsts.BR) != paramStringBuilder.length() - 1) {

						paramStringBuilder.append(CommonConsts.BR);
					}
					paramStringBuilder.append("ba.sample_count="+String.valueOf(CommonConsts.ADD_RECEIVER_PARAM_BA_SAMPLE_COUNT));
				}
				if(CommonConsts.ADD_RECEIVER_PARAM_BA_RRI_INPUT != null) {

					if(paramStringBuilder.length() > 0 && paramStringBuilder.lastIndexOf(CommonConsts.BR) != paramStringBuilder.length() - 1) {

						paramStringBuilder.append(CommonConsts.BR);
					}
					paramStringBuilder.append("ba.rri_input="+String.valueOf(CommonConsts.ADD_RECEIVER_PARAM_BA_RRI_INPUT));
				}
			} else if(keyList.get(i).equals("ba.interpolated_rri")) {

				if (CommonConsts.ADD_RECEIVER_PARAM_BA_FREQ_SAMPLING_INTERVAL != -1 && paramStringBuilder.indexOf("ba.freq_sampling_interval") == -1) {
					if(paramStringBuilder.length() > 0 && paramStringBuilder.lastIndexOf(CommonConsts.BR) != paramStringBuilder.length() - 1) {

						paramStringBuilder.append(CommonConsts.BR);
					}
					paramStringBuilder.append("ba.freq_sampling_interval=" + String.valueOf(CommonConsts.ADD_RECEIVER_PARAM_BA_FREQ_SAMPLING_INTERVAL));
				}
				if(CommonConsts.ADD_RECEIVER_PARAM_BA_FREQ_SAMPLING_WINDOW != -1 && paramStringBuilder.indexOf("ba.freq_sampling_window") == -1) {
					if (paramStringBuilder.length() > 0 && paramStringBuilder.lastIndexOf(CommonConsts.BR) != paramStringBuilder.length() - 1) {

						paramStringBuilder.append(CommonConsts.BR);
					}
					paramStringBuilder.append("ba.freq_sampling_window="+String.valueOf(CommonConsts.ADD_RECEIVER_PARAM_BA_FREQ_SAMPLING_WINDOW));
				}
				if(CommonConsts.ADD_RECEIVER_PARAM_BA_RRI_SAMPLING_RATE != -1 && paramStringBuilder.indexOf("ba.rri_sampling_rate") == -1) {
					if(paramStringBuilder.length() > 0 && paramStringBuilder.lastIndexOf(CommonConsts.BR) != paramStringBuilder.length() - 1) {

						paramStringBuilder.append(CommonConsts.BR);
					}
					paramStringBuilder.append("ba.rri_sampling_rate="+String.valueOf(CommonConsts.ADD_RECEIVER_PARAM_BA_RRI_SAMPLING_RATE));
				}
			} else if(keyList.get(i).equals("ba.freq_domain")) {

				if (CommonConsts.ADD_RECEIVER_PARAM_BA_FREQ_SAMPLING_INTERVAL != -1 && paramStringBuilder.indexOf("ba.freq_sampling_interval") == -1) {
					if(paramStringBuilder.length() > 0 && paramStringBuilder.lastIndexOf(CommonConsts.BR) != paramStringBuilder.length() - 1) {

						paramStringBuilder.append(CommonConsts.BR);
					}
					paramStringBuilder.append("ba.freq_sampling_interval=" + String.valueOf(CommonConsts.ADD_RECEIVER_PARAM_BA_FREQ_SAMPLING_INTERVAL));
				}
				if(CommonConsts.ADD_RECEIVER_PARAM_BA_FREQ_SAMPLING_WINDOW != -1 && paramStringBuilder.indexOf("ba.freq_sampling_window") == -1) {
					if (paramStringBuilder.length() > 0 && paramStringBuilder.lastIndexOf(CommonConsts.BR) != paramStringBuilder.length() - 1) {

						paramStringBuilder.append(CommonConsts.BR);
					}
					paramStringBuilder.append("ba.freq_sampling_window="+String.valueOf(CommonConsts.ADD_RECEIVER_PARAM_BA_FREQ_SAMPLING_WINDOW));
				}
				if(CommonConsts.ADD_RECEIVER_PARAM_BA_RRI_SAMPLING_RATE != -1 && paramStringBuilder.indexOf("ba.rri_sampling_rate") == -1) {
					if(paramStringBuilder.length() > 0 && paramStringBuilder.lastIndexOf(CommonConsts.BR) != paramStringBuilder.length() - 1) {

						paramStringBuilder.append(CommonConsts.BR);
					}
					paramStringBuilder.append("ba.rri_sampling_rate="+String.valueOf(CommonConsts.ADD_RECEIVER_PARAM_BA_RRI_SAMPLING_RATE));
				}
			} else if(keyList.get(i).equals("ba.time_domain")) {

				if (CommonConsts.ADD_RECEIVER_PARAM_BA_TIME_SAMPLING_INTERVAL != -1) {
					if(paramStringBuilder.length() > 0 && paramStringBuilder.lastIndexOf(CommonConsts.BR) != paramStringBuilder.length() - 1) {

						paramStringBuilder.append(CommonConsts.BR);
					}
					paramStringBuilder.append("ba.time_sampling_interval=" + String.valueOf(CommonConsts.ADD_RECEIVER_PARAM_BA_TIME_SAMPLING_INTERVAL));
				}
				if(CommonConsts.ADD_RECEIVER_PARAM_BA_TIME_SAMPLING_WINDOW != -1) {
					if(paramStringBuilder.length() > 0 && paramStringBuilder.lastIndexOf(CommonConsts.BR) != paramStringBuilder.length() - 1) {

						paramStringBuilder.append(CommonConsts.BR);
					}
					paramStringBuilder.append("ba.time_sampling_window="+String.valueOf(CommonConsts.ADD_RECEIVER_PARAM_BA_TIME_SAMPLING_WINDOW));
				}
			} else {

			}
		}

		String paramString = paramStringBuilder.toString();

		return paramString;
	}
	// 拡張分析レシーバ登録設定値
	public static final String ADD_RECEIVER_PARAM_EX_ACC_AXIS_XYZ = "XYZ";
	public static final int ADD_RECEIVER_PARAM_EX_POSTURE_WINDOW = 1;
	public static final double ADD_RECEIVER_PARAM_EX_WALK_STRIDE = 0.81;
	public static final double ADD_RECEIVER_PARAM_EX_RUN_STRIDE_COF = 0.0091;
	public static final double ADD_RECEIVER_PARAM_EX_RUN_STRIDE_INT = 0.1806;

	// 姿勢推定表示設定
	public static final int BACK_FORWARD_THRESHOLD = 30;
	public static final int LEFT_RIGHT_THRESHOLD = 20;

	public static final String getExParam(String keyString) {

		StringBuilder paramStringBuilder = new StringBuilder();
		if(keyString.equals("ex.stress")) {

		} else if(keyString.equals("ex.posture")) {

			if(CommonConsts.ADD_RECEIVER_PARAM_EX_ACC_AXIS_XYZ != null) {

				if(paramStringBuilder.length() > 0) {

					paramStringBuilder.append(CommonConsts.BR);
				}
				paramStringBuilder.append("ex.acc_axis_xyz="+CommonConsts.ADD_RECEIVER_PARAM_EX_ACC_AXIS_XYZ);
			}
			if(CommonConsts.ADD_RECEIVER_PARAM_EX_POSTURE_WINDOW != -1) {

				if(paramStringBuilder.length() > 0) {

					paramStringBuilder.append(CommonConsts.BR);
				}
				paramStringBuilder.append("ex.posture_window="+CommonConsts.ADD_RECEIVER_PARAM_EX_POSTURE_WINDOW);
			}
		} else if(keyString.equals("ex.walk")) {

			if(CommonConsts.ADD_RECEIVER_PARAM_EX_ACC_AXIS_XYZ != null) {

				if(paramStringBuilder.length() > 0) {

					paramStringBuilder.append(CommonConsts.BR);
				}
				paramStringBuilder.append("ex.acc_axis_xyz="+CommonConsts.ADD_RECEIVER_PARAM_EX_ACC_AXIS_XYZ);
			}
			if(CommonConsts.ADD_RECEIVER_PARAM_EX_WALK_STRIDE != -1) {

				if(paramStringBuilder.length() > 0) {

					paramStringBuilder.append(CommonConsts.BR);
				}
				paramStringBuilder.append("ex.walk_stride="+CommonConsts.ADD_RECEIVER_PARAM_EX_WALK_STRIDE);
			}
			if(CommonConsts.ADD_RECEIVER_PARAM_EX_RUN_STRIDE_COF != -1) {

				if(paramStringBuilder.length() > 0) {

					paramStringBuilder.append(CommonConsts.BR);
				}
				paramStringBuilder.append("ex.run_stride_cof="+CommonConsts.ADD_RECEIVER_PARAM_EX_RUN_STRIDE_COF);
			}
			if(CommonConsts.ADD_RECEIVER_PARAM_EX_RUN_STRIDE_INT != -1) {

				if(paramStringBuilder.length() > 0) {

					paramStringBuilder.append(CommonConsts.BR);
				}
				paramStringBuilder.append("ex.run_stride_int="+CommonConsts.ADD_RECEIVER_PARAM_EX_RUN_STRIDE_INT);
			}
		} else if(keyString.equals("ex.lr_balance")) {
			if(CommonConsts.ADD_RECEIVER_PARAM_EX_ACC_AXIS_XYZ != null) {

				if(paramStringBuilder.length() > 0) {

					paramStringBuilder.append(CommonConsts.BR);
				}
				paramStringBuilder.append("ex.acc_axis_xyz="+CommonConsts.ADD_RECEIVER_PARAM_EX_ACC_AXIS_XYZ);
			}
		} else {

			// キーがない場合
		}

		String paramString = paramStringBuilder.toString();

		return paramString;
	}

}
