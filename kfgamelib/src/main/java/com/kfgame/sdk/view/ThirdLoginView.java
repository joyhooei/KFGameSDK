package com.kfgame.sdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.kfgame.sdk.common.ConfigUtil;
import com.kfgame.sdk.pojo.ThirdType;
import com.kfgame.sdk.util.ResourceUtil;
import com.kfgame.sdk.view.viewinterface.BaseOnClickListener;

public class ThirdLoginView extends BaseLinearLayout {

	public ThirdLoginView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public ThirdLoginView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ThirdLoginView(Context context) {
		super(context);
	}

	private BaseOnClickListener onClickListener = new BaseOnClickListener() {
		@Override
		public void onBaseClick(View v) {
			thirdLogin((ThirdType) v.getTag());
		}
	};

	@Override
	public void initView() {
		ImageView[] btn = new ImageView[5];
		btn[0] = (ImageView) findViewById(ResourceUtil.getId("iv_login_qq"));
		btn[0].setOnClickListener(onClickListener);
		btn[1] = (ImageView) findViewById(ResourceUtil.getId("iv_login_weixin"));
		btn[1].setOnClickListener(onClickListener);
		btn[2] = (ImageView) findViewById(ResourceUtil.getId("iv_login_weibo"));
		btn[2].setOnClickListener(onClickListener);
		btn[3] = (ImageView) findViewById(ResourceUtil.getId("iv_login_alipay"));
		btn[3].setOnClickListener(onClickListener);
		btn[4] = (ImageView) findViewById(ResourceUtil.getId("iv_login_baidu"));
		btn[4].setOnClickListener(onClickListener);

		int index = 0;
		if (ConfigUtil.getConfigEnable(ConfigUtil.LOGIN_QQ)) {
			setThirdBtn(btn[index], ThirdType.QQ);
			index++;
		}
		if (ConfigUtil.getConfigEnable(ConfigUtil.LOGIN_WEIXIN)) {
			setThirdBtn(btn[index], ThirdType.wechat);
			index++;
		}
		if (ConfigUtil.getConfigEnable(ConfigUtil.LOGIN_WEIBO)) {
			setThirdBtn(btn[index], ThirdType.weibo);
			index++;
		}
		if (ConfigUtil.getConfigEnable(ConfigUtil.LOGIN_ALIPAY)) {
			setThirdBtn(btn[index], ThirdType.alipay);
			index++;
		}
        if (ConfigUtil.getConfigEnable(ConfigUtil.LOGIN_BAIDU)) {
            setThirdBtn(btn[index], ThirdType.baidu);
			index++;
        }
		for (int i = index; i < 5; i++) {
			btn[i].setVisibility(View.INVISIBLE);
		}
	}

	private void setThirdBtn(ImageView v, ThirdType type) {
		v.setTag(type);
		if (type == ThirdType.QQ) {
			v.setImageResource(findDrawableId("kfgame_login_qq"));
		} else if (type == ThirdType.wechat) {
			v.setImageResource(findDrawableId("kfgame_login_weixin"));
		} else if (type == ThirdType.weibo) {
			v.setImageResource(findDrawableId("kfgame_login_weibo"));
		} else if (type == ThirdType.alipay) {
			v.setImageResource(findDrawableId("kfgame_login_alipay"));
		} else if (type == ThirdType.baidu) {
			v.setImageResource(findDrawableId("kfgame_login_baidu"));
		}
	}

	protected static int findDrawableId(String drawableName) {
		return ResourceUtil.getDrawableId(drawableName);
	}

	public static ThirdLoginView createView(Context ctx) {
		if (ctx == null)
			return null;
		ThirdLoginView view = (ThirdLoginView) LayoutInflater.from(ctx)
				.inflate(ResourceUtil.getLayoutId("kfgame_sdk_view_third_login"), null);
		view.initView();
		return view;
	}

	@Override
	public String getViewTitle() {
		return "其它登陆";
	}

}
