package com.healthmanage.ylis.common;

import com.healthmanage.ylis.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 自定义对话框
 * 
 * @author cxw
 *
 */
public class DefineDialog extends Dialog {

	public static int HAS_TITLE = 0;// 有标题的布局
	public static int HAS_TITLE_LEFT = 2;// 有标题,内容靠左的布局
	public static int NO_TITLE = 1;// 无标题的布局
	public static int SPORT = 3;// 运动健康
	public static int ADD_DATA = 4;// 维护数据
	public static int JKSQ_NOTICE = 5;// 维护数据
	public static int NURSING_TASK_CANCLE = 6;// 取消订单

	public DefineDialog(Context context) {
		super(context);
	}

	public DefineDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private String title;// 标题
		private String message;// 消息
		private String positiveBtnText;// 确定按钮内容
		private String negativeBtnText;// 取消按钮内容
		private View contentView;// 视图
		private DialogInterface.OnClickListener positiveBtnClickLis;// 确定点击监听
		private DialogInterface.OnClickListener negativeBtnClickLis;// 取消点击监听
		private View layout = null;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public String getMessage() {
			return message;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder setContentView(View view) {
			this.contentView = view;
			return this;
		}

		public Builder setPostitiveBtn(int positiveBtnText,
				DialogInterface.OnClickListener listener) {
			this.positiveBtnText = (String) context.getText(positiveBtnText);
			this.positiveBtnClickLis = listener;
			return this;
		}

		public Builder setPostitiveBtn(String positiveBtnText,
				DialogInterface.OnClickListener listener) {
			this.positiveBtnText = positiveBtnText;
			this.positiveBtnClickLis = listener;
			return this;
		}

		public Builder setNegativeBtn(int negativeBtnText,
				DialogInterface.OnClickListener listener) {
			this.negativeBtnText = (String) context.getText(negativeBtnText);
			this.negativeBtnClickLis = listener;
			return this;
		}

		public Builder setNegativeBtn(String negativeBtnText,
				DialogInterface.OnClickListener listener) {
			this.negativeBtnText = negativeBtnText;
			this.negativeBtnClickLis = listener;
			return this;
		}

		public DefineDialog create(int position) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final DefineDialog dialog = new DefineDialog(context,
					R.style.Dialog);
			if (position == HAS_TITLE) {
				layout = inflater.inflate(
						R.layout.dialog_normal_has_title_layout, null);
				((TextView) layout.findViewById(R.id.title)).setText(title);
			}
			if (layout == null) {
				Log.e("layout", "layout == null");
			}
			if (dialog == null) {
				Log.e("dialog", "dialog == null");
			}
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			if (null != positiveBtnText && position != SPORT
					&& position != JKSQ_NOTICE) {
				TextView posBtn = (TextView) layout
						.findViewById(R.id.positiveButton);
				posBtn.setText(positiveBtnText);
				if (null != positiveBtnClickLis) {
					posBtn.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							message = ((TextView) layout
									.findViewById(R.id.message)).getText()
									.toString();
							positiveBtnClickLis.onClick(dialog,
									DialogInterface.BUTTON_POSITIVE);
						}
					});
				}
			} else if (null == positiveBtnText && position != SPORT
					&& position != JKSQ_NOTICE) {
				layout.findViewById(R.id.divider_imageview).setVisibility(
						View.GONE);
				layout.findViewById(R.id.positiveButton).setVisibility(
						View.GONE);
			}

			if (null != negativeBtnText && position != SPORT
					&& position != JKSQ_NOTICE) {
				TextView negaBtn = (TextView) layout
						.findViewById(R.id.negativeButton);
				negaBtn.setText(negativeBtnText);
				if (null != negativeBtnClickLis) {
					negaBtn.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							negativeBtnClickLis.onClick(dialog,
									DialogInterface.BUTTON_NEGATIVE);
						}
					});
				}
			} else if (null == negativeBtnText && position != SPORT
					&& position != JKSQ_NOTICE) {
				layout.findViewById(R.id.divider_imageview).setVisibility(
						View.GONE);
				layout.findViewById(R.id.negativeButton).setVisibility(
						View.GONE);
			}
			if (message != null) {
				((TextView) layout.findViewById(R.id.message)).setText(message);
			} else if (contentView != null) {
				((LinearLayout) layout.findViewById(R.id.message_layout))
						.removeAllViews();
				((LinearLayout) layout.findViewById(R.id.message_layout))
						.addView(contentView, new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT));
			}
			dialog.setContentView(layout);
			return dialog;
		}

	}

}
