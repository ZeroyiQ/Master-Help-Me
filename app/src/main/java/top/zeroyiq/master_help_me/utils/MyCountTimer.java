package top.zeroyiq.master_help_me.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import top.zeroyiq.master_help_me.R;

/**
 * Created by ZeroyiQ on 2017/9/4.
 */

public class MyCountTimer extends CountDownTimer {
    private TextView button;
    private Context context;

    /**
     *
     * @param millisInFuture    总计时时间
     * @param countDownInterval 倒计时变化
     * @param button    传入按钮
     * @param context   context
     */
    public MyCountTimer(long millisInFuture, long countDownInterval, TextView button, Context context) {
        super(millisInFuture, countDownInterval);
        this.button = button;
        this.context = context;
    }

    /**
     * 倒计时开始
     * @param millisUntilFinished
     */
    @Override
    public void onTick(long millisUntilFinished) {
        button.setClickable(false);
        button.setText(millisUntilFinished / 1000 + "秒后可重新发送");         // 设置重新发送倒计时
        button.setBackgroundResource(R.drawable.bt_button_gray);
        button.setTextColor(ContextCompat.getColor(context,R.color.colorSixSixBlack));
    }

    /**
     * 倒计时结束
     */
    @Override
    public void onFinish() {
        button.setText("重新获取验证码");
        button.setBackgroundResource(R.drawable.bt_button_blue);
        button.setTextColor(ContextCompat.getColor(context, R.color.colorPureWhite));
        button.setClickable(true);

    }
}
