package edu.rosehulman.golfballdelivery;

import android.os.Handler;
import android.widget.Toast;

import edu.rosehulman.me435.NavUtils;
import edu.rosehulman.me435.RobotActivity;

/**
 * Created by frazeema on 4/24/2018.
 */

public class Scripts {
    private Handler mCommandHandler = new Handler();

    private GolfBallDeliveryActivity mActivity;

    private int ARM_REMOVAL_TIME = 3000;

    public Scripts(GolfBallDeliveryActivity activity) {
        mActivity = activity;
    }

    public void testStraightScript() {
        mActivity.sendWheelSpeed(mActivity.mLeftStraightPwmValue, mActivity.mRightStraightPwmValue);
        Toast.makeText(mActivity,"Begin driving", Toast.LENGTH_SHORT).show();
        mCommandHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mActivity.sendWheelSpeed(0,0);
                Toast.makeText(mActivity,"Stop driving", Toast.LENGTH_SHORT).show();
            }
        }, 8000);
    }
    public void nearBallScript() {
        double distanceToNearBall  = NavUtils.getDistance(15, 0 ,90, 50);
        long driveTimeMs = (long) (distanceToNearBall/ RobotActivity.DEFAULT_SPEED_FT_PER_SEC * 1000);
        // FOR testing, this has been made shorter
        driveTimeMs = 3000;
        mActivity.sendWheelSpeed(mActivity.mLeftStraightPwmValue, mActivity.mRightStraightPwmValue);
        mCommandHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mActivity.sendWheelSpeed(0,0);
                removeBallAtLocationScript(mActivity.mNearBallLocation);
            }
        }, driveTimeMs);

        mCommandHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mActivity.mState == GolfBallDeliveryActivity.State.NEAR_BALL_SCRIPT) {
                    mActivity.setState(GolfBallDeliveryActivity.State.DRIVE_TOWARD_FAR_BALL);
                }
            }
        }, driveTimeMs + ARM_REMOVAL_TIME);
    }
    public void farBallScript() {
        mActivity.sendWheelSpeed(0,0);
        removeBallAtLocationScript(mActivity.mFarBallLocation);
        mCommandHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mActivity.sendWheelSpeed(0,0);
                if(mActivity.mWhiteBallLocation != 0) {
                    removeBallAtLocationScript(mActivity.mWhiteBallLocation);
                }
                if(mActivity.mState == GolfBallDeliveryActivity.State.FAR_BALL_SCRIPT) {
                    mActivity.setState(GolfBallDeliveryActivity.State.DRIVE_TOWARDS_HOME);
                }
            }
        }, ARM_REMOVAL_TIME);

    }
    private void removeBallAtLocationScript(final int location) {
        mActivity.sendCommand("ATTACH 111111");
        mCommandHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mActivity.sendCommand("POSITION 83 90 0 -90 90");
            }
        }, 10);
        mCommandHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mActivity.sendCommand("POSITION 90 141 -60 -180 169");
            }
        }, 2000);
        mCommandHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mActivity.setLocationToColor(location, GolfBallDeliveryActivity.BallColor.NONE);
            }
        }, ARM_REMOVAL_TIME);
    }
}
